package com.example.data.parser

import android.content.Context
import android.net.Uri
import com.example.data.db.AppDatabase
import com.example.data.model.BacklogEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.PreventiveEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

data class ImportResult(
    val type: SheetType,
    val totalProcessed: Int,
    val successCount: Int,
    val errorCount: Int,
    val validatedTagsCount: Int,
    val message: String
)

enum class SheetType {
    PREVENTIVAS,
    BACKLOGS,
    HISTORICO,
    UNKNOWN
}

data class IntegrityAuditReport(
    val validatedTags: Int = 148,
    val formattingErrors: Int = 0,
    val offlinePending: Int = 3,
    val auditPercentage: Int = 100,
    val lastSyncTime: String = "Hoje às 13:45",
    val technician: String = "Roberto Silva (Téc. Sênior)"
)

object SpreadsheetParser {

    /**
     * Importa arquivo local selecionado pelo usuário via SAF (Uri)
     */
    suspend fun importFromUri(
        context: Context,
        database: AppDatabase,
        uri: Uri,
        forceType: SheetType? = null
    ): ImportResult = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: ""
        val fileName = uri.lastPathSegment ?: ""

        val rawRows: List<List<String>> = try {
            contentResolver.openInputStream(uri)?.use { stream ->
                if (fileName.endsWith(".xlsx", ignoreCase = true) || mimeType.contains("spreadsheetml", ignoreCase = true)) {
                    parseXlsxStream(stream)
                } else {
                    parseCsvStream(stream)
                }
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: tentar como CSV se falhar
            try {
                contentResolver.openInputStream(uri)?.use { parseCsvStream(it) } ?: emptyList()
            } catch (ex: Exception) {
                emptyList()
            }
        }

        if (rawRows.isEmpty()) {
            return@withContext ImportResult(
                type = forceType ?: SheetType.UNKNOWN,
                totalProcessed = 0,
                successCount = 0,
                errorCount = 1,
                validatedTagsCount = 0,
                message = "Nenhuma linha válida encontrada no arquivo."
            )
        }

        val detectedType = forceType ?: detectSheetType(rawRows.firstOrNull() ?: emptyList())
        processRowsAndSave(database, detectedType, rawRows)
    }

    /**
     * Detecta o tipo de planilha baseado nas colunas do cabeçalho
     */
    fun detectSheetType(header: List<String>): SheetType {
        val normalized = header.map { it.trim().uppercase() }
        return when {
            normalized.any { it.contains("FAROL") } || normalized.any { it.contains("STATUS DA OS") } && normalized.any { it.contains("OBSERVAÇÃO") } -> SheetType.BACKLOGS
            normalized.any { it.contains("DATA DE ENCERRAMENTO") } || normalized.any { it.contains("EQUIPAMENTO - 1") } -> SheetType.HISTORICO
            normalized.any { it.contains("DATA DE PLANEJ") } || normalized.any { it.contains("SERVIÇO - DESCRIÇÃO") } -> SheetType.PREVENTIVAS
            else -> SheetType.PREVENTIVAS
        }
    }

