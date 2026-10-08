package com.mag5112005.drugdose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mag5112005.drugdose.data.models.DoseRecommendation
import kotlin.math.sin
import kotlin.math.PI

@Composable
fun ProfessionalDoseDisplay(result: DoseRecommendation) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header Card with Gradient
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Prescription",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            result.drugName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Icon(
                        Icons.Default.Medication,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Main Dose Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Dose Amount - Highlighted
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "RECOMMENDED DOSE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    "${result.dose}",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    result.doseUnit,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Icon(
                            Icons.Default.LocalPharmacy,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Administration Details
                DoseInfoRow(
                    icon = Icons.Default.Route,
                    label = "Route",
                    value = result.route,
                    color = Color(0xFF1976D2)
                )
                DoseInfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Frequency",
                    value = result.frequency,
                    color = Color(0xFF388E3C)
                )
                DoseInfoRow(
                    icon = Icons.Default.Timer,
                    label = "Duration",
                    value = result.duration,
                    color = Color(0xFFF57C00)
                )
                DoseInfoRow(
                    icon = Icons.Default.Warning,
                    label = "Max Daily Dose",
                    value = "${result.maxDailyDose} ${result.doseUnit}",
                    color = Color(0xFFD32F2F)
                )
            }
        }

        // Pharmacokinetic Graph Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Pharmacokinetic Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Concentration-Time Curve
                PlasmaConcentrationGraph()
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // PK Parameters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PKParameter(
                        label = "Peak Time",
                        value = "2-4 h",
                        icon = Icons.Default.TrendingUp,
                        color = Color(0xFF1976D2)
                    )
                    PKParameter(
                        label = "Half-Life (t½)",
                        value = "6-8 h",
                        icon = Icons.Default.HourglassBottom,
                        color = Color(0xFF388E3C)
                    )
                    PKParameter(
                        label = "Bioavailability",
                        value = "85%",
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFFF57C00)
                    )
                }
            }
        }

        // Precautions Card
        if (result.precautions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Precautions & Warnings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    result.precautions.forEach { precaution ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFF57C00),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                precaution,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF424242)
                            )
                        }
                    }
                }
            }
        }

        // Drug Interactions Card
        if (result.interactions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MedicalInformation,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Drug Interactions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    result.interactions.forEach { interaction ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(16.dp).padding(end = 4.dp)
                            )
                            Text(
                                interaction,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF424242)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoseInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PKParameter(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun PlasmaConcentrationGraph() {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val points = 100
        val peakTime = 0.25f // 25% of timeline (2-4h in 12h)
        val halfLife = 0.5f // Half-life point
        
        // Draw axes
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = Offset(0f, height),
            end = Offset(width, height),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = Offset(0f, 0f),
            end = Offset(0f, height),
            strokeWidth = 2f
        )
        
        // Draw grid lines
        for (i in 1..5) {
            val y = height * i / 6
            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }
        
        // Draw concentration curve
        val path = Path()
        val pointsList = mutableListOf<Offset>()
        
        for (i in 0 until points) {
            val x = width * i / points
            val normalizedX = i.toFloat() / points
            
            // Pharmacokinetic curve: absorption phase + elimination phase
            val y = if (normalizedX < peakTime) {
                // Absorption phase - increasing
                height * (1f - (normalizedX / peakTime) * 0.85f)
            } else {
                // Elimination phase - exponential decay
                val decay = (normalizedX - peakTime) / (1f - peakTime)
                height * (0.15f + 0.85f * Math.exp(-2.0 * decay).toFloat())
            }
            
            pointsList.add(Offset(x, y))
        }
        
        // Draw area under curve
        val fillPath = Path().apply {
            moveTo(0f, height)
            pointsList.forEach { lineTo(it.x, it.y) }
            lineTo(width, height)
            close()
        }
        
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        )
        
        // Draw curve line
        path.moveTo(pointsList.first().x, pointsList.first().y)
        pointsList.forEach { path.lineTo(it.x, it.y) }
        
        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(width = 4f)
        )
        
        // Draw peak marker
        val peakX = width * peakTime
        val peakY = height * 0.15f
        drawCircle(
            color = secondaryColor,
            radius = 8f,
            center = Offset(peakX, peakY)
        )
        
        // Draw half-life marker
        val halfX = width * (peakTime + halfLife * (1 - peakTime))
        val halfY = height * 0.575f
        drawCircle(
            color = Color(0xFFF57C00),
            radius = 6f,
            center = Offset(halfX, halfY)
        )
    }
    
    // Time axis labels
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("0h", style = MaterialTheme.typography.labelSmall)
        Text("Peak", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        Text("t½", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF57C00))
        Text("12h", style = MaterialTheme.typography.labelSmall)
    }
}
