package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SafetyCheck
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiThinkingIndicator
import com.example.ui.components.GlassCard
import com.example.ui.components.GuardianCard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.PulseBadge
import com.example.ui.components.SecondaryButton
import com.example.ui.components.SimulatedMapCanvas
import com.example.ui.components.StepProgressTimeline
import com.example.ui.theme.GuardianAccent
import com.example.ui.theme.GuardianBackgroundDark
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSecondary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextMuted
import com.example.ui.theme.GuardianWarning
import com.example.viewmodel.EmergencyViewModel

// -------------------------------------------------------------
// 1. JOURNEY MONITORING SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyMonitoringScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit,
    onSimulateAccident: () -> Unit
) {
    val telemetry by viewModel.telemetryState.collectAsState()
    val isJourneyActive by viewModel.isJourneyActive.collectAsState()
    val durationSeconds by viewModel.journeyDurationSeconds.collectAsState()

    val minutes = durationSeconds / 60
    val seconds = durationSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Journey Guard", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Live Map Preview
                SimulatedMapCanvas(
                    hospitalName = "Guardian Telematics GPS Active",
                    etaMinutes = 0,
                    showAmbulanceRoute = false
                )

                // Status Bar Card
                GuardianCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MONITORING ACTIVE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GuardianSuccess
                            )
                            Text(
                                text = "Trip Duration: $timeFormatted",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        PulseBadge(text = "AI GUARD ON", color = GuardianSuccess)
                    }
                }

                // Live Telematics Cards
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GuardianCard(modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = GuardianPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Speed", fontSize = 12.sp, color = GuardianTextMuted)
                        Text("${telemetry.speedMph} mph", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    GuardianCard(modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.SafetyCheck, contentDescription = null, tint = GuardianAccent)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("G-Force", fontSize = 12.sp, color = GuardianTextMuted)
                        Text("${telemetry.impactForceG} G", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    GuardianCard(modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = GuardianSuccess)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Crash Risk", fontSize = 12.sp, color = GuardianTextMuted)
                        Text("LOW", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GuardianSuccess)
                    }
                }
            }

            // SIMULATE ACCIDENT BUTTON (PROMINENT RED)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSimulateAccident,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianDanger)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "SIMULATE CRASH / ACCIDENT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                SecondaryButton(
                    text = if (isJourneyActive) "Stop Journey" else "Start Journey",
                    onClick = {
                        if (isJourneyActive) viewModel.stopJourney() else viewModel.startJourney()
                    },
                    icon = Icons.Default.Stop
                )
            }
        }
    }
}

