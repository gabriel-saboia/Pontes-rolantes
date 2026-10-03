package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.TechnicalManual
import com.example.ui.theme.CatCriticalRed
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

@Composable
fun SignatureDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var remarks by remember { mutableStateOf("Inspeção quinzenal de cabos e sistema de frenagem concluída em conformidade com as normas CAT-FMS.") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CatSurfaceLow)
                .border(1.dp, CatYellow, RoundedCornerShape(12.dp))
                .padding(18.dp)
                .testTag("dialog_signature"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Draw,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ASSINATURA DIGITAL DO INSPETOR",
                        color = CatTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = CatTextSecondary
                    )
                }
            }

            // Technician credentials pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceContainer)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Engineering,
                    contentDescription = null,
                    tint = CatYellow,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Roberto Silva • CAT-9942",
                        color = CatTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Técnico Especialista em Pontes Rolantes • Turno A",
                        color = CatTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Column {
                Text(
                    text = "PARECER TÉCNICO / OBSERVAÇÕES:",
                    color = CatTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CatYellow,
                        unfocusedBorderColor = CatSurfaceContainer,
                        focusedTextColor = CatTextPrimary,
                        unfocusedTextColor = CatTextPrimary,
                        focusedContainerColor = CatSurfaceLowest,
                        unfocusedContainerColor = CatSurfaceLowest
                    ),
                    minLines = 3,
                    maxLines = 4
                )
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatSurfaceHigh,
                        contentColor = CatTextPrimary
                    )
                ) {
                    Text(text = "CANCELAR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { onConfirm(remarks) },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("btn_confirm_sign"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatYellow,
                        contentColor = CatOnYellow
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "ASSINAR & CONCLUIR", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ManualPreviewDialog(
    manual: TechnicalManual,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CatSurfaceLow)
                .border(1.dp, CatSurfaceContainer, RoundedCornerShape(12.dp))
                .padding(18.dp)
                .testTag("dialog_manual_preview"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MANUAL TÉCNICO OFFLINE",
                        color = CatYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = CatTextSecondary
                    )
                }
            }

            Text(
                text = manual.title,
                color = CatTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceContainer)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "MODELO APLICÁVEL: ${manual.equipmentModel}",
                        color = CatYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "REFERÊNCIA: ${manual.code} • ${manual.revision}",
                        color = CatTextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "TAMANHO: ${manual.fileSize} (Armazenado na memória interna do dispositivo)",
                        color = CatSuccessGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Parâmetros Críticos de Fábrica:\n• Tensão de operação: Trifásico 440V ± 5%\n• Faixa de folga das sapatas: 1.20mm a 1.80mm\n• Lubrificante homologado: Graxa de Lítio EP-2 / Redutor ISO VG 220\n• Critério de descarte de cabos: máx. 6 arames partidos em 6 passos",
                color = CatTextPrimary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CatYellow,
                    contentColor = CatOnYellow
                )
            ) {
                Text(text = "FECHAR VISUALIZADOR", fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DefectPhotoPreviewDialog(
    photoName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CatSurfaceLow)
                .border(1.dp, CatCriticalRed, RoundedCornerShape(12.dp))
                .padding(18.dp)
                .testTag("dialog_defect_photo"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = CatCriticalRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "REGISTRO FOTOGRÁFICO DE NÃO CONFORMIDADE",
                        color = CatCriticalRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = CatTextSecondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CatSurfaceLowest)
                    .border(1.dp, CatSurfaceContainer, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = CatYellow,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = photoName,
                        color = CatYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Macro de filamento rompido na espira superior do tambor leste.",
                        color = CatTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CatSurfaceContainer)
                    .padding(10.dp)
            ) {
                Text(
                    text = "Classificação de Risco: Grau 3 (Monitoramento imediato & substituição no próximo fechamento de linha).",
                    color = CatCriticalRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CatSurfaceHigh,
                    contentColor = CatTextPrimary
                )
            ) {
                Text(text = "VOLTAR AO CHECKLIST", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