    /**
     * Processa as linhas mapeadas e salva no Room Database com verificação de TAGs
     */
    suspend fun processRowsAndSave(
        database: AppDatabase,
        type: SheetType,
        rows: List<List<String>>
    ): ImportResult = withContext(Dispatchers.IO) {
        if (rows.size <= 1) {
            return@withContext ImportResult(type, 0, 0, 0, 0, "Planilha sem linhas de dados.")
        }

        val header = rows.first().map { it.trim().uppercase() }
        val dataRows = rows.drop(1)

        val headerMap = header.mapIndexed { index, name -> name to index }.toMap()

        fun getCol(row: List<String>, vararg candidates: String): String {
            for (c in candidates) {
                val idx = headerMap[c.uppercase()]
                if (idx != null && idx < row.size) {
                    val v = row[idx].trim()
                    if (v.isNotEmpty()) return v
                }
            }
            return ""
        }

        val knownTags = mutableSetOf<String>()
        var successCount = 0
        var errorCount = 0

        when (type) {
            SheetType.PREVENTIVAS -> {
                val preventives = mutableListOf<PreventiveEntity>()
                for (row in dataRows) {
                    val tag = getCol(row, "EQUIPAMENTO", "TAG", "EQUIPAMENTO - 1")
                    val os = getCol(row, "OS", "NUMERO OS", "ORDEM")
                    val osDesc = getCol(row, "OS - DESCRIÇÃO", "DESCRIÇÃO DA OS", "OS_DESCRICAO")
                    val date = getCol(row, "DATA DE PLANEJ.", "DATA DE PLANEJ", "DATA PLANEJAMENTO")
                    val servDesc = getCol(row, "SERVIÇO - DESCRIÇÃO", "SERVIÇO", "SERVICO")
                    val local = getCol(row, "LOCAL", "SETOR")

                    if (tag.isNotEmpty()) {
                        knownTags.add(tag)
                        ensureEquipmentExists(database, tag, local)
                        preventives.add(
                            PreventiveEntity(
                                equipmentTag = tag,
                                osNumber = if (os.isNotEmpty()) os else "OS-${System.currentTimeMillis() % 10000}",
                                osDescription = if (osDesc.isNotEmpty()) osDesc else "Preventiva programada",
                                plannedDate = if (date.isNotEmpty()) date else "2026-10-15",
                                serviceDescription = if (servDesc.isNotEmpty()) servDesc else "Inspeção preventiva",
                                location = if (local.isNotEmpty()) local else "Setor Geral"
                            )
                        )
                        successCount++
                    } else {
                        errorCount++
                    }
                }
                if (preventives.isNotEmpty()) {
                    database.preventiveDao().insertPreventives(preventives)
                }
            }

            SheetType.BACKLOGS -> {
                val backlogs = mutableListOf<BacklogEntity>()
                for (row in dataRows) {
                    val tag = getCol(row, "EQUIPAMENTO", "TAG")
                    val os = getCol(row, "OS", "NUMERO OS")
                    val statusOs = getCol(row, "STATUS DA OS", "STATUS")
                    val farol = getCol(row, "FAROL", "PRIORIDADE")
                    val desc = getCol(row, "DESCRIÇÃO", "DESCRICAO")
                    val notas = getCol(row, "NOTAS")
                    val obs = getCol(row, "OBSERVAÇÃO", "OBSERVACAO")
                    val comment = getCol(row, "COMENTARIO", "COMENTÁRIO")
                    val dtCad = getCol(row, "DATA/HORA DE CAD.", "DATA CADASTRO", "DATA")

                    if (tag.isNotEmpty()) {
                        knownTags.add(tag)
                        ensureEquipmentExists(database, tag, "")

                        // REGRA: Identificar pendências e alertar status crítico
                        // (ex: "LEVANT. - AG. AÇÃO DO PLANEJ." ou farol "atrasadas")
                        val isCritical = statusOs.contains("LEVANT. - AG. AÇÃO DO PLANEJ.", ignoreCase = true) ||
                                farol.contains("atrasada", ignoreCase = true) ||
                                statusOs.contains("CRÍTICA", ignoreCase = true) ||
                                desc.contains("freio", ignoreCase = true) ||
                                desc.contains("sapata", ignoreCase = true)

                        val criticality = if (isCritical) "CRÍTICA" else if (farol.contains("média", ignoreCase = true)) "MÉDIA" else "BAIXA"

                        val action = when {
                            statusOs.contains("LEVANT. - AG. AÇÃO DO PLANEJ.", ignoreCase = true) -> "AG. AÇÃO DO PLANEJ."
                            statusOs.contains("TRÂNSITO", ignoreCase = true) -> "EM TRÂNSITO"
                            statusOs.contains("PARADA", ignoreCase = true) -> "AG. PARADA"
                            statusOs.contains("LIBERAD", ignoreCase = true) -> "LIBERADO P/ EXECUÇÃO"
                            isCritical -> "AG. AÇÃO DO PLANEJ."
                            else -> "AG. PEÇA"
                        }

                        backlogs.add(
                            BacklogEntity(
                                equipmentTag = tag,
                                osNumber = if (os.isNotEmpty()) os else "BK-${System.currentTimeMillis() % 10000}",
                                osStatus = if (statusOs.isNotEmpty()) statusOs else "ABERTA",
                                farol = if (farol.isNotEmpty()) farol else if (isCritical) "atrasadas" else "no prazo",
                                description = if (desc.isNotEmpty()) desc else "Manutenção corretiva registrada",
                                notes = notas.ifEmpty { null },
                                observation = obs.ifEmpty { null },
                                comment = comment.ifEmpty { null },
                                createdAt = if (dtCad.isNotEmpty()) dtCad else "Hoje",
                                criticality = criticality,
                                isCritical = isCritical,
                                statusAction = action
                            )
                        )
                        successCount++
                    } else {
                        errorCount++
                    }
                }
                if (backlogs.isNotEmpty()) {
                    database.backlogDao().insertBacklogs(backlogs)
                }
            }

            SheetType.HISTORICO -> {
                val histories = mutableListOf<HistoryEntity>()
                for (row in dataRows) {
                    val tag = getCol(row, "EQUIPAMENTO - 1", "EQUIPAMENTO", "TAG")
                    val os = getCol(row, "OS", "NUMERO OS")
                    val osDesc = getCol(row, "OS - DESCRIÇÃO", "DESCRIÇÃO")
                    val status = getCol(row, "STATUS DA OS", "STATUS")
                    val closed = getCol(row, "DATA DE ENCERRAMENTO - DATA", "DATA DE ENCERRAMENTO", "DATA FECHAMENTO")
                    val plan = getCol(row, "DATA DE PLANEJ.", "DATA DE PLANEJ")
                    val serv = getCol(row, "SERVIÇO - DESCRIÇÃO", "SERVIÇO")

                    if (tag.isNotEmpty()) {
                        knownTags.add(tag)
                        ensureEquipmentExists(database, tag, "")
                        histories.add(
                            HistoryEntity(
                                equipmentTag = tag,
                                osNumber = if (os.isNotEmpty()) os else "OS-${System.currentTimeMillis() % 10000}",
                                osDescription = if (osDesc.isNotEmpty()) osDesc else "Histórico de intervenção técnica",
                                osStatus = if (status.isNotEmpty()) status else "ENCERRADA",
                                closedDate = if (closed.isNotEmpty()) closed else "2026-08-15",
                                plannedDate = if (plan.isNotEmpty()) plan else "2026-08-10",
                                serviceDescription = if (serv.isNotEmpty()) serv else "Serviço realizado",
                                badgeType = if (status.contains("APROV", ignoreCase = true)) "APROVADO" else "CONCLUÍDO"
                            )
                        )
                        successCount++
                    } else {
                        errorCount++
                    }
                }
                if (histories.isNotEmpty()) {
                    database.historyDao().insertHistories(histories)
                }
            }

            SheetType.UNKNOWN -> {
                errorCount = dataRows.size
            }
        }

        ImportResult(
            type = type,
            totalProcessed = dataRows.size,
            successCount = successCount,
            errorCount = errorCount,
            validatedTagsCount = knownTags.size,
            message = "Sincronizados $successCount registros para ${knownTags.size} equipamentos TAG."
        )
    }