// -------------------------------------------------------------
// -------------------------------------------------------------
// 2. AI ANALYSIS SCREEN (SCREEN 5)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAnalysisScreen(
    viewModel: EmergencyViewModel,
    onNavigateToCountdown: () -> Unit
) {
    val isAnalyzing by viewModel.isAiAnalyzing.collectAsState()
    val analysis by viewModel.currentAiAnalysis.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Emergency Decision", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isAnalyzing || analysis == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AiThinkingIndicator(
                        title = "AI Analyzing Emergency Situation",
                        statusMessage = "OpenAI processing telemetry: Speed Drop 68mph, G-Force 38.4G..."
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = GuardianDanger.copy(alpha = 0.12f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GuardianDanger.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "AI Has Analyzed Situation",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GuardianDanger
                                    )
                                    PulseBadge(
                                        text = "${analysis!!.confidenceScore}% CONFIDENCE",
                                        color = GuardianDanger
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = analysis!!.reasoningText,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Key metrics summary card grid
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GuardianCard(modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Nearest Hospital", fontSize = 11.sp, color = GuardianTextMuted)
                                Text(analysis!!.recommendedHospital.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("${analysis!!.recommendedHospital.distanceKm} km away", fontSize = 11.sp, color = GuardianPrimary)
                            }

                            GuardianCard(modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = GuardianAccent)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Nearest Ambulance", fontSize = 11.sp, color = GuardianTextMuted)
                                Text(analysis!!.recommendedAmbulance.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text("${analysis!!.recommendedAmbulance.etaMinutes} min away", fontSize = 11.sp, color = GuardianAccent)
                            }
                        }
                    }

                    item {
                        GuardianCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Estimated Cost", fontSize = 12.sp, color = GuardianTextMuted)
                                    Text("₹850 / $85 USD", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GuardianSuccess)
                                    Text("Pre-Approved via PRAVA AI Wallet", fontSize = 11.sp, color = GuardianTextMuted)
                                }
                                PulseBadge(text = "PRAVA AUTOPAY READY", color = GuardianSuccess)
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("AI Recommendation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GuardianPrimary)
                                Text(
                                    "Book ambulance, reserve trauma ICU bed & trigger Prava Emergency AutoPay immediately.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Text(
                                    "⚠️ Patient unresponsive. AI will auto-authorize in 10 seconds.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GuardianDanger,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrimaryButton(
                        text = "Authorize Emergency & AutoPay",
                        onClick = onNavigateToCountdown,
                        containerColor = GuardianDanger
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. ACCIDENT DETECTED & COUNTDOWN SCREEN (SCREEN 4)
// -------------------------------------------------------------
@Composable
fun CountdownScreen(
    viewModel: EmergencyViewModel,
    onCountdownExpired: () -> Unit,
    onCancelled: () -> Unit
) {
    val secondsLeft by viewModel.countdownSeconds.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startEmergencyCountdown(onCountdownFinish = onCountdownExpired)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Header Alert Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(GuardianDanger.copy(alpha = 0.2f), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = GuardianDanger, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ACCIDENT DETECTED!", color = GuardianDanger, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Impact detected at 08:41 AM",
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Circular Countdown Timer
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .background(GuardianDanger.copy(alpha = 0.15f), CircleShape)
                        .border(4.dp, GuardianDanger, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${secondsLeft}s",
                            fontSize = 54.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Checking response...",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "No response? AI will automatically take action.",
                    fontSize = 13.sp,
                    color = GuardianAccent,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.cancelEmergencyCountdown()
                        onCancelled()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianSuccess)
                ) {
                    Text(
                        text = "I'm OK, Cancel Alert",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Button(
                    onClick = {
                        viewModel.cancelEmergencyCountdown()
                        onCountdownExpired()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GuardianDanger)
                ) {
                    Text(
                        text = "Patient Unconscious (Auto AI & AutoPay)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. HOSPITAL SELECTION SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalSelectionScreen(
    viewModel: EmergencyViewModel,
    onHospitalSelected: () -> Unit
) {
    val hospitals = viewModel.getAvailableHospitals()
    val selected by viewModel.selectedHospital.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Trauma Hospital", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(hospitals) { hospital ->
                    val isSelected = hospital.id == selected?.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectHospital(hospital) }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) GuardianPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) GuardianPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = hospital.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (hospital.isAiRecommended) {
                                    PulseBadge(text = "AI RECOMMENDED", color = GuardianPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${hospital.traumaLevel} • ${hospital.distanceKm} km • ${hospital.etaMinutes} min ETA",
                                fontSize = 13.sp,
                                color = GuardianTextMuted
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "ICU Beds: ${hospital.availableIcuBeds} open",
                                    fontSize = 12.sp,
                                    color = GuardianSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Deposit: $${hospital.baseAdmissionCost.toInt()}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Confirm Hospital",
                onClick = onHospitalSelected
            )
        }
    }
}

// -------------------------------------------------------------
// 5. AMBULANCE SELECTION SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmbulanceSelectionScreen(
    viewModel: EmergencyViewModel,
    onAmbulanceSelected: () -> Unit
) {
    val ambulances = viewModel.getAvailableAmbulances()
    val selected by viewModel.selectedAmbulance.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Ambulance Dispatch", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(ambulances) { ambulance ->
                    val isSelected = ambulance.id == selected?.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectAmbulance(ambulance) }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) GuardianAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) GuardianAccent.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ambulance.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (ambulance.isAiRecommended) {
                                    PulseBadge(text = "AI TOP DISPATCH", color = GuardianAccent)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${ambulance.type} • ${ambulance.etaMinutes} min ETA",
                                fontSize = 13.sp,
                                color = GuardianTextMuted
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Driver: ${ambulance.driverName} (${ambulance.vehiclePlate})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Fare: $${ambulance.price.toInt()} USD",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GuardianPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Confirm Ambulance",
                onClick = onAmbulanceSelected
            )
        }
    }
}

