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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.ui.viewmodel.DashboardFilter

@Composable
fun DashboardScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: DashboardFilter,
    onFilterChange: (DashboardFilter) -> Unit,
    pendingCount: Int,
    completedCount: Int,
    totalCount: Int,
    onSelectEquipment: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CatDarkBackground)
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Search Input with Optical icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceLowest)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = CatTextSecondary,
                        modifier = Modifier
                            .padding(start = 14.dp, end = 8.dp)
                            .size(24.dp)
                    )

                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                text = "PESQUISAR POR TAG (EX: 28HX000246)...",
                                color = CatTextSecondary.copy(alpha = 0.6f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
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
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_tag_input")
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Backspace,
                                contentDescription = "Limpar pesquisa",
                                tint = CatTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Summary KPI Tiles (2 Columns Grid)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Preventivas Pendentes
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceLow)
                        .border(
                            width = 1.dp,
                            color = CatSurfaceContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PREVENTIVAS PENDENTES",
                                color = CatTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Icon(
                                imageVector = Icons.Default.AssignmentLate,
                                contentDescription = null,
                                tint = CatYellow,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$pendingCount",
                                color = CatTextPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CatSurfaceHighest)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "3 P/ HOJE",
                                    color = CatYellow,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // Concluídas do Mês
                val percentage = if (totalCount > 0) ((completedCount.toFloat() / totalCount) * 100).toInt() else 78
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(105.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceLow)
                        .border(
                            width = 1.dp,
                            color = CatSurfaceContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CONCLUÍDAS DO MÊS",
                                color = CatTextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CatYellowDim,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$percentage%",
                                color = CatTextPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CatSurfaceHighest)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$completedCount / $totalCount",
                                    color = CatTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Mini progress bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CatSurfaceHighest)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(percentage / 100f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(CatYellow)
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Status Filter Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterPill(
                    label = "TODOS",
                    count = 20,
                    isSelected = selectedFilter == DashboardFilter.TODOS,
                    onClick = { onFilterChange(DashboardFilter.TODOS) },
                    modifier = Modifier.weight(1f),
                    testTag = "filter_todos"
                )
                FilterPill(
                    label = "PENDENTES",
                    count = pendingCount,
                    isSelected = selectedFilter == DashboardFilter.PENDENTES,
                    onClick = { onFilterChange(DashboardFilter.PENDENTES) },
                    modifier = Modifier.weight(1f),
                    testTag = "filter_pendentes"
                )
                FilterPill(
                    label = "PRONTOS",
                    count = completedCount,
                    isSelected = selectedFilter == DashboardFilter.PRONTOS,
                    onClick = { onFilterChange(DashboardFilter.PRONTOS) },
                    modifier = Modifier.weight(1f),
                    testTag = "filter_prontos"
                )
            }
        }

        // SECTION A: QUANTIDADE DE EQUIPAMENTOS POR SEÇÃO
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceLow)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(8.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PieChart,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "QUANTIDADE DE EQUIPAMENTOS POR SEÇÃO",
                            color = CatTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "TOTAL: 42",
                        color = CatTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SectionProgressItem(
                    name = "1. Forjaria & Laminação",
                    count = 14,
                    percentage = 33
                )
                Spacer(modifier = Modifier.height(10.dp))
                SectionProgressItem(
                    name = "2. Chassis & Montagem Final",
                    count = 12,
                    percentage = 29
                )
                Spacer(modifier = Modifier.height(10.dp))
                SectionProgressItem(
                    name = "3. Usinagem Pesada",
                    count = 9,
                    percentage = 21
                )
                Spacer(modifier = Modifier.height(10.dp))
                SectionProgressItem(
                    name = "4. Baia de Solda & Caldeiraria",
                    count = 7,
                    percentage = 17
                )
            }
        }

        // SECTION B: EQUIPAMENTOS COM MAIS CORRETIVAS (TOP OFENSORES)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = CatCriticalRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EQUIPAMENTOS COM MAIS CORRETIVAS",
                            color = CatTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CatCriticalRedContainer.copy(alpha = 0.6f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "TOP OFENSORES",
                            color = CatOnCriticalRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                CorrectiveOffenderCard(
                    tag = "28HX000246",
                    name = "Ponte Demag 50T",
                    correctiveCount = 11,
                    fault = "Desgaste prematuro de freio de translação",
                    criticality = "ALTA CRITICIDADE",
                    isHighCriticality = true,
                    onClick = { onSelectEquipment("28HX000246") }
                )

                CorrectiveOffenderCard(
                    tag = "28HX000189",
                    name = "Guindaste Giratório 10T",
                    correctiveCount = 8,
                    fault = "Folga excessiva em rolamento axial",
                    criticality = "ALTA CRITICIDADE",
                    isHighCriticality = true,
                    onClick = { onSelectEquipment("28HX000189") }
                )

                CorrectiveOffenderCard(
                    tag = "28HX000312",
                    name = "Ponte Pórtico Biviga 30T",
                    correctiveCount = 6,
                    fault = "Sobreaquecimento contator Schneider",
                    criticality = "MÉDIA CRITICIDADE",
                    isHighCriticality = false,
                    onClick = { onSelectEquipment("28HX000312") }
                )
            }
        }

        // SECTION C: EQUIPAMENTOS COM BACKLOG
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PendingActions,
                            contentDescription = null,
                            tint = CatYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EQUIPAMENTOS COM BACKLOG",
                            color = CatTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "3 ATIVOS",
                        color = CatTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                BacklogCraneSummaryCard(
                    tag = "28HX000246",
                    name = "Ponte 50T - Montagem",
                    backlogCount = 3,
                    primaryIssue = "• 1 Crítico: Sapatas de freio c/ folga",
                    secondaryIssue = "• 2 Médios: Vazamento redutor, lâmpada strobo",
                    statusAction = "AG. PEÇA",
                    hasCritical = true,
                    onClick = { onSelectEquipment("28HX000246") }
                )

                BacklogCraneSummaryCard(
                    tag = "28HX000405",
                    name = "Ponte 20T - Usinagem",
                    backlogCount = 2,
                    primaryIssue = "• Trinca leve em batente fim curso; Cabo botoeira",
                    secondaryIssue = null,
                    statusAction = "AG. PARADA",
                    hasCritical = false,
                    onClick = { onSelectEquipment("28HX000405") }
                )

                BacklogCraneSummaryCard(
                    tag = "28HX000114",
                    name = "Monovia Talha 5T - Caldeiraria",
                    backlogCount = 1,
                    primaryIssue = "• Ajuste de esteira porta-cabos festoon",
                    secondaryIssue = null,
                    statusAction = "LIBERADO P/ EXECUÇÃO",
                    hasCritical = false,
                    onClick = { onSelectEquipment("28HX000114") }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) CatYellow else CatSurfaceLow)
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
                color = if (isSelected) CatOnYellow else CatTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "($count)",
                color = if (isSelected) CatOnYellow.copy(alpha = 0.8f) else CatYellow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionProgressItem(
    name: String,
    count: Int,
    percentage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CatSurfaceContainer)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = CatTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$count UNID. ($percentage%)",
                color = CatYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CatSurfaceHighest)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(CatYellow)
            )
        }
    }
}

