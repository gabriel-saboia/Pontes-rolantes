package com.example.ui.screens

import android.content.Context
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.TechnicalManual
import com.example.ui.components.MechanicalToleranceStepper
import com.example.ui.components.TriStateInspectionToggle
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
import com.example.ui.theme.CatYellow
import com.example.ui.viewmodel.ChecklistMode

@Composable
fun ChecklistScreen(
    context: Context,
    mode: ChecklistMode,
    onModeChange: (ChecklistMode) -> Unit,
    checklistItems: List<ChecklistItemEntity>,
    answeredCount: Int,
    totalSteps: Int,
    manuals: List<TechnicalManual>,
    onUpdateStatus: (ChecklistItemEntity, String) -> Unit,
    onAdjustTolerance: (ChecklistItemEntity, Double) -> Unit,
    onSaveDraft: () -> Unit,
    onFinalizeAndSign: () -> Unit,
    onOpenManual: (TechnicalManual) -> Unit,
    onPreviewPhoto: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatDarkBackground)
            .testTag("checklist_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Top Segmented Mode Switcher (CHECKLIST ATIVO vs MANUAIS PDF)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CatSurfaceLowest)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (mode == ChecklistMode.CHECKLIST_ATIVO) CatYellow else Color.Transparent)
                            .clickable { onModeChange(ChecklistMode.CHECKLIST_ATIVO) }
                            .testTag("tab_checklist_ativo"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FactCheck,
                                contentDescription = null,
                                tint = if (mode == ChecklistMode.CHECKLIST_ATIVO) CatOnYellow else CatTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CHECKLIST ATIVO",
                                color = if (mode == ChecklistMode.CHECKLIST_ATIVO) CatOnYellow else CatTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (mode == ChecklistMode.MANUAIS_PDF) CatYellow else Color.Transparent)
                            .clickable { onModeChange(ChecklistMode.MANUAIS_PDF) }
                            .testTag("tab_manuais_pdf"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = if (mode == ChecklistMode.MANUAIS_PDF) CatOnYellow else CatTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MANUAIS PDF",
                                color = if (mode == ChecklistMode.MANUAIS_PDF) CatOnYellow else CatTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(CatSurfaceHighest)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${manuals.size}",
                                    color = CatTextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (mode == ChecklistMode.CHECKLIST_ATIVO) {
                // Header machine info banner
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CatSurfaceLow)
                            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
                    ) {
                        // Hazard accent stripe
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .background(CatYellow)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CatSurfaceHighest)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "TAG: 28HX000246",
                                                color = CatYellow,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(CatSurfaceHigh)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "CRANE-50T",
                                                color = CatTextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "CHECKLIST PREVENTIVA • PONTE ROLANTE 50T",
                                        color = CatTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        lineHeight = 20.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CatSurfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudSync,
                                        contentDescription = "Sincronização",
                                        tint = CatYellow,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            // Inspector Credentials Bar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CatSurfaceLowest)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Engineering,
                                        contentDescription = null,
                                        tint = CatYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Roberto Silva • CAT-9942",
                                        color = CatTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CatSurfaceContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "TURNO A",
                                        color = CatTextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Progress Bar
                            val effectiveTotal = if (totalSteps > 0) totalSteps else 8
                            val percent = ((answeredCount.toFloat() / effectiveTotal) * 100).toInt()

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(CatYellow)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PROGRESSO DA INSPEÇÃO",
                                            color = CatTextPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "Passo $answeredCount de $effectiveTotal ($percent%)",
                                        color = CatYellow,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CatSurfaceHighest)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth((answeredCount.toFloat() / effectiveTotal).coerceIn(0f, 1f))
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(CatYellow)
                                    )
                                }
                            }
                        }
                    }
                }

                // Machine Diagnostic Context Banner
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CatSurfaceContainer)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Demag-CAT DH 50T-H12",
                                    color = CatTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(CatSurfaceHighest)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "SETOR B",
                                        color = CatYellow,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Inspeção Nível 2 • Lubrificação do tambor e calibração de limitadores pendentes.",
                                color = CatTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Inspection Steps Items
                items(checklistItems.size) { idx ->
                    val item = checklistItems[idx]
                    ChecklistStepCard(
                        step = item,
                        onStatusChange = { newStatus -> onUpdateStatus(item, newStatus) },
                        onToleranceAdjust = { delta -> onAdjustTolerance(item, delta) },
                        onPreviewPhoto = onPreviewPhoto
                    )
                }

                item {
                    // Space for bottom sticky bar
                    Spacer(modifier = Modifier.height(80.dp))
                }
            } else {
                // Technical Manuals View
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "DOCUMENTAÇÃO OFFLINE",
                                    color = CatYellow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "MANUAIS DO MODELO DEMAG-CAT DH 50T",
                                    color = CatTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CatSurfaceHigh)
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "CACHE 100%",
                                    color = CatTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Documentos técnicos salvos para consulta instantânea sem conectividade na área industrial subterrânea.",
                            color = CatTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                items(manuals.size) { idx ->
                    val manual = manuals[idx]
                    TechnicalManualCard(
                        manual = manual,
                        onOpen = { onOpenManual(manual) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Persistent Bottom Action Dock (when in Checklist mode)
        if (mode == ChecklistMode.CHECKLIST_ATIVO) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(CatSurfaceLowest.copy(alpha = 0.95f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onSaveDraft,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("btn_save_draft"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CatSurfaceHigh,
                            contentColor = CatTextPrimary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                tint = CatYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SALVAR RASCUNHO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onFinalizeAndSign,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("btn_finalize_sign"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CatYellow,
                            contentColor = CatOnYellow
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Draw,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FINALIZAR & ASSINAR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChecklistStepCard(
    step: ChecklistItemEntity,
    onStatusChange: (String) -> Unit,
    onToleranceAdjust: (Double) -> Unit,
    onPreviewPhoto: (String) -> Unit
) {
    val isFault = step.status == "NAO_CONFORME"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CatSurfaceLow)
            .border(
                width = if (isFault) 1.5.dp else 1.dp,
                color = if (isFault) CatCriticalRed else CatSurfaceContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isFault) CatCriticalRedContainer else CatSurfaceHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${step.stepNumber}",
                        color = if (isFault) CatOnCriticalRed else CatTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = step.title.uppercase(),
                    color = if (isFault) CatCriticalRed else CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isFault) CatCriticalRedContainer else CatSurfaceHigh)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isFault) "FALHA" else step.categoryBadge,
                    color = if (isFault) CatOnCriticalRed else CatTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Text(
            text = step.description,
            color = CatTextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        // Tolerance Stepper Gauge if required for this step
        if (step.hasTolerance) {
            MechanicalToleranceStepper(
                currentValue = step.measuredTolerance,
                minSafe = step.minSafeTolerance,
                maxSafe = step.maxSafeTolerance,
                onAdjust = onToleranceAdjust
            )
        }

        // Tri-State Inspection Toggle
        TriStateInspectionToggle(
            currentStatus = step.status,
            onStatusSelected = onStatusChange,
            testTagPrefix = "step_${step.stepNumber}"
        )

        // Non-conformance incident alert banner
        if (isFault) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CatSurfaceLowest)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = CatCriticalRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "AVISO DE NÃO CONFORMIDADE GRAVADA",
                            color = CatCriticalRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = step.nonConformanceNote ?: "Defeito detectado durante a inspeção visual.",
                            color = CatTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Photo Attachment Pill & Thumbnail Strip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            step.photoFileName?.let { onPreviewPhoto(it) }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CatSurfaceHigh)
                                .border(1.dp, CatYellow, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = CatYellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "📷 ${step.photoCount} FOTOS ANEXADAS",
                                color = CatYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = step.photoFileName ?: "foto_anexo.jpg",
                                color = CatTextSecondary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CatSurfaceContainer)
                            .clickable { onPreviewPhoto(step.photoFileName ?: "novo_anexo.jpg") }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = CatTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+ ANEXAR",
                                color = CatTextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TechnicalManualCard(
    manual: TechnicalManual,
    onOpen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CatSurfaceLow)
            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(10.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceLowest),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        manual.title.contains("Elétrico") -> Icons.Default.ElectricBolt
                        manual.title.contains("Lubrificante") -> Icons.Default.OilBarrel
                        else -> Icons.Default.PictureAsPdf
                    },
                    contentDescription = null,
                    tint = CatYellow,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CatSurfaceHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = manual.fileSize,
                            color = CatTextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Text(
                        text = manual.badge,
                        color = if (manual.badge == "DISPONÍVEL") CatSuccessGreen else CatYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = manual.title,
                    color = CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = manual.revision,
                    color = CatTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onOpen,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CatYellow,
                    contentColor = CatOnYellow
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ABRIR OFFLINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Button(
                onClick = onOpen,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CatSurfaceContainer,
                    contentColor = CatTextPrimary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DownloadForOffline,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RE-BAIXAR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
