package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BacklogDao
import com.example.data.dao.ChecklistDao
import com.example.data.dao.EquipmentDao
import com.example.data.dao.FieldNoteDao
import com.example.data.dao.HistoryDao
import com.example.data.dao.PreventiveDao
import com.example.data.model.BacklogEntity
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.FieldNoteEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.PreventiveEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        EquipmentEntity::class,
        PreventiveEntity::class,
        BacklogEntity::class,
        HistoryEntity::class,
        ChecklistItemEntity::class,
        FieldNoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun equipmentDao(): EquipmentDao
    abstract fun preventiveDao(): PreventiveDao
    abstract fun backlogDao(): BacklogDao
    abstract fun historyDao(): HistoryDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun fieldNoteDao(): FieldNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cat_cranes_maintenance.db"
                )
                    .addCallback(DatabaseSeederCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseSeederCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    seedInitialData(database)
                }
            }
        }
    }
}

suspend fun seedInitialData(database: AppDatabase) {
    // 1. Equipamentos base Caterpillar
    val equipments = listOf(
        EquipmentEntity(
            tag = "28HX000246",
            name = "Ponte Rolante Biviga CAT-DEMAG Heavy Duty 50T/10T",
            model = "CAT-DEMAG DH 50T-H12",
            location = "Setor 28PI-B • Linha de Montagem de Chassis",
            sector = "Chassis & Montagem Final",
            capacity = "50 / 10 TON",
            spanElevation = "28.5m / 16m",
            femGroup = "A6 / M6",
            status = "EM OPERAÇÃO / CRÍTICO P1",
            totalCorrectiveCount = 11,
            mainFaultDescription = "Desgaste prematuro de freio de translação",
            faultCriticality = "ALTA CRITICIDADE"
        ),
        EquipmentEntity(
            tag = "28HX000189",
            name = "Guindaste Giratório 10T",
            model = "CAT-SWIVEL G-10T",
            location = "Setor 14-A • Forjaria & Laminação",
            sector = "Forjaria & Laminação",
            capacity = "10 TON",
            spanElevation = "12.0m / 8m",
            femGroup = "A5 / M5",
            status = "EM OPERAÇÃO",
            totalCorrectiveCount = 8,
            mainFaultDescription = "Folga excessiva em rolamento axial",
            faultCriticality = "ALTA CRITICIDADE"
        ),
        EquipmentEntity(
            tag = "28HX000312",
            name = "Ponte Pórtico Biviga 30T",
            model = "CAT-GANTRY BG-30",
            location = "Pátio Externo • Armazém de Matéria-Prima",
            sector = "Forjaria & Laminação",
            capacity = "30 TON",
            spanElevation = "32.0m / 14m",
            femGroup = "A6 / M6",
            status = "EM OPERAÇÃO",
            totalCorrectiveCount = 6,
            mainFaultDescription = "Sobreaquecimento contator Schneider",
            faultCriticality = "MÉDIA CRITICIDADE"
        ),
        EquipmentEntity(
            tag = "28HX000405",
            name = "Ponte 20T - Usinagem",
            model = "CAT-BRIDGE HD-20",
            location = "Setor 09-C • Usinagem Pesada",
            sector = "Usinagem Pesada",
            capacity = "20 TON",
            spanElevation = "24.0m / 12m",
            femGroup = "A5 / M5",
            status = "EM OPERAÇÃO",
            totalCorrectiveCount = 4,
            mainFaultDescription = "Trinca leve em batente fim curso; Cabo botoeira",
            faultCriticality = "MÉDIA CRITICIDADE"
        ),
        EquipmentEntity(
            tag = "28HX000114",
            name = "Monovia Talha 5T - Caldeiraria",
            model = "CAT-HOIST M-5T",
            location = "Setor 03-D • Baia de Solda & Caldeiraria",
            sector = "Baia de Solda & Caldeiraria",
            capacity = "5 TON",
            spanElevation = "18.0m / 6m",
            femGroup = "A4 / M4",
            status = "EM OPERAÇÃO",
            totalCorrectiveCount = 2,
            mainFaultDescription = "Ajuste de esteira porta-cabos festoon",
            faultCriticality = "BAIXA CRITICIDADE"
        )
    )
    database.equipmentDao().insertEquipments(equipments)

    // 2. Preventivas do Mês
    val preventives = listOf(
        PreventiveEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS-2026-9941",
            osDescription = "Inspeção Nível 2 • Cabos, Freios e Limitadores de Fim de Curso",
            plannedDate = "2026-10-03",
            serviceDescription = "Preventiva quinzenal e medição de folga",
            location = "Setor 28PI-B",
            isCompleted = false
        ),
        PreventiveEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS-2026-9915",
            osDescription = "Lubrificação geral de cabos de aço e redutor de giro",
            plannedDate = "2026-10-18",
            serviceDescription = "Lubrificação pesada preventiva",
            location = "Setor 28PI-B",
            isCompleted = false
        ),
        PreventiveEntity(
            equipmentTag = "28HX000189",
            osNumber = "OS-2026-9882",
            osDescription = "Verificação de torque de parafusos da coroa de rotação",
            plannedDate = "2026-10-04",
            serviceDescription = "Preventiva mecânica",
            location = "Setor 14-A",
            isCompleted = false
        ),
        PreventiveEntity(
            equipmentTag = "28HX000312",
            osNumber = "OS-2026-9810",
            osDescription = "Reaperto de barramento elétrico 440V e termografia",
            plannedDate = "2026-10-08",
            serviceDescription = "Preventiva elétrica / Preditiva",
            location = "Pátio Externo",
            isCompleted = true,
            completionDate = "2026-10-01"
        ),
        PreventiveEntity(
            equipmentTag = "28HX000405",
            osNumber = "OS-2026-9799",
            osDescription = "Calibração célula de carga de sobrepeso e freio de trole",
            plannedDate = "2026-10-12",
            serviceDescription = "Aferição metrológica",
            location = "Setor 09-C",
            isCompleted = true,
            completionDate = "2026-10-01"
        )
    )
    database.preventiveDao().insertPreventives(preventives)

    // 3. Backlogs e Reparos Ativos
    val backlogs = listOf(
        BacklogEntity(
            equipmentTag = "28HX000246",
            osNumber = "BK-2026-084",
            osStatus = "ABERTA",
            farol = "atrasadas",
            description = "Desgaste excessivo nas sapatas do freio de elevação principal (tolerância medida inferior a 2.5mm com micrômetro digital).",
            notes = "Requer substituição de sapatas e lona de atrito.",
            observation = "Aguardando envio pelo almoxarifado central de Piracicaba.",
            comment = "Prioridade máxima de segurança de içamento.",
            createdAt = "18/10/2026",
            criticality = "CRÍTICA",
            isCritical = true,
            statusAction = "AG. AÇÃO DO PLANEJ.",
            partNumber = "CAT-8921-X",
            technicianName = "Carlos Mendonça (Téc. Sênior)"
        ),
        BacklogEntity(
            equipmentTag = "28HX000246",
            osNumber = "BK-2026-061",
            osStatus = "EM ANDAMENTO",
            farol = "no prazo",
            description = "Vazamento leve no retentor de óleo da caixa redutora do trole. Coletado 120ml em bandeja de contenção temporária.",
            notes = "Guarnição NBR 14022 despachada.",
            observation = "Nível de óleo ainda acima do mínimo de segurança.",
            comment = "Previsão de chegada 26/10.",
            createdAt = "12/10/2026",
            criticality = "MÉDIA",
            isCritical = false,
            statusAction = "EM TRÂNSITO (ETA: 26/10)",
            partNumber = "NBR 14022",
            technicianName = "Carlos Mendonça (Téc. Sênior)"
        ),
        BacklogEntity(
            equipmentTag = "28HX000246",
            osNumber = "BK-2026-052",
            osStatus = "AGENDADA",
            farol = "no prazo",
            description = "Lâmpada strobo de sinalização visual intermitente durante translação rápida.",
            notes = "Módulo LED 24V sobressalente já em bancada.",
            observation = "Executar junto com a parada de manutenção programada.",
            comment = "Sem risco de parada de linha.",
            createdAt = "08/10/2026",
            criticality = "BAIXA",
            isCritical = false,
            statusAction = "AG. PARADA",
            partNumber = "CAT-STROB-24V",
            technicianName = "Manoel Costa"
        ),
        BacklogEntity(
            equipmentTag = "28HX000405",
            osNumber = "BK-2026-049",
            osStatus = "ABERTA",
            farol = "atrasadas",
            description = "Trinca leve em batente de borracha de fim de curso e desgaste na capa do cabo da botoeira.",
            createdAt = "05/10/2026",
            criticality = "MÉDIA",
            isCritical = false,
            statusAction = "AG. PARADA"
        ),
        BacklogEntity(
            equipmentTag = "28HX000114",
            osNumber = "BK-2026-033",
            osStatus = "LIBERADA",
            farol = "no prazo",
            description = "Ajuste e lubrificação de esteira porta-cabos festoon de alimentação da talha elétrica.",
            createdAt = "02/10/2026",
            criticality = "BAIXA",
            isCritical = false,
            statusAction = "LIBERADO P/ EXECUÇÃO"
        )
    )
    database.backlogDao().insertBacklogs(backlogs)

    // 4. Histórico Completo
    val histories = listOf(
        HistoryEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS 99401",
            osDescription = "Troca de cabos de aço e teste de carga estática 62.5T. Emissão de laudo ART 109923.",
            osStatus = "ENCERRADA",
            closedDate = "24/07/2026",
            plannedDate = "20/07/2026",
            serviceDescription = "Substituição completa de cabo de elevação e testes com dinamômetro de 100T.",
            badgeType = "APROVADO"
        ),
        HistoryEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS 84120",
            osDescription = "Reforma do motor de translação da ponte e substituição de contactores Schneider no painel de comando.",
            osStatus = "ENCERRADA",
            closedDate = "15/03/2025",
            plannedDate = "10/03/2025",
            serviceDescription = "Rebobinamento motor 15CV e troca de contatores LC1D80.",
            badgeType = "CONCLUÍDO"
        ),
        HistoryEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS 71092",
            osDescription = "Manutenção preventiva anual completa. Alinhamento a laser dos trilhos de rolamento estrutural.",
            osStatus = "ENCERRADA",
            closedDate = "10/11/2024",
            plannedDate = "05/11/2024",
            serviceDescription = "Geometria de vigas e nivelamento topográfico com teodolito a laser.",
            badgeType = "ANUAL"
        ),
        HistoryEntity(
            equipmentTag = "28HX000246",
            osNumber = "OS 60231",
            osDescription = "Revisão geral do tambor de enrolamento e troca de mancais bipartidos com rolamento autocompensador.",
            osStatus = "ENCERRADA",
            closedDate = "18/06/2023",
            plannedDate = "15/06/2023",
            serviceDescription = "Troca de rolamentos SKF 22220 e retentores.",
            badgeType = "CONCLUÍDO"
        )
    )
    database.historyDao().insertHistories(histories)

    // 5. Checklist Items para 28HX000246
    val checklistItems = listOf(
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 1,
            title = "Estrutura e Trilhos de Rolamento",
            categoryBadge = "CRÍTICO",
            description = "Verificar fixação de talas de junção, desgaste lateral das vigas de rolamento e batentes amortecedores de fim de curso.",
            status = "CONFORME",
            hasTolerance = false
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 2,
            title = "Cabos de Aço e Moitão de Carga",
            categoryBadge = "FALHA",
            description = "Inspeção visual de arames rompidos (máx. 6 em 6d), deformações tipo gaiola de pássaro ou corrosão ácida acentuada.",
            status = "NAO_CONFORME",
            hasTolerance = false,
            nonConformanceNote = "Detectado 1 arame rompido na espira superior do tambor leste. Classificado como desgaste grau 3.",
            photoCount = 2,
            photoFileName = "cabo_defeito_01.jpg"
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 3,
            title = "Sistema de Freios e Freio Eletromagnético",
            categoryBadge = "TOLERÂNCIA",
            description = "Medição de folga das sapatas (nominal: 1.2mm a 1.8mm) e teste de retenção dinâmica com carga nominal.",
            status = "CONFORME",
            hasTolerance = true,
            measuredTolerance = 1.45,
            minSafeTolerance = 1.20,
            maxSafeTolerance = 1.80
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 4,
            title = "Painel Elétrico e Botoeira / Rádio",
            categoryBadge = "EM ABERTO",
            description = "Estado dos contatores, parada de emergência tipo cogumelo e sincronismo de antena rádio CAT-Link.",
            status = "PENDENTE",
            hasTolerance = false
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 5,
            title = "Fim de Curso e Limitadores de Carga",
            categoryBadge = "CRÍTICO",
            description = "Teste prático de atuação das chaves fim de curso de elevação e translação em velocidade reduzida.",
            status = "PENDENTE",
            hasTolerance = false
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 6,
            title = "Lubrificação de Redutores e Mancais",
            categoryBadge = "PADRÃO",
            description = "Checagem do visor de nível de óleo sintético ISO VG 220 e engraxe de mancais com graxa lítio EP-2.",
            status = "PENDENTE",
            hasTolerance = false
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 7,
            title = "Barramento Blindado e Sapatas Coletoras",
            categoryBadge = "ELÉTRICA",
            description = "Desgaste de escovas de cobre-grafite, pressão das molas e alinhamento do canal condutor de cobre.",
            status = "PENDENTE",
            hasTolerance = false
        ),
        ChecklistItemEntity(
            equipmentTag = "28HX000246",
            stepNumber = 8,
            title = "Teste Operacional de Movimentação e Ruídos",
            categoryBadge = "FINAL",
            description = "Teste sem carga nos 3 eixos (elevação, trole e ponte) verificando suavidade e ausência de vibração.",
            status = "PENDENTE",
            hasTolerance = false
        )
    )
    database.checklistDao().insertChecklistItems(checklistItems)

    // 6. Notas de campo
    val notes = listOf(
        FieldNoteEntity(
            equipmentTag = "28HX000246",
            author = "Manoel Costa",
            shift = "TURNO 02",
            timestampText = "Há 38 min",
            isAudioTranscribed = true,
            content = "\"Ruído anômalo na descida sem carga verificado às 14:15. Monitorar vibração no rolamento axial antes da liberação do próximo lote.\""
        ),
        FieldNoteEntity(
            equipmentTag = "28HX000246",
            author = "Roberto Silva",
            shift = "TURNO 01",
            timestampText = "Ontem 22:10",
            isAudioTranscribed = false,
            content = "Substituído fusível de comando 4A do inversor de frequência após queda de fase momentânea na subestação SE-04."
        )
    )
    for (note in notes) {
        database.fieldNoteDao().insertNote(note)
    }
}