    private suspend fun ensureEquipmentExists(database: AppDatabase, tag: String, location: String) {
        val existing = database.equipmentDao().getEquipmentByTagSync(tag)
        if (existing == null) {
            val newEquipment = EquipmentEntity(
                tag = tag,
                name = "Ponte Rolante $tag",
                model = "CAT-DEMAG Standard",
                location = location.ifEmpty { "Setor Operacional CAT" },
                sector = "Manutenção Geral",
                capacity = "25 TON",
                spanElevation = "24.0m / 14m",
                femGroup = "A5 / M5",
                status = "EM OPERAÇÃO",
                totalCorrectiveCount = 0
            )
            database.equipmentDao().insertEquipments(listOf(newEquipment))
        }
    }

    /**
     * Leitor de CSV / TSV rápido com suporte a separadores vírgula, ponto e vírgula e tabulação
     */
    fun parseCsvStream(inputStream: InputStream): List<List<String>> {
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
        val rows = mutableListOf<List<String>>()

        var line: String? = reader.readLine()
        var delimiter = ";"
        if (line != null) {
            // Auto detect delimiter
            val semicolonCount = line.count { it == ';' }
            val commaCount = line.count { it == ',' }
            val tabCount = line.count { it == '\t' }
            delimiter = when {
                semicolonCount >= commaCount && semicolonCount >= tabCount -> ";"
                commaCount > semicolonCount && commaCount >= tabCount -> ","
                tabCount > semicolonCount && tabCount > commaCount -> "\t"
                else -> ";"
            }
            rows.add(parseCsvLine(line, delimiter))
        }

        while (true) {
            line = reader.readLine() ?: break
            if (line.isNotBlank()) {
                rows.add(parseCsvLine(line, delimiter))
            }
        }
        return rows
    }

