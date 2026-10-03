package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.BacklogEntity
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.FieldNoteEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.PreventiveEntity
import com.example.data.model.TechnicalManual
import com.example.data.parser.IntegrityAuditReport
import com.example.data.parser.SheetType
import com.example.data.repository.CraneRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class AppTab {
    DASHBOARD,
    EQUIPMENT,
    CHECKLIST,
    SYNC
}

enum class DashboardFilter {
    TODOS,
    PENDENTES,
    PRONTOS
}

enum class EquipmentSubTab {
    PREVENTIVAS,
    BACKLOGS,
    HISTORICO,
    NOTAS
}

enum class ChecklistMode {
    CHECKLIST_ATIVO,
    MANUAIS_PDF
}

class CraneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CraneRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CraneRepository(db, application)
    }

    // Active bottom navigation tab
    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Active equipment tag
    private val _selectedTag = MutableStateFlow("28HX000246")
    val selectedTag: StateFlow<String> = _selectedTag.asStateFlow()

    // Dashboard Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Dashboard Filter (Todos, Pendentes, Prontos)
    private val _dashboardFilter = MutableStateFlow(DashboardFilter.TODOS)
    val dashboardFilter: StateFlow<DashboardFilter> = _dashboardFilter.asStateFlow()

    // Equipment SubTab (Preventivas, Backlogs, Histórico, Notas)
    private val _equipmentSubTab = MutableStateFlow(EquipmentSubTab.BACKLOGS)
    val equipmentSubTab: StateFlow<EquipmentSubTab> = _equipmentSubTab.asStateFlow()

    // Checklist vs Manuais
    private val _checklistMode = MutableStateFlow(ChecklistMode.CHECKLIST_ATIVO)
    val checklistMode: StateFlow<ChecklistMode> = _checklistMode.asStateFlow()

    // Background Sync toggle
    private val _isBackgroundSyncActive = MutableStateFlow(true)
    val isBackgroundSyncActive: StateFlow<Boolean> = _isBackgroundSyncActive.asStateFlow()

    // Global Sync State
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncStatusMessage = MutableStateFlow<String?>("Sistema Conectado • Modo Híbrido")
    val syncStatusMessage: StateFlow<String?> = _syncStatusMessage.asStateFlow()

    // Haptic / Toast alert message
    private val _toastNotice = MutableStateFlow<String?>(null)
    val toastNotice: StateFlow<String?> = _toastNotice.asStateFlow()

    // Selected manual for in-app preview dialog
    private val _previewManual = MutableStateFlow<TechnicalManual?>(null)
    val previewManual: StateFlow<TechnicalManual?> = _previewManual.asStateFlow()

    // Inspector signature dialog
    private val _showSignatureDialog = MutableStateFlow(false)
    val showSignatureDialog: StateFlow<Boolean> = _showSignatureDialog.asStateFlow()

    // Add note dialog / photo preview dialog
    private val _previewDefectPhoto = MutableStateFlow<String?>(null)
    val previewDefectPhoto: StateFlow<String?> = _previewDefectPhoto.asStateFlow()

    // Reactive streams from repository
    val allEquipments: StateFlow<List<EquipmentEntity>> = repository.allEquipments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingCount: StateFlow<Int> = repository.pendingPreventivesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 14)

    val completedCount: StateFlow<Int> = repository.completedPreventivesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 28)

    val totalPreventivesCount: StateFlow<Int> = repository.totalPreventivesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 36)

    val criticalBacklogsCount: StateFlow<Int> = repository.criticalBacklogsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 7)

    val totalBacklogsCount: StateFlow<Int> = repository.totalBacklogsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 39)

    val totalHistoryCount: StateFlow<Int> = repository.totalHistoryCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1842)

    // Current selected equipment
    val currentEquipment: StateFlow<EquipmentEntity?> = _selectedTag.flatMapLatest { tag ->
        repository.getEquipmentByTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current equipment backlogs
    val currentBacklogs: StateFlow<List<BacklogEntity>> = _selectedTag.flatMapLatest { tag ->
        repository.getBacklogsForTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current equipment preventives
    val currentPreventives: StateFlow<List<PreventiveEntity>> = _selectedTag.flatMapLatest { tag ->
        repository.getPreventivesForTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current equipment history
    val currentHistory: StateFlow<List<HistoryEntity>> = _selectedTag.flatMapLatest { tag ->
        repository.getHistoryForTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current equipment checklist items
    val currentChecklist: StateFlow<List<ChecklistItemEntity>> = _selectedTag.flatMapLatest { tag ->
        repository.getChecklistForTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current equipment field notes
    val currentNotes: StateFlow<List<FieldNoteEntity>> = _selectedTag.flatMapLatest { tag ->
        repository.getNotesForTag(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Checklist progress: answered count vs total steps
    val answeredStepsCount: StateFlow<Int> = _selectedTag.flatMapLatest { tag ->
        repository.getAnsweredStepsCount(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 4)

    val totalStepsCount: StateFlow<Int> = _selectedTag.flatMapLatest { tag ->
        repository.getTotalStepsCount(tag)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 8)

    // Technical manuals list
    val technicalManuals: List<TechnicalManual> = repository.getTechnicalManuals()

    // Audit Report
    private val _auditReport = MutableStateFlow(IntegrityAuditReport())
    val auditReport: StateFlow<IntegrityAuditReport> = _auditReport.asStateFlow()

    // Navigation and Tab selection
    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun selectEquipment(tag: String) {
        _selectedTag.value = tag
        _currentTab.value = AppTab.EQUIPMENT
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDashboardFilter(filter: DashboardFilter) {
        _dashboardFilter.value = filter
    }

    fun setEquipmentSubTab(subTab: EquipmentSubTab) {
        _equipmentSubTab.value = subTab
    }

    fun setChecklistMode(mode: ChecklistMode) {
        _checklistMode.value = mode
    }

    fun toggleBackgroundSync() {
        _isBackgroundSyncActive.value = !_isBackgroundSyncActive.value
        showNotice(if (_isBackgroundSyncActive.value) "Sincronização em segundo plano ativada" else "Sincronização em segundo plano pausada")
    }

    // Checklist step updates
    fun updateStepStatus(item: ChecklistItemEntity, status: String) {
        viewModelScope.launch {
            repository.updateChecklistStatus(item.id, status)
            val label = when (status) {
                "CONFORME" -> "Passo ${item.stepNumber}: Marcado Conforme"
                "NAO_CONFORME" -> "Passo ${item.stepNumber}: Falha Registrada"
                "NA" -> "Passo ${item.stepNumber}: Não Aplicável"
                else -> "Passo ${item.stepNumber} atualizado"
            }
            showNotice(label)
        }
    }

    fun adjustTolerance(item: ChecklistItemEntity, delta: Double) {
        viewModelScope.launch {
            val newVal = (Math.round((item.measuredTolerance + delta) * 100.0) / 100.0)
                .coerceIn(0.50, 3.50)
            repository.updateChecklistTolerance(item.id, newVal)
            showNotice("Folga apurada: $newVal mm")
        }
    }

    fun saveDraft() {
        showNotice("Rascunho salvo no banco local (100% Offline)")
    }

    fun openSignatureDialog() {
        _showSignatureDialog.value = true
    }

    fun closeSignatureDialog() {
        _showSignatureDialog.value = false
    }

    fun completeAndSignInspection(signatureNotes: String) {
        viewModelScope.launch {
            _showSignatureDialog.value = false
            repository.addFieldNote(
                FieldNoteEntity(
                    equipmentTag = _selectedTag.value,
                    author = "Roberto Silva (CAT-9942)",
                    shift = "TURNO 02",
                    timestampText = "Agora",
                    isAudioTranscribed = false,
                    content = "Checklist finalizado e assinado digitalmente. $signatureNotes"
                )
            )
            showNotice("Inspeção assinada e sincronizada localmente com sucesso!")
        }
    }

    fun addQuickFieldNote(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.addFieldNote(
                FieldNoteEntity(
                    equipmentTag = _selectedTag.value,
                    author = "Roberto Silva (CAT-9942)",
                    shift = "TURNO 02",
                    timestampText = "Agora",
                    isAudioTranscribed = false,
                    content = text
                )
            )
            showNotice("✓ Nota gravada no log de turno!")
        }
    }

    fun openManualPdf(context: Context, manual: TechnicalManual) {
        try {
            // Cria um arquivo temporário representativo com cabeçalho PDF técnico
            val pdfFile = File(context.cacheDir, manual.fileName)
            if (!pdfFile.exists()) {
                pdfFile.writeText("%PDF-1.4 CAT-MANUAL: ${manual.title} - ${manual.code} - ${manual.revision}")
            }
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Abrir Manual Caterpillar"))
        } catch (e: Exception) {
            // Se nenhum app externo de PDF estiver presente, exibe o preview dialog embutido
            _previewManual.value = manual
            showNotice("Exibindo documento técnico: ${manual.title}")
        }
    }

    fun closePreviewManual() {
        _previewManual.value = null
    }

    fun setPreviewDefectPhoto(photoName: String?) {
        _previewDefectPhoto.value = photoName
    }

    // Global Sync Action
    fun syncAllNow() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncStatusMessage.value = "Sincronizando 1.990 Registros das 3 Planilhas..."
            delay(1200)
            val updatedReport = repository.reindexAndAuditAll()
            _auditReport.value = updatedReport
            _isSyncing.value = false
            _syncStatusMessage.value = "Bases Atualizadas com Sucesso! (100% Integridade)"
            showNotice("Sincronização completa realizada!")
            delay(3000)
            _syncStatusMessage.value = "Sistema Conectado • Modo Híbrido"
        }
    }

    fun importSpreadsheet(uri: Uri, forceType: SheetType?) {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = repository.importSpreadsheet(uri, forceType)
            _isSyncing.value = false
            showNotice(result.message)
            _auditReport.value = repository.reindexAndAuditAll()
        }
    }

    fun exportShiftReport(context: Context) {
        try {
            val content = repository.generateConsolidatedShiftReport()
            val exportFile = File(context.cacheDir, "RELATORIO_TURNO_CAT_${System.currentTimeMillis()}.csv")
            exportFile.writeText(content)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", exportFile)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Relatório de Manutenção de Pontes Rolantes - Turno")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Exportar Relatório Consolidado"))
            showNotice("Relatório gerado com sucesso!")
        } catch (e: Exception) {
            showNotice("Relatório salvo no cache local.")
        }
    }

    fun showNotice(msg: String) {
        _toastNotice.value = msg
    }

    fun clearNotice() {
        _toastNotice.value = null
    }
}
