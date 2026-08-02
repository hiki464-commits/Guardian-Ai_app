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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GuardianCard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.PulseBadge
import com.example.ui.theme.GuardianAccent
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSecondary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextMuted
import com.example.viewmodel.EmergencyViewModel

@Composable
fun DashboardScreen(
    viewModel: EmergencyViewModel,
    onStartJourney: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToAutoPay: () -> Unit,
    onNavigateToIncidents: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isJourneyActive by viewModel.isJourneyActive.collectAsState()
    val autoPayConfig by viewModel.autoPayConfig.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val incidents by viewModel.incidents.collectAsState()
    val trips by viewModel.trips.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${userProfile.name.split(" ").first()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Guardian AI Protection Active",
                        fontSize = 13.sp,
                        color = GuardianSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(GuardianPrimary.copy(alpha = 0.15f), CircleShape)
                        .clickable { onNavigateToProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = GuardianPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Start Journey Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GuardianPrimary.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GuardianPrimary, GuardianSecondary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PulseBadge(
                                text = if (isJourneyActive) "MONITORING LIVE" else "READY TO MONITOR",
                                color = if (isJourneyActive) GuardianSuccess else Color.White
                            )

                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (isJourneyActive) "Active Journey Guard" else "Start Journey Safeguard",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Real-time AI collision detection, instant trauma hospital pre-reservations & Prava AutoPay.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                        )

                        PrimaryButton(
                            text = if (isJourneyActive) "View Active Telematics" else "Start Journey",
                            onClick = onStartJourney,
                            containerColor = Color.White,
                            contentColor = GuardianPrimary,
                            icon = Icons.Default.Navigation
                        )
                    }
                }
            }
        }

        // Emergency AutoPay & Insurance Status Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // AutoPay Card
                GuardianCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAutoPay() },
                    elevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(GuardianPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = GuardianPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = GuardianTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Prava AutoPay",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "$${autoPayConfig?.limitAmount?.toInt() ?: 5000} Limit Authorized",
                        fontSize = 12.sp,
                        color = GuardianSuccess,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Insurance Card
                GuardianCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToProfile() },
                    elevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(GuardianAccent.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = GuardianAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = GuardianTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Health Insurance",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Blue Cross (80%)",
                        fontSize = 12.sp,
                        color = GuardianAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Emergency Contacts Section
        item {
            GuardianCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToContacts() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ContactPhone,
                            contentDescription = null,
                            tint = GuardianPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Emergency Contacts (${contacts.size})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = GuardianTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (contacts.isEmpty()) {
                    Text(
                        text = "No emergency contacts configured yet. Tap to add.",
                        fontSize = 13.sp,
                        color = GuardianTextMuted
                    )
                } else {
                    contacts.take(2).forEach { contact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = contact.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = contact.relationship,
                                fontSize = 12.sp,
                                color = GuardianTextMuted
                            )
                        }
                    }
                }
            }
        }

        // Recent Trips Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Recent Trips Guarded",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (trips.isEmpty()) {
                    Text("No past trips logged.", fontSize = 13.sp, color = GuardianTextMuted)
                } else {
                    trips.take(2).forEach { trip ->
                        GuardianCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${trip.startAddress} ➔ ${trip.endAddress}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${trip.distanceKm} km • Safety Score ${trip.safetyScore}%",
                                        fontSize = 12.sp,
                                        color = GuardianSuccess
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = GuardianTextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Incident Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Incident Logs",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (incidents.isNotEmpty()) {
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            color = GuardianPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToIncidents() }
                        )
                    }
                }

                if (incidents.isEmpty()) {
                    GuardianCard {
                        Text(
                            text = "No emergency incidents recorded. Vehicle is safe.",
                            fontSize = 13.sp,
                            color = GuardianTextMuted
                        )
                    }
                } else {
                    incidents.take(1).forEach { incident ->
                        GuardianCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToIncidents() }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = incident.severity,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GuardianDanger
                                    )
                                    Text(
                                        text = incident.hospitalName,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "TX: ${incident.transactionId} • Settled $${incident.payableAmount}",
                                        fontSize = 11.sp,
                                        color = GuardianTextMuted
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = GuardianTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
