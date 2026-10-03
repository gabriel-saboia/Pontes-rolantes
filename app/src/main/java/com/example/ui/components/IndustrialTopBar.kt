package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CatDarkBackground
import com.example.ui.theme.CatOnYellow
import com.example.ui.theme.CatSurfaceContainer
import com.example.ui.theme.CatSurfaceHigh
import com.example.ui.theme.CatSurfaceLowest
import com.example.ui.theme.CatTextPrimary
import com.example.ui.theme.CatTextSecondary
import com.example.ui.theme.CatYellow

@Composable
fun IndustrialTopBar(
    onMonthSelected: (String) -> Unit = {}
) {
    var isMonthDropdownOpen by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("OUTUBRO/2026") }
    val months = listOf("OUTUBRO/2026", "SETEMBRO/2026", "AGOSTO/2026", "JULHO/2026")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CatSurfaceLowest)
            .testTag("industrial_top_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left CAT Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                // Tactical CAT Logo Block
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "CAT",
                            color = CatYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PONTES ROLANTES",
                        color = CatYellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isMonthDropdownOpen = true }
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = selectedMonth,
                            color = CatTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Selecionar mês",
                            tint = CatYellow,
                            modifier = Modifier.size(18.dp)
                        )

                        DropdownMenu(
                            expanded = isMonthDropdownOpen,
                            onDismissRequest = { isMonthDropdownOpen = false },
                            modifier = Modifier.background(CatSurfaceContainer)
                        ) {
                            months.forEach { month ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = month,
                                            color = if (month == selectedMonth) CatYellow else CatTextPrimary,
                                            fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedMonth = month
                                        isMonthDropdownOpen = false
                                        onMonthSelected(month)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Right Technician Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CatSurfaceHigh)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CatSurfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "Perfil Roberto Silva (Técnico)",
                        tint = CatYellow,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Industrial Top Progress / Hazard Line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(CatSurfaceHigh)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(3.dp)
                    .background(CatYellow)
            )
        }
    }
}
