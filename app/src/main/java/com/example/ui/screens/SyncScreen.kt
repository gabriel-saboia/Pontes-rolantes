package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.parser.IntegrityAuditReport
import com.example.data.parser.SheetType
import com.example.ui.theme.CatCriticalRed
import com.example.ui.theme.CatCriticalRedContainer
import com.example.ui.theme.CatDarkBackground
import com.example.ui.theme.CatOnCriticalRed
import com.example.ui.theme.CatOnYellow
import com.example.ui.theme.CatSuccessGreen
import com.example.ui.theme.CatSurfaceContainer
import com.example.ui.theme.CatSurfaceHigh
import com.example.ui.theme.CatSurfaceHighest
import com.example.ui.theme.CatSurfaceLow
import com.example.ui.theme.CatSurfaceLowest
import com.example.ui.theme.CatTextPrimary
import com.example.ui.theme.CatTextSecondary
import com.example.ui.theme.CatWarningAmber
import com.example.ui.theme.CatYellow
import com.example.ui.theme.CatYellowDim

@Composable
fun SyncScreen(
    isSyncing: Boolean,
    syncStatusMessage: String?,
    auditReport: IntegrityAuditReport,
    isBackgroundSyncActive: Boolean,
    onSyncAll: () -> Unit,
    onToggleBackgroundSync: () -> Unit,
    onImportFile: (Uri, SheetType) -> Unit,
    onExportReport: () -> Unit
) {
    // SAF File pickers
    val preventiveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { onImportFile(it, SheetType.PREVENTIVAS) }
    }

    val backlogLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { onImportFile(it, SheetType.BACKLOGS) }
    }

    val historyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { onImportFile(it, SheetType.HISTORICO) }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        ),
        label = "spin_angle"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CatDarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("sync_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Top Live Sync Status Banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CatSurfaceLow)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
            ) {
                // Hazard Accent Line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(CatYellow, CatYellowDim, CatYellow)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(CatYellow)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SISTEMA CONECTADO • MODO HÍBRIDO",
                                color = CatYellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CatSurfaceHigh)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "ONLINE / BACKUP ATIVO",
                                color = CatTextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = CatYellowDim,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Última sincronização: ${auditReport.lastSyncTime} por ${auditReport.technician}",
                            color = CatTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Master Action Button
                    Button(
                        onClick = onSyncAll,
                        enabled = !isSyncing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("btn_sync_all"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CatYellow,
                            contentColor = CatOnYellow,
                            disabledContainerColor = CatSurfaceHigh,
                            disabledContentColor = CatYellow
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(if (isSyncing) spinAngle else 0f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSyncing) "SINCRONIZANDO 1.990 REGISTROS..." else "SINCRONIZAR TUDO AGORA",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TableChart,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GESTÃO DE PLANILHAS & TELEMETRIA",
                        color = CatTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CatSurfaceContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "3 BASES ATIVAS",
                        color = CatTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // SPREADSHEET CARD 1: PREVENTIVAS
        item {
            SpreadsheetCard(
                category = "PLANO OPERACIONAL",
                title = "1. Preventivas do Mês Atual (Outubro/2026)",
                updatedTime = "Atualizado (13:45)",
                fileName = "PREV_OUTUBRO_2026_REV4.xlsx",
                icon = Icons.Default.Description,
                stat1 = "148 Itens Programados",
                stat2 = "42 Concluídos (28%)",
                buttonText = "CARREGAR NOVO ARQUIVO .XLSX / .CSV",
                buttonIcon = Icons.Default.UploadFile,
                onButtonClick = {
                    preventiveLauncher.launch(
                        arrayOf(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel",
                            "text/csv",
                            "text/plain",
                            "*/*"
                        )
                    )
                },
                testTag = "card_spreadsheet_preventivas"
            )
        }

        // SPREADSHEET CARD 2: LOG HISTÓRICO
        item {
            SpreadsheetCard(
                category = "BASE ANALÍTICA",
                title = "2. Log Histórico de Manutenção (2022+)",
                updatedTime = "Sincronizado (Ontem 18:20)",
                fileName = "HISTORICO_CRANES_CAT_2022_2026.xlsx",
                icon = Icons.Default.Storage,
                stat1 = "1.842 Ordens de Serviço (OS)",
                stat2 = "4.2 MB",
                buttonText = "ATUALIZAR BASE HISTÓRICA",
                buttonIcon = Icons.Default.Update,
                onButtonClick = {
                    historyLauncher.launch(
                        arrayOf(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel",
                            "text/csv",
                            "text/plain",
                            "*/*"
                        )
                    )
                },
                testTag = "card_spreadsheet_historico"
            )
        }

        // SPREADSHEET CARD 3: BACKLOGS & PEÇAS
        item {
            SpreadsheetCard(
                category = "ALOCAÇÃO CRÍTICA",
                title = "3. Backlogs Ativos & Pendências de Peças",
                updatedTime = "! 3 NOVOS LOCAIS",
                fileName = "BACKLOGS_ATIVOS_SETORES_28.xlsx",
                icon = Icons.Default.PendingActions,
                stat1 = "39 Pendências em Aberto",
                stat2 = "7 Críticas",
                buttonText = "IMPORTAR PLANILHA DE BACKLOGS",
                buttonIcon = Icons.Default.DriveFolderUpload,
                isCriticalCard = true,
                onButtonClick = {
                    backlogLauncher.launch(
                        arrayOf(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel",
                            "text/csv",
                            "text/plain",
                            "*/*"
                        )
                    )
                },
                testTag = "card_spreadsheet_backlogs"
            )
        }

        // Conflict & Data Validation Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CatSurfaceLowest)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RELATÓRIO DE INTEGRIDADE DOS DADOS",
                            color = CatTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CatSurfaceHigh)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AUDITORIA ${auditReport.auditPercentage}%",
                            color = CatYellow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IntegrityBullet(
                    icon = Icons.Default.CheckCircle,
                    iconTint = CatSuccessGreen,
                    boldText = "${auditReport.validatedTags} TAGs validados",
                    normalText = " com sucesso contra o catálogo mestre de ativos da unidade."
                )

                IntegrityBullet(
                    icon = Icons.Default.CheckCircle,
                    iconTint = CatSuccessGreen,
                    boldText = "${auditReport.formattingErrors} erros",
                    normalText = " de formatação de data, chassi ou duplicidade de número de OS identificados."
                )

                IntegrityBullet(
                    icon = Icons.Default.Info,
                    iconTint = CatYellow,
                    boldText = "${auditReport.offlinePending} pendências",
                    normalText = " registradas em modo offline aguardando validação da equipe de Planejamento."
                )
            }
        }

        // Connectivity & Sync Preferences Switch
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CatSurfaceContainer)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SINCRONIZAÇÃO EM SEGUNDO PLANO",
                        color = CatTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Wi-Fi Industrial Cat® / Conexão Celular 5G",
                        color = CatTextSecondary,
                        fontSize = 11.sp
                    )
                }

                // Glove-Friendly Tactile Switch
                Box(
                    modifier = Modifier
                        .width(64.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isBackgroundSyncActive) CatYellow else CatSurfaceHigh)
                        .clickable(onClick = onToggleBackgroundSync)
                        .padding(3.dp)
                        .testTag("switch_wifi_sync")
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CatSurfaceLowest)
                            .align(if (isBackgroundSyncActive) Alignment.CenterEnd else Alignment.CenterStart),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Wi-Fi",
                            tint = if (isBackgroundSyncActive) CatYellow else CatTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Shift Export Tactical Action Button
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onExportReport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_export_consolidated_report"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatSurfaceHigh,
                        contentColor = CatYellow
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EXPORTAR RELATÓRIO CONSOLIDADO DO TURNO (.XLSX)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = CatTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CANAL CRIPTOGRAFADO PONTA A PONTA • PADRÃO CAT-FMS",
                        color = CatTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SpreadsheetCard(
    category: String,
    title: String,
    updatedTime: String,
    fileName: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    stat1: String,
    stat2: String,
    buttonText: String,
    buttonIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onButtonClick: () -> Unit,
    isCriticalCard: Boolean = false,
    testTag: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CatSurfaceLow)
            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag(testTag),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    color = if (isCriticalCard) CatYellow else CatYellowDim,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    color = CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCriticalCard) CatSurfaceHighest else CatSurfaceContainer)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = updatedTime,
                    color = if (isCriticalCard) CatYellow else CatYellowDim,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Visual File Representation Pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(CatSurfaceHighest.copy(alpha = 0.6f))
                .border(1.dp, CatSurfaceContainer, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CatYellow,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = fileName,
                color = CatYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(CatSurfaceContainer)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stat1,
                color = CatTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isCriticalCard) CatCriticalRedContainer else CatSurfaceHigh)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stat2,
                    color = if (isCriticalCard) CatOnCriticalRed else CatYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Action Button
        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CatSurfaceHigh,
                contentColor = CatTextPrimary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = buttonIcon,
                    contentDescription = null,
                    tint = CatYellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buttonText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun IntegrityBullet(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    boldText: String,
    normalText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CatSurfaceLow)
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = boldText,
                color = CatYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = normalText,
                color = CatTextPrimary,
                fontSize = 12.sp
            )
        }
    }
}
