package com.example

import com.example.data.parser.SheetType
import com.example.data.parser.SpreadsheetParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream

class ExampleUnitTest {
    @Test
    fun testDetectSheetType() {
        val preventivasHeader = listOf("Equipamento", "OS", "OS - Descrição", "Data de Planej.", "Serviço - Descrição", "Local")
        val backlogsHeader = listOf("EQUIPAMENTO", "OS", "STATUS DA OS", "FAROL", "DESCRIÇÃO", "NOTAS", "OBSERVAÇÃO", "COMENTARIO", "DATA/HORA DE CAD.")
        val historicoHeader = listOf("Equipamento - 1", "OS", "OS - Descrição", "Status da OS", "Data de Encerramento - Data", "Data de Planej.", "Serviço - Descrição")

        assertEquals(SheetType.PREVENTIVAS, SpreadsheetParser.detectSheetType(preventivasHeader))
        assertEquals(SheetType.BACKLOGS, SpreadsheetParser.detectSheetType(backlogsHeader))
        assertEquals(SheetType.HISTORICO, SpreadsheetParser.detectSheetType(historicoHeader))
    }

    @Test
    fun testParseCsvStream() {
        val csvData = """
            EQUIPAMENTO;OS;STATUS DA OS;FAROL;DESCRIÇÃO;NOTAS;OBSERVAÇÃO;COMENTARIO;DATA/HORA DE CAD.
            28HX000246;BK-2026-084;LEVANT. - AG. AÇÃO DO PLANEJ.;atrasadas;Desgaste sapatas;Requer troca;Ag almoxarifado;;18/10/2026
        """.trimIndent()

        val parsed = SpreadsheetParser.parseCsvStream(ByteArrayInputStream(csvData.toByteArray()))
        assertEquals(2, parsed.size)
        assertEquals("28HX000246", parsed[1][0])
        assertEquals("BK-2026-084", parsed[1][1])
        assertTrue(parsed[1][2].contains("LEVANT. - AG. AÇÃO DO PLANEJ."))
    }
}
