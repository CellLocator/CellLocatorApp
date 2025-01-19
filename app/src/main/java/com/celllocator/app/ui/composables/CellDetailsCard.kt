package com.celllocator.app.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.celllocator.app.entities.Cell
import com.celllocator.app.util.getNodeName

@Composable
fun CellDetailsCard(cellDetails: Cell) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${getNodeName(cellDetails.networkType)} ${cellDetails.getEnbNumber() ?: "???"}:${cellDetails.getSector()} - ${cellDetails.networkType}",
                    color = getFontColor(true),
                    fontSize = 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${cellDetails.rsrp} dBm",
                        color = getFontColor(true),
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MCC: " + (cellDetails.mcc ?: "?") + " MNC: " + (cellDetails.mnc ?: "?") + " TAC: " + (cellDetails.tac ?: "?"),
                        color = getFontColor(true),
                        fontSize = 16.sp
                    )
                }
            }
            HorizontalDivider(thickness = 0.2.dp, color = getFontColor())

            Column(
                verticalArrangement = Arrangement.spacedBy((-5).dp)
            ) {
                if (cellDetails.pci != null) {
                    InfoRow(label = "PCI", value = cellDetails.pci.toString())
                }
                if (cellDetails.rsrq != null) {
                    InfoRow(label = "RSRQ", value = "${cellDetails.rsrq} dB")
                }
                if (cellDetails.sinr != null) {
                    InfoRow(label = "SINR", value = "${cellDetails.sinr} dB")
                }
                InfoRow(label = "RX Frequency", value = "${cellDetails.rxFrequency} MHz")
                InfoRow(label = "TX Frequency", value = "${cellDetails.txFrequency} MHz")
                if (cellDetails.pci != null) {
                    InfoRow(label = "PCI", value = cellDetails.pci.toString())
                }
                if (cellDetails.rsrq != null) {
                    InfoRow(label = "RSRQ", value = "${cellDetails.rsrq} dB")
                }
                if (cellDetails.sinr != null) {
                    InfoRow(label = "SINR", value = "${cellDetails.sinr} dB")
                }
                InfoRow(label = "Band Nr", value = cellDetails.bandNr.toString())
            }
        }
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = getFontColor(),
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = getFontColor(),
            fontSize = 14.sp
        )
    }
}


@Composable
fun getFontColor(infoHeader : Boolean = false): Color {
    val context = LocalContext.current
    val isDarkTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    if (infoHeader) {
        return if (isDarkTheme) Color.LightGray else Color.DarkGray
    }
    return if (isDarkTheme) Color.White else Color.Black
}