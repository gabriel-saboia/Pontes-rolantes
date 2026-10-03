package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.CatWarningAmber
import com.example.ui.theme.CatYellow

/**
 * Faixa visual de advertência padrão Caterpillar (amarelo e preto em 45 graus)
 */
@Composable
fun HazardStripesBar(
    modifier: Modifier = Modifier,
    height: Int = 8
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        val stripeWidth = 24f
        val w = size.width
        val h = size.height

        drawRect(color = Color(0xFF0A0E15))

        var x = -h
        while (x < w + h) {
            drawLine(
                color = Color(0xFFFFCD00),
                start = Offset(x, 0f),
                end = Offset(x + h, h),
                strokeWidth = stripeWidth / 2
            )
            x += stripeWidth
        }
    }
}

/**
 * Botão industrial largo, ergonômico para uso com luvas de proteção
 */
@Composable
fun GloveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    testTag: String = "glove_button"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) CatYellow else CatSurfaceHigh,
            contentColor = if (isPrimary) CatOnYellow else CatTextPrimary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Seletor de 3 estados para Checklist: Conforme / Não Conforme / N/A
 */
@Composable
fun TriStateInspectionToggle(
    currentStatus: String, // "CONFORME", "NAO_CONFORME", "NA", "PENDENTE"
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "step"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Conforme
        val isConforme = currentStatus == "CONFORME"
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isConforme) CatSurfaceHighest else CatSurfaceLowest)
                .border(
                    width = if (isConforme) 1.5.dp else 1.dp,
                    color = if (isConforme) CatSuccessGreen else CatSurfaceContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onStatusSelected("CONFORME") }
                .testTag("${testTagPrefix}_btn_conforme"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Conforme",
                    tint = if (isConforme) CatSuccessGreen else CatTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "CONFORME",
                    color = if (isConforme) CatTextPrimary else CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Não Conforme
        val isNaoConforme = currentStatus == "NAO_CONFORME"
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isNaoConforme) CatCriticalRedContainer else CatSurfaceLowest)
                .border(
                    width = if (isNaoConforme) 2.dp else 1.dp,
                    color = if (isNaoConforme) CatCriticalRed else CatSurfaceContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onStatusSelected("NAO_CONFORME") }
                .testTag("${testTagPrefix}_btn_nao_conforme"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isNaoConforme) Icons.Default.Error else Icons.Default.Cancel,
                    contentDescription = "Não Conforme",
                    tint = if (isNaoConforme) CatOnCriticalRed else CatTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "NÃO CONF.",
                    color = if (isNaoConforme) CatOnCriticalRed else CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // N / A
        val isNA = currentStatus == "NA"
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isNA) CatSurfaceHigh else CatSurfaceLowest)
                .border(
                    width = 1.dp,
                    color = if (isNA) CatYellow else CatSurfaceContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onStatusSelected("NA") }
                .testTag("${testTagPrefix}_btn_na"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Não Aplicável",
                    tint = if (isNA) CatYellow else CatTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "N / A",
                    color = if (isNA) CatYellow else CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Stepper para medição e calibração de folgas mecânicas (freios e sapatas)
 */
@Composable
fun MechanicalToleranceStepper(
    currentValue: Double,
    minSafe: Double = 1.20,
    maxSafe: Double = 1.80,
    onAdjust: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSafe = currentValue in minSafe..maxSafe

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CatSurfaceLowest)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FOLGA APURADA (MM)",
                    color = CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isSafe) CatSuccessGreen else CatCriticalRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSafe) "Faixa Segura ($minSafe - $maxSafe)" else "Fora de Tolerância!",
                        color = if (isSafe) CatSuccessGreen else CatCriticalRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Stepper Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceContainer)
                        .clickable { onAdjust(-0.05) }
                        .testTag("stepper_minus"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Diminuir folga",
                        tint = CatTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .width(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceHigh)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format(java.util.Locale.US, "%.2f", currentValue),
                            color = CatYellow,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "mm",
                            color = CatTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CatSurfaceContainer)
                        .clickable { onAdjust(0.05) }
                        .testTag("stepper_plus"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Aumentar folga",
                        tint = CatTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
