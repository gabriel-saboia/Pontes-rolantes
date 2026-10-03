package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material3.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CatSurfaceContainer
import com.example.ui.theme.CatSurfaceHigh
import com.example.ui.theme.CatSurfaceLowest
import com.example.ui.theme.CatTextSecondary
import com.example.ui.theme.CatYellow
import com.example.ui.viewmodel.AppTab

@Composable
fun IndustrialBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(CatSurfaceLowest)
            .testTag("industrial_bottom_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem(
            label = "DASHBOARD",
            icon = Icons.Default.Dashboard,
            isSelected = currentTab == AppTab.DASHBOARD,
            testTag = "nav_tab_dashboard",
            onClick = { onTabSelected(AppTab.DASHBOARD) },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            label = "EQUIPMENT",
            icon = Icons.Default.PrecisionManufacturing,
            isSelected = currentTab == AppTab.EQUIPMENT,
            testTag = "nav_tab_equipment",
            onClick = { onTabSelected(AppTab.EQUIPMENT) },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            label = "CHECKLIST",
            icon = Icons.Default.FactCheck,
            isSelected = currentTab == AppTab.CHECKLIST,
            testTag = "nav_tab_checklist",
            onClick = { onTabSelected(AppTab.CHECKLIST) },
            modifier = Modifier.weight(1f)
        )
        BottomNavItem(
            label = "SYNC",
            icon = Icons.Default.CloudSync,
            isSelected = currentTab == AppTab.SYNC,
            testTag = "nav_tab_sync",
            onClick = { onTabSelected(AppTab.SYNC) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(72.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = CatYellow),
                onClick = onClick
            )
            .testTag(testTag)
            .background(if (isSelected) CatSurfaceHigh else CatSurfaceLowest),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) CatYellow else CatTextSecondary,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = label,
                color = if (isSelected) CatYellow else CatTextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Active indicator line on top of button
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(CatYellow)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
