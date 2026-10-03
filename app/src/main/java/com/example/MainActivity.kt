package com.example

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.DefectPhotoPreviewDialog
import com.example.ui.components.IndustrialBottomBar
import com.example.ui.components.IndustrialTopBar
import com.example.ui.components.ManualPreviewDialog
import com.example.ui.components.SignatureDialog
import com.example.ui.screens.ChecklistScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EquipmentScreen
import com.example.ui.screens.SyncScreen
import com.example.ui.theme.CatDarkBackground
import com.example.ui.theme.CatSurfaceContainer
import com.example.ui.theme.CatSurfaceHighest
import com.example.ui.theme.CatTextPrimary
import com.example.ui.theme.CatYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.ChecklistMode
import com.example.ui.viewmodel.CraneViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CraneMaintenanceApp()
            }
        }
    }
}

@Composable
fun CraneMaintenanceApp(
    viewModel: CraneViewModel = viewModel()
) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val dashboardFilter by viewModel.dashboardFilter.collectAsStateWithLifecycle()
    val equipmentSubTab by viewModel.equipmentSubTab.collectAsStateWithLifecycle()
    val checklistMode by viewModel.checklistMode.collectAsStateWithLifecycle()

    val currentEquipment by viewModel.currentEquipment.collectAsStateWithLifecycle()
    val currentBacklogs by viewModel.currentBacklogs.collectAsStateWithLifecycle()
    val currentPreventives by viewModel.currentPreventives.collectAsStateWithLifecycle()
    val currentHistory by viewModel.currentHistory.collectAsStateWithLifecycle()
    val currentChecklist by viewModel.currentChecklist.collectAsStateWithLifecycle()
    val currentNotes by viewModel.currentNotes.collectAsStateWithLifecycle()

    val answeredStepsCount by viewModel.answeredStepsCount.collectAsStateWithLifecycle()
    val totalStepsCount by viewModel.totalStepsCount.collectAsStateWithLifecycle()

    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val completedCount by viewModel.completedCount.collectAsStateWithLifecycle()
    val totalCount by viewModel.totalPreventivesCount.collectAsStateWithLifecycle()

    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val syncStatusMessage by viewModel.syncStatusMessage.collectAsStateWithLifecycle()
    val auditReport by viewModel.auditReport.collectAsStateWithLifecycle()
    val isBackgroundSyncActive by viewModel.isBackgroundSyncActive.collectAsStateWithLifecycle()

    val toastNotice by viewModel.toastNotice.collectAsStateWithLifecycle()
    val previewManual by viewModel.previewManual.collectAsStateWithLifecycle()
    val showSignatureDialog by viewModel.showSignatureDialog.collectAsStateWithLifecycle()
    val previewDefectPhoto by viewModel.previewDefectPhoto.collectAsStateWithLifecycle()

    // Handle back button when in sub-screens
    BackHandler(enabled = currentTab != AppTab.DASHBOARD) {
        viewModel.selectTab(AppTab.DASHBOARD)
    }

    // Auto-dismiss toast notice after 2.5 seconds
    LaunchedEffect(toastNotice) {
        if (toastNotice != null) {
            triggerIndustrialHaptic(context)
            delay(2500)
            viewModel.clearNotice()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CatDarkBackground,
        topBar = {
            IndustrialTopBar(
                onMonthSelected = { month ->
                    viewModel.showNotice("Filtro operacional atualizado para $month")
                }
            )
        },
        bottomBar = {
            IndustrialBottomBar(
                currentTab = currentTab,
                onTabSelected = { tab ->
                    viewModel.selectTab(tab)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CatDarkBackground)
        ) {
            when (currentTab) {
                AppTab.DASHBOARD -> {
                    DashboardScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChange = viewModel::setSearchQuery,
                        selectedFilter = dashboardFilter,
                        onFilterChange = viewModel::setDashboardFilter,
                        pendingCount = pendingCount,
                        completedCount = completedCount,
                        totalCount = totalCount,
                        onSelectEquipment = { tag ->
                            viewModel.selectEquipment(tag)
                        }
                    )
                }

                AppTab.EQUIPMENT -> {
                    EquipmentScreen(
                        equipment = currentEquipment,
                        subTab = equipmentSubTab,
                        onSubTabSelected = viewModel::setEquipmentSubTab,
                        backlogs = currentBacklogs,
                        preventives = currentPreventives,
                        history = currentHistory,
                        notes = currentNotes,
                        onOpenChecklist = {
                            viewModel.setChecklistMode(ChecklistMode.CHECKLIST_ATIVO)
                            viewModel.selectTab(AppTab.CHECKLIST)
                        },
                        onOpenManuals = {
                            viewModel.setChecklistMode(ChecklistMode.MANUAIS_PDF)
                            viewModel.selectTab(AppTab.CHECKLIST)
                        },
                        onAddNote = { noteText ->
                            viewModel.addQuickFieldNote(noteText)
                        }
                    )
                }

                AppTab.CHECKLIST -> {
                    ChecklistScreen(
                        context = context,
                        mode = checklistMode,
                        onModeChange = viewModel::setChecklistMode,
                        checklistItems = currentChecklist,
                        answeredCount = answeredStepsCount,
                        totalSteps = totalStepsCount,
                        manuals = viewModel.technicalManuals,
                        onUpdateStatus = { item, status ->
                            viewModel.updateStepStatus(item, status)
                        },
                        onAdjustTolerance = { item, delta ->
                            viewModel.adjustTolerance(item, delta)
                        },
                        onSaveDraft = {
                            viewModel.saveDraft()
                        },
                        onFinalizeAndSign = {
                            viewModel.openSignatureDialog()
                        },
                        onOpenManual = { manual ->
                            viewModel.openManualPdf(context, manual)
                        },
                        onPreviewPhoto = { photoName ->
                            viewModel.setPreviewDefectPhoto(photoName)
                        }
                    )
                }

                AppTab.SYNC -> {
                    SyncScreen(
                        isSyncing = isSyncing,
                        syncStatusMessage = syncStatusMessage,
                        auditReport = auditReport,
                        isBackgroundSyncActive = isBackgroundSyncActive,
                        onSyncAll = {
                            viewModel.syncAllNow()
                        },
                        onToggleBackgroundSync = {
                            viewModel.toggleBackgroundSync()
                        },
                        onImportFile = { uri, sheetType ->
                            viewModel.importSpreadsheet(uri, sheetType)
                        },
                        onExportReport = {
                            viewModel.exportShiftReport(context)
                        }
                    )
                }
            }

            // Industrial Toast Overlay Notification
            AnimatedVisibility(
                visible = toastNotice != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceHighest)
                        .border(1.dp, CatYellow, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CatYellow)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = toastNotice ?: "",
                            color = CatTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Interactive Modals
            if (showSignatureDialog) {
                SignatureDialog(
                    onDismiss = { viewModel.closeSignatureDialog() },
                    onConfirm = { signatureNotes ->
                        viewModel.completeAndSignInspection(signatureNotes)
                    }
                )
            }

            previewManual?.let { manual ->
                ManualPreviewDialog(
                    manual = manual,
                    onDismiss = { viewModel.closePreviewManual() }
                )
            }

            previewDefectPhoto?.let { photoName ->
                DefectPhotoPreviewDialog(
                    photoName = photoName,
                    onDismiss = { viewModel.setPreviewDefectPhoto(null) }
                )
            }
        }
    }
}

/**
 * Haptic feedback para ambiente fabril
 */
fun triggerIndustrialHaptic(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            @Suppress("DEPRECATION")
            vibrator?.vibrate(45)
        }
    } catch (_: Exception) {
    }
}