// -------------------------------------------------------------
// 6. COST ESTIMATION SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostEstimationScreen(
    viewModel: EmergencyViewModel,
    onProceedToPrava: () -> Unit
) {
    val hospital by viewModel.selectedHospital.collectAsState()
    val ambulance by viewModel.selectedAmbulance.collectAsState()

    val hospCost = hospital?.baseAdmissionCost ?: 3200.0
    val ambCost = ambulance?.price ?: 650.0
    val totalCost = hospCost + ambCost
    val insuranceCovered = totalCost * 0.80
    val finalPayable = totalCost - insuranceCovered

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Cost Breakdown", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GuardianCard {
                    Text(
                        text = "Itemized Emergency Statement",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ambulance Fare (${ambulance?.name ?: "ALS"})", color = GuardianTextMuted, fontSize = 13.sp)
                        Text("$${ambCost.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hospital ER Deposit (${hospital?.name ?: "Stanford"})", color = GuardianTextMuted, fontSize = 13.sp)
                        Text("$${hospCost.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("$${totalCost.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Blue Cross Insurance (80% Pre-Approval)", color = GuardianSuccess, fontSize = 13.sp)
                        Text("-$${insuranceCovered.toInt()}", color = GuardianSuccess, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GuardianPrimary.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GuardianPrimary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Final Payable via Prava AutoPay", fontSize = 13.sp, color = GuardianTextMuted)
                        Text(
                            text = "$${finalPayable.toInt()} USD",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = GuardianPrimary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "Pre-approved within your $5,000 Prava Emergency AutoPay limit.",
                            fontSize = 12.sp,
                            color = GuardianSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Authorize Prava AutoPay Settlement",
                onClick = onProceedToPrava
            )
        }
    }
}

// -------------------------------------------------------------
// -------------------------------------------------------------
// 7. PRAVA SANDBOX PAYMENT SCREEN (SCREEN 6)
// -------------------------------------------------------------
@Composable
fun PravaPaymentScreen(
    viewModel: EmergencyViewModel,
    onPaymentCompleted: () -> Unit
) {
    val stepIndex by viewModel.paymentStepIndex.collectAsState()
    val stepMessage by viewModel.paymentStepMessage.collectAsState()
    val hospital by viewModel.selectedHospital.collectAsState()
    val ambulance by viewModel.selectedAmbulance.collectAsState()

    val hospCost = hospital?.baseAdmissionCost ?: 3200.0
    val ambCost = ambulance?.price ?: 650.0
    val totalCost = hospCost + ambCost
    val insuranceCovered = totalCost * 0.80
    val finalPayable = totalCost - insuranceCovered

    LaunchedEffect(Unit) {
        viewModel.executePravaPayment(onPaymentSuccess = onPaymentCompleted)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(GuardianPrimary, GuardianSecondary)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SECURE PAYMENT WITH PRAVA AI",
                    color = GuardianAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Processing Emergency AutoPay",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Patient Unconscious • AI Executing Pre-Approved Payment",
                    color = GuardianSuccess,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Central Breakdown & Timeline Panel
            GlassCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Emergency Booking Breakdown", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ambulance Service", color = GuardianTextMuted, fontSize = 12.sp)
                        Text(ambulance?.name ?: "Advanced Life Support", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Hospital Reserved", color = GuardianTextMuted, fontSize = 12.sp)
                        Text(hospital?.name ?: "Apollo Hospital", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Payment Method", color = GuardianTextMuted, fontSize = 12.sp)
                        Text("PRAVA AI Wallet", color = GuardianAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Amount Settled", color = GuardianTextMuted, fontSize = 12.sp)
                        Text("₹850 / $85 USD", color = GuardianSuccess, fontSize = 15.sp, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x336C63FF)))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Timeline Steps
                    StepProgressTimeline(
                        currentStepIndex = stepIndex,
                        steps = listOf(
                            "Verifying Incident Certificate",
                            "Authorizing PRAVA Escrow",
                            "Processing Fast Settlement",
                            "Payment Successful"
                        ),
                        currentMessage = stepMessage
                    )
                }
            }

            Text(
                text = "🔒 This is a secure AI-initiated transaction • PRAVA Escrow #PRV8X24FG91",
                color = Color.LightGray,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// 8. PAYMENT SUCCESS SCREEN (SCREEN 7)
// -------------------------------------------------------------
@Composable
fun PaymentSuccessScreen(
    viewModel: EmergencyViewModel,
    onTrackLive: () -> Unit,
    onViewReport: () -> Unit
) {
    val tx by viewModel.activeTransaction.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuardianBackgroundDark)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(GuardianSuccess.copy(alpha = 0.2f), CircleShape)
                        .border(3.dp, GuardianSuccess, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GuardianSuccess,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ambulance Booked!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = "Payment Successful via PRAVA AI Wallet",
                    fontSize = 14.sp,
                    color = GuardianSuccess,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                GlassCard {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Transaction ID:", color = GuardianTextMuted, fontSize = 12.sp)
                            Text(tx?.transactionId ?: "PRV8X24FG91", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount Paid:", color = GuardianTextMuted, fontSize = 12.sp)
                            Text("₹850 / $85 USD", color = GuardianSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Time:", color = GuardianTextMuted, fontSize = 12.sp)
                            Text("08:41 AM", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Status:", color = GuardianTextMuted, fontSize = 12.sp)
                            Text("Success ✔️", color = GuardianSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(
                    text = "Track Live & View Details",
                    onClick = onTrackLive,
                    icon = Icons.Default.Navigation
                )

                SecondaryButton(
                    text = "View Booking Summary",
                    onClick = onViewReport,
                    borderColor = Color.White
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 9. LIVE TRACKING & DISPATCH DETAILS SCREEN (SCREENS 8, 9, 10, 11)
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit,
    onViewIncidentReport: () -> Unit
) {
    val hospital by viewModel.selectedHospital.collectAsState()
    val ambulance by viewModel.selectedAmbulance.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ambulance On The Way", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onViewIncidentReport) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = "Report", tint = GuardianPrimary)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Map Canvas
            item {
                SimulatedMapCanvas(
                    hospitalName = hospital?.name ?: "Apollo Hospital Emergency",
                    etaMinutes = ambulance?.etaMinutes ?: 5,
                    showAmbulanceRoute = true
                )
            }

            // Driver Card (Screen 8)
            item {
                GuardianCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Driver Assigned", fontSize = 12.sp, color = GuardianTextMuted)
                            Text("Rohit Sharma ⭐ 4.8", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text("Vehicle: ${ambulance?.vehiclePlate ?: "MH 12 AB 4321"} (${ambulance?.name ?: "ALS Unit"})", fontSize = 12.sp, color = GuardianTextMuted)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(GuardianSuccess.copy(alpha = 0.15f), CircleShape)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = GuardianSuccess, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Hospital Notified Card (Screen 9)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GuardianSuccess.copy(alpha = 0.12f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GuardianSuccess.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalHospital, contentDescription = null, tint = GuardianSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hospital Notified", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GuardianSuccess)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${hospital?.name ?: "Apollo Hospital"} Emergency Department has been notified.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Bed Reserved: ER - Bed 12", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GuardianPrimary)
                            Text("ETA: 08:52 AM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text("Hospital is prepared and ready for arrival.", fontSize = 11.sp, color = GuardianTextMuted, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            // Live Location Sharing with Contacts (Screen 10 & 11)
            item {
                GuardianCard {
                    Text("Emergency Contacts Notified", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text("Sharing live crash location with Mom, Dad, Rahul +2", fontSize = 12.sp, color = GuardianSuccess, modifier = Modifier.padding(top = 2.dp, bottom = 10.dp))

                    val defaultList = listOf("Mom", "Dad", "Rahul", "Sister")
                    defaultList.forEach { contactName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(contactName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            PulseBadge(text = "NOTIFIED ✔️", color = GuardianSuccess)
                        }
                    }
                }
            }

            // Action Button
            item {
                PrimaryButton(
                    text = "View Incident & Payment Summary",
                    onClick = onViewIncidentReport,
                    icon = Icons.Default.ReceiptLong
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 10. INCIDENT REPORT SCREEN
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentReportScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val telemetry by viewModel.telemetryState.collectAsState()
    val analysis by viewModel.currentAiAnalysis.collectAsState()
    val tx by viewModel.activeTransaction.collectAsState()
    val hospital by viewModel.selectedHospital.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Emergency Incident Report", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GuardianCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("INCIDENT #PRV-CRASH-991", fontWeight = FontWeight.Black, fontSize = 16.sp, color = GuardianPrimary)
                        PulseBadge(text = "VERIFIED", color = GuardianSuccess)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Location: ${telemetry.locationName}", fontSize = 13.sp, color = GuardianTextMuted)
                    Text("Collision Severity: ${analysis?.severityLevel ?: "CRITICAL"}", fontSize = 13.sp, color = GuardianDanger, fontWeight = FontWeight.Bold)
                    Text("Impact Force: ${telemetry.impactForceG} G", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            item {
                GuardianCard {
                    Text("OpenAI / Gemini Reasoning Transcript", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = analysis?.reasoningText ?: "Vehicle experienced high G-force deceleration. Guardian AI confidence is 97%. Optimal trauma center selected.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }

            item {
                GuardianCard {
                    Text("Prava Escrow Settlement Receipt", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Transaction ID: ${tx?.transactionId ?: "PRV-EMG-8812"}", fontSize = 12.sp, color = GuardianTextMuted)
                    Text("Hospital: ${hospital?.name ?: "Stanford Trauma Center"}", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("Amount Settled: $${tx?.netPayableAmount?.toInt() ?: 770}", fontSize = 14.sp, color = GuardianSuccess, fontWeight = FontWeight.Bold)
                }
            }

            item {
                PrimaryButton(
                    text = "Download Official Receipt (PDF)",
                    onClick = { /* Download receipt */ },
                    icon = Icons.Default.Download
                )
            }
        }
    }
}
