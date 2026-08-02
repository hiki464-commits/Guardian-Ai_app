package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GuardianAccent
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess

@Composable
fun SimulatedMapCanvas(
    modifier: Modifier = Modifier,
    hospitalName: String = "Stanford Health Trauma Center",
    etaMinutes: Int = 4,
    showAmbulanceRoute: Boolean = true
) {
    val progressAnim = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 0.85f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .border(1.dp, GuardianPrimary.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw map grid lines
                val gridColor = Color(0xFF1E293B)
                for (x in 0..w.toInt() step 80) {
                    drawLine(gridColor, Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), strokeWidth = 1f)
                }
                for (y in 0..h.toInt() step 80) {
                    drawLine(gridColor, Offset(0f, y.toFloat()), Offset(w, y.toFloat()), strokeWidth = 1f)
                }

                // Main roads
                val roadColor = Color(0xFF334155)
                drawLine(roadColor, Offset(0f, h * 0.4f), Offset(w, h * 0.4f), strokeWidth = 18f)
                drawLine(roadColor, Offset(w * 0.5f, 0f), Offset(w * 0.5f, h), strokeWidth = 18f)

                if (showAmbulanceRoute) {
                    // Route curve from Ambulance Dispatch -> Collision Site -> Hospital
                    val routePath = Path().apply {
                        moveTo(w * 0.15f, h * 0.8f)
                        quadraticTo(w * 0.35f, h * 0.25f, w * 0.85f, h * 0.3f)
                    }

                    drawPath(
                        path = routePath,
                        color = GuardianPrimary,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 6f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                        )
                    )

                    // Draw Collision Point
                    drawCircle(
                        color = GuardianDanger,
                        radius = 12f,
                        center = Offset(w * 0.15f, h * 0.8f)
                    )

                    // Draw Hospital Point
                    drawCircle(
                        color = GuardianSuccess,
                        radius = 14f,
                        center = Offset(w * 0.85f, h * 0.3f)
                    )

                    // Interpolated Ambulance Position along path
                    val t = progressAnim.value
                    val currentX = (1 - t) * (1 - t) * (w * 0.15f) + 2 * (1 - t) * t * (w * 0.35f) + t * t * (w * 0.85f)
                    val currentY = (1 - t) * (1 - t) * (h * 0.8f) + 2 * (1 - t) * t * (h * 0.25f) + t * t * (h * 0.3f)

                    drawCircle(
                        color = GuardianAccent,
                        radius = 16f,
                        center = Offset(currentX, currentY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = Offset(currentX, currentY)
                    )
                }
            }

            // Top Status Overlay
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .background(Color(0xEE0F172A), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = null,
                        tint = GuardianAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE DISPATCH TRACKING • $etaMinutes MIN ETA",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom Destination Card Overlay
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0xEE1E293B), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(GuardianSuccess.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = GuardianSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hospitalName,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Level 1 Trauma Pre-Reserved • Priority Green Wave",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
