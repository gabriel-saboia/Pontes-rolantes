package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BacklogEntity
import com.example.data.model.EquipmentEntity
import com.example.data.model.FieldNoteEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.PreventiveEntity
import com.example.ui.components.HazardStripesBar
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
import com.example.ui.theme.CatYellowDim
import com.example.ui.viewmodel.EquipmentSubTab

@Composable
fun EquipmentScreen(
    equipment: EquipmentEntity?,
    subTab: EquipmentSubTab,
    onSubTabSelected: (EquipmentSubTab) -> Unit,
    backlogs: List<BacklogEntity>,
    preventives: List<PreventiveEntity>,
    history: List<HistoryEntity>,
    notes: List<FieldNoteEntity>,
    onOpenChecklist: () -> Unit,
    onOpenManuals: () -> Unit,
    onAddNote: (String) -> Unit
) {
    val activeCrane = equipment ?: EquipmentEntity(
        tag = "28HX000246",
        name = "Ponte Rolante Biviga CAT-DEMAG Heavy Duty 50T/10T",
        model = "CAT-DEMAG DH 50T-H12",
        location = "Setor 28PI-B • Linha de Montagem de Chassis",
        sector = "Chassis & Montagem Final",
        capacity = "50 / 10 TON",
        spanElevation = "28.5m / 16m",
        femGroup = "A6 / M6"
    )

    var quickNoteText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CatDarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("equipment_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Equipment Header Card with Hazard Bar
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CatSurfaceLow)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
            ) {
                // High contrast hazard diagonal stripes banner
                HazardStripesBar(height = 10)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Top Status & ID Block
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TAG DE ATIVO",
                                color = CatTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = activeCrane.tag,
                                color = CatYellow,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Pulsing status pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(CatCriticalRedContainer.copy(alpha = 0.4f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(CatCriticalRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activeCrane.status,
                                    color = CatCriticalRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    // Equipment Name & Location
                    Column {
                        Text(
                            text = activeCrane.name,
                            color = CatTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CatYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = activeCrane.location,
                                color = CatTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Telemetry Spec Badges (3 columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecBadge(
                            label = "CAPACIDADE",
                            value = activeCrane.capacity,
                            modifier = Modifier.weight(1f)
                        )
                        SpecBadge(
                            label = "VÃO / ELEVAÇÃO",
                            value = activeCrane.spanElevation,
                            modifier = Modifier.weight(1f)
                        )
                        SpecBadge(
                            label = "GRUPO FEM",
                            value = activeCrane.femGroup,
                            isHighlighted = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Quick Action Bar
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Primary CTA: Active Checklist
                        Button(
                            onClick = onOpenChecklist,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("btn_open_checklist"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CatYellow,
                                contentColor = CatOnYellow
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FactCheck,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ABRIR CHECKLIST ATIVO",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Secondary Quick Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onOpenManuals,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_technical_manuals"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CatSurfaceHigh,
                                    contentColor = CatTextPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureAsPdf,
                                    contentDescription = null,
                                    tint = CatYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MANUAIS TÉCNICOS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            Button(
                                onClick = { onSubTabSelected(EquipmentSubTab.NOTAS) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("btn_new_incident"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CatSurfaceHigh,
                                    contentColor = CatTextPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    tint = CatYellow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "NOVA OCORRÊNCIA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sub-Navigation Segmented Scroll Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CatSurfaceLowest)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SubTabButton(
                    label = "PREVENTIVAS",
                    badge = null,
                    isSelected = subTab == EquipmentSubTab.PREVENTIVAS,
                    onClick = { onSubTabSelected(EquipmentSubTab.PREVENTIVAS) },
                    modifier = Modifier.weight(1f),
                    testTag = "subtab_preventivas"
                )
                SubTabButton(
                    label = "BACKLOGS",
                    badge = "${backlogs.size}",
                    isSelected = subTab == EquipmentSubTab.BACKLOGS,
                    onClick = { onSubTabSelected(EquipmentSubTab.BACKLOGS) },
                    modifier = Modifier.weight(1f),
                    testTag = "subtab_backlogs"
                )
                SubTabButton(
                    label = "HISTÓRICO",
                    badge = null,
                    isSelected = subTab == EquipmentSubTab.HISTORICO,
                    onClick = { onSubTabSelected(EquipmentSubTab.HISTORICO) },
                    modifier = Modifier.weight(1f),
                    testTag = "subtab_historico"
                )
                SubTabButton(
                    label = "NOTAS",
                    badge = null,
                    isSelected = subTab == EquipmentSubTab.NOTAS,
                    onClick = { onSubTabSelected(EquipmentSubTab.NOTAS) },
                    modifier = Modifier.weight(1f),
                    testTag = "subtab_notas"
                )
            }
        }

        // SUB-TAB CONTENT
        when (subTab) {
            EquipmentSubTab.BACKLOGS -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = CatYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${backlogs.size} BACKLOGS PENDENTES",
                                color = CatTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "TAG ${activeCrane.tag}",
                            color = CatTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(backlogs.size) { idx ->
                    val b = backlogs[idx]
                    BacklogDetailCard(b)
                }

                // Historical Timeline teaser section
                item {
                    HistoricalTimelineSection(
                        history = history,
                        onViewAllHistory = { onSubTabSelected(EquipmentSubTab.HISTORICO) }
                    )
                }

                // Turn Notes Section
                item {
                    TurnNotesSection(
                        notes = notes,
                        quickNoteText = quickNoteText,
                        onQuickNoteChange = { quickNoteText = it },
                        onSubmitNote = {
                            if (quickNoteText.isNotBlank()) {
                                onAddNote(quickNoteText)
                                quickNoteText = ""
                            }
                        }
                    )
                }
            }

            EquipmentSubTab.PREVENTIVAS -> {
                item {
                    Text(
                        text = "ORDENS DE SERVIÇO PREVENTIVAS AGENDADAS",
                        color = CatYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }

                items(preventives.size) { idx ->
                    val p = preventives[idx]
                    PreventiveCard(p)
                }
            }

            EquipmentSubTab.HISTORICO -> {
                item {
                    HistoricalTimelineSection(
                        history = history,
                        onViewAllHistory = {},
                        showAll = true
                    )
                }
            }

            EquipmentSubTab.NOTAS -> {
                item {
                    TurnNotesSection(
                        notes = notes,
                        quickNoteText = quickNoteText,
                        onQuickNoteChange = { quickNoteText = it },
                        onSubmitNote = {
                            if (quickNoteText.isNotBlank()) {
                                onAddNote(quickNoteText)
                                quickNoteText = ""
                            }
                        },
                        showAll = true
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
private fun SpecBadge(
    label: String,
    value: String,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CatSurfaceContainer)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = CatTextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            color = if (isHighlighted) CatYellow else CatTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun SubTabButton(
    label: String,
    badge: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CatSurfaceHigh else Color.Transparent)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                color = if (isSelected) CatYellow else CatTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
            if (badge != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CatYellow)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        color = CatOnYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun BacklogDetailCard(b: BacklogEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CatSurfaceLow)
            .border(
                width = 1.5.dp,
                color = if (b.isCritical) CatCriticalRed else CatYellowDim,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CatSurfaceHighest)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = b.osNumber,
                        color = CatTextPrimary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (b.isCritical) CatCriticalRed else CatSurfaceHigh)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = b.criticality,
                        color = if (b.isCritical) CatOnCriticalRed else CatYellowDim,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(CatSurfaceContainer)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = b.statusAction,
                    color = if (b.statusAction.contains("TRÂNSITO")) CatYellow else CatYellowDim,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Text(
            text = b.description,
            color = CatTextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        b.technicianName?.let { tech ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CatSurfaceContainer)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$tech • ${b.createdAt}",
                        color = CatTextSecondary,
                        fontSize = 11.sp
                    )
                }
                if (b.isCritical) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = CatCriticalRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(CatSurfaceHigh)
                    .clickable { }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = null,
                        tint = CatTextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ATUALIZAR STATUS",
                        color = CatTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            b.partNumber?.let { part ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CatSurfaceHigh)
                        .clickable { }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PEÇA #$part",
                            color = CatYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricalTimelineSection(
    history: List<HistoryEntity>,
    onViewAllHistory: () -> Unit,
    showAll: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CatSurfaceLow)
            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(10.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = CatYellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "HISTÓRICO RECENTE (2022+)",
                    color = CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
            if (!showAll) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onViewAllHistory)
                ) {
                    Text(
                        text = "VER ${history.size} OS",
                        color = CatYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        val displayList = if (showAll) history else history.take(3)
        displayList.forEach { h ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when (h.badgeType) {
                                "APROVADO" -> CatYellow
                                "CONCLUÍDO" -> CatYellowDim
                                else -> CatTextSecondary
                            }
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CatSurfaceContainer)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${h.closedDate} • ${h.osNumber}",
                            color = CatYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CatSurfaceHigh)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = h.badgeType,
                                color = CatTextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = h.osDescription,
                        color = CatTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TurnNotesSection(
    notes: List<FieldNoteEntity>,
    quickNoteText: String,
    onQuickNoteChange: (String) -> Unit,
    onSubmitNote: () -> Unit,
    showAll: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CatSurfaceLow)
            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(10.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = null,
                    tint = CatYellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NOTAS DE CAMPO DO TURNO",
                    color = CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(CatSurfaceHigh)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "TURNO 02",
                    color = CatTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        val displayNotes = if (showAll) notes else notes.take(2)
        displayNotes.forEach { note ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CatSurfaceContainer)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${note.author} • ${note.timestampText}",
                            color = CatTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (note.isAudioTranscribed) {
                        Text(
                            text = "ÁUDIO TRANSCRITO",
                            color = CatYellowDim,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    color = CatTextPrimary,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 16.sp
                )
            }
        }

        // Quick Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceLowest)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                TextField(
                    value = quickNoteText,
                    onValueChange = onQuickNoteChange,
                    placeholder = {
                        Text(
                            text = "Adicionar nota de turno...",
                            color = CatTextSecondary.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = CatTextPrimary,
                        unfocusedTextColor = CatTextPrimary
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceContainer)
                    .clickable { /* mic voice simulation */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voz",
                    tint = CatYellow,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceContainer)
                    .clickable { /* photo simulation */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Foto",
                    tint = CatYellow,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatYellow)
                    .clickable(onClick = onSubmitNote)
                    .testTag("btn_send_turn_note"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar",
                    tint = CatOnYellow,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun PreventiveCard(p: PreventiveEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CatSurfaceLow)
            .border(1.dp, CatSurfaceContainer, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${p.osNumber} • ${p.plannedDate}",
                color = CatYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (p.isCompleted) CatSuccessGreen.copy(alpha = 0.2f) else CatSurfaceHigh)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (p.isCompleted) "CONCLUÍDA" else "PROGRAMADA",
                    color = if (p.isCompleted) CatSuccessGreen else CatYellowDim,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = p.osDescription,
            color = CatTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${p.serviceDescription} (${p.location})",
            color = CatTextSecondary,
            fontSize = 11.sp
        )
    }
}