@Composable
private fun CorrectiveOffenderCard(
    tag: String,
    name: String,
    correctiveCount: Int,
    fault: String,
    criticality: String,
    isHighCriticality: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CatSurfaceLow)
            .border(
                width = 1.dp,
                color = if (isHighCriticality) CatCriticalRed else CatYellowDim,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
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
                        .background(CatSurfaceLowest)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "TAG: $tag",
                        color = CatYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name.uppercase(),
                    color = CatTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isHighCriticality) CatCriticalRedContainer else CatSurfaceHigh)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$correctiveCount CORRETIVAS",
                    color = if (isHighCriticality) CatOnCriticalRed else CatYellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(CatSurfaceContainer)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.BuildCircle,
                    contentDescription = null,
                    tint = if (isHighCriticality) CatCriticalRed else CatYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "FALHA: ",
                    color = CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = fault,
                    color = CatTextPrimary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
            Text(
                text = criticality,
                color = if (isHighCriticality) CatCriticalRed else CatYellow,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun BacklogCraneSummaryCard(
    tag: String,
    name: String,
    backlogCount: Int,
    primaryIssue: String,
    secondaryIssue: String?,
    statusAction: String,
    hasCritical: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CatSurfaceLow)
            .border(
                width = 1.dp,
                color = if (hasCritical) CatYellow else CatSurfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
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
                        .background(CatSurfaceLowest)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "TAG: $tag",
                        color = CatYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name.uppercase(),
                    color = CatTextPrimary,
                    fontSize = 13.sp,
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
                    text = "$backlogCount BACKLOG${if (backlogCount > 1) "S" else ""}",
                    color = CatYellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(CatSurfaceContainer)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = primaryIssue,
                    color = if (hasCritical) CatCriticalRed else CatTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CatSurfaceHigh)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = statusAction,
                        color = if (statusAction.contains("LIBERAD")) CatYellow else CatTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            if (secondaryIssue != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = secondaryIssue,
                    color = CatTextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
