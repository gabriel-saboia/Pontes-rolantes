package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipment_table")
data class EquipmentEntity(
    @PrimaryKey
    val tag: String,
    val name: String,
    val model: String,
    val location: String,
    val sector: String,
    val capacity: String,
    val spanElevation: String,
    val femGroup: String,
    val status: String = "EM OPERAÇÃO / CRÍTICO P1",
    val totalCorrectiveCount: Int = 0,
    val mainFaultDescription: String? = null,
    val faultCriticality: String? = "ALTA CRITICIDADE"
)

/**
 * Planilha a) Preventivas do Mês
 * Colunas: 'Equipamento', 'OS', 'OS - Descrição', 'Data de Planej.', 'Serviço - Descrição', 'Local'
 */
@Entity(tableName = "preventives_table")
data class PreventiveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentTag: String,
    val osNumber: String,
    val osDescription: String,
    val plannedDate: String,
    val serviceDescription: String,
    val location: String,
    val isCompleted: Boolean = false,
    val completionDate: String? = null
)

/**
 * Planilha b) Backlogs e Reparos Ativos (OS'S)
 * Colunas: 'EQUIPAMENTO', 'OS', 'STATUS DA OS', 'FAROL', 'DESCRIÇÃO', 'NOTAS', 'OBSERVAÇÃO', 'COMENTARIO', 'DATA/HORA DE CAD.'
 * Regra: Identificar pendências e alertar status crítico (ex: "LEVANT. - AG. AÇÃO DO PLANEJ." ou farol "atrasadas").
 */
@Entity(tableName = "backlogs_table")
data class BacklogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentTag: String,
    val osNumber: String,
    val osStatus: String,
    val farol: String,
    val description: String,
    val notes: String? = null,
    val observation: String? = null,
    val comment: String? = null,
    val createdAt: String,
    val criticality: String = "MÉDIA", // CRÍTICA, MÉDIA, BAIXA
    val isCritical: Boolean = false,
    val statusAction: String = "AG. AÇÃO DO PLANEJ.",
    val partNumber: String? = null,
    val technicianName: String? = "Carlos Mendonça (Téc. Sênior)"
)

/**
 * Planilha c) Histórico Completo (todas_OS_desde_2022)
 * Colunas: 'Equipamento - 1', 'OS', 'OS - Descrição', 'Status da OS', 'Data de Encerramento - Data', 'Data de Planej.', 'Serviço - Descrição'
 */
@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentTag: String,
    val osNumber: String,
    val osDescription: String,
    val osStatus: String,
    val closedDate: String,
    val plannedDate: String,
    val serviceDescription: String,
    val badgeType: String = "APROVADO" // APROVADO, CONCLUÍDO, ANUAL
)

/**
 * Checklist de Manutenção Preventiva do Equipamento
 */
@Entity(tableName = "checklist_items_table")
data class ChecklistItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentTag: String,
    val stepNumber: Int,
    val title: String,
    val categoryBadge: String = "CRÍTICO", // CRÍTICO, FALHA, TOLERÂNCIA, EM ABERTO
    val description: String,
    val status: String = "PENDENTE", // CONFORME, NAO_CONFORME, NA, PENDENTE
    val hasTolerance: Boolean = false,
    val measuredTolerance: Double = 1.45,
    val minSafeTolerance: Double = 1.20,
    val maxSafeTolerance: Double = 1.80,
    val nonConformanceNote: String? = null,
    val photoCount: Int = 0,
    val photoFileName: String? = null
)

/**
 * Notas de Campo do Turno (Transcrições e Apontamentos)
 */
@Entity(tableName = "field_notes_table")
data class FieldNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val equipmentTag: String,
    val author: String,
    val shift: String = "TURNO 02",
    val timestampText: String = "Agora",
    val isAudioTranscribed: Boolean = false,
    val content: String
)

/**
 * Modelo de Manual Técnico em PDF para abertura via Intent
 */
data class TechnicalManual(
    val id: String,
    val equipmentModel: String,
    val title: String,
    val fileSize: String,
    val badge: String,
    val revision: String,
    val code: String,
    val fileName: String
)