    private fun parseCsvLine(line: String, delimiter: String): List<String> {
        val tokens = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false

        for (i in line.indices) {
            val ch = line[i]
            if (ch == '\"') {
                inQuotes = !inQuotes
            } else if (!inQuotes && line.startsWith(delimiter, i)) {
                tokens.add(sb.toString().trim().removeSurrounding("\""))
                sb.clear()
            } else {
                sb.append(ch)
            }
        }
        tokens.add(sb.toString().trim().removeSurrounding("\""))
        return tokens
    }

    /**
     * Leitor ultraleve de planilhas XLSX (.xlsx = zip contendo sheet1.xml e sharedStrings.xml)
     * Não necessita de JARs pesados e funciona 100% nativo no Android.
     */
    fun parseXlsxStream(inputStream: InputStream): List<List<String>> {
        val sharedStrings = mutableListOf<String>()
        val rawSheetXml = mutableListOf<Byte>()

        ZipInputStream(inputStream).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (entry.name.equals("xl/sharedStrings.xml", ignoreCase = true)) {
                    val factory = XmlPullParserFactory.newInstance()
                    val parser = factory.newPullParser()
                    parser.setInput(zis, "UTF-8")
                    var eventType = parser.eventType
                    var currentText: StringBuilder? = null

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG && parser.name == "t") {
                            currentText = StringBuilder()
                        } else if (eventType == XmlPullParser.TEXT && currentText != null) {
                            currentText.append(parser.text)
                        } else if (eventType == XmlPullParser.END_TAG && parser.name == "t") {
                            sharedStrings.add(currentText?.toString() ?: "")
                            currentText = null
                        }
                        eventType = parser.next()
                    }
                } else if (entry.name.contains("sheet1.xml", ignoreCase = true)) {
                    val buffer = ByteArray(4096)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        for (i in 0 until len) {
                            rawSheetXml.add(buffer[i])
                        }
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }

        if (rawSheetXml.isEmpty()) {
            return emptyList()
        }

        // Parse sheet1.xml
        val rows = mutableListOf<List<String>>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(rawSheetXml.toByteArray().inputStream(), "UTF-8")

        var eventType = parser.eventType
        var currentRow: MutableList<String>? = null
        var currentCellType = ""
        var cellValue: StringBuilder? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "row" -> currentRow = mutableListOf()
                        "c" -> {
                            currentCellType = parser.getAttributeValue(null, "t") ?: ""
                            cellValue = null
                        }
                        "v" -> cellValue = StringBuilder()
                    }
                }
                XmlPullParser.TEXT -> {
                    cellValue?.append(parser.text)
                }
                XmlPullParser.END_TAG -> {
                    when (parser.name) {
                        "c" -> {
                            val rawVal = cellValue?.toString()?.trim() ?: ""
                            val resolvedVal = if (currentCellType == "s") {
                                val idx = rawVal.toIntOrNull()
                                if (idx != null && idx in sharedStrings.indices) {
                                    sharedStrings[idx]
                                } else rawVal
                            } else rawVal
                            currentRow?.add(resolvedVal)
                            cellValue = null
                        }
                        "row" -> {
                            currentRow?.let { if (it.isNotEmpty()) rows.add(it) }
                            currentRow = null
                        }
                    }
                }
            }
            eventType = parser.next()
        }

        return rows
    }
}
