package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GuardianCard
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.GuardianAccent
import com.example.ui.theme.GuardianDanger
import com.example.ui.theme.GuardianPrimary
import com.example.ui.theme.GuardianSuccess
import com.example.ui.theme.GuardianTextMuted
import com.example.viewmodel.EmergencyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit,
    onNavigateToAutoPay: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToWallet: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Protection Profile", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                GuardianCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(GuardianPrimary.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = GuardianPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userProfile.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = userProfile.email,
                                fontSize = 13.sp,
                                color = GuardianTextMuted
                            )
                            Text(
                                text = userProfile.phone,
                                fontSize = 13.sp,
                                color = GuardianPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Medical Information Card
            item {
                GuardianCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = GuardianDanger,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Medical Profile",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Blood Group: ${userProfile.bloodGroup}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = userProfile.medicalNotes,
                        fontSize = 13.sp,
                        color = GuardianTextMuted
                    )
                }
            }

            // Insurance Policy Card
            item {
                GuardianCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            tint = GuardianAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Health Insurance",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userProfile.insuranceProvider,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Policy: ${userProfile.policyNumber}",
                        fontSize = 12.sp,
                        color = GuardianTextMuted
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Coverage: 80% Emergency Trauma Pre-Approval",
                        fontSize = 12.sp,
                        color = GuardianSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Action Quick Links
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GuardianCard(
                        modifier = Modifier.clickable { onNavigateToAutoPay() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Payment, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Emergency AutoPay Setup", fontWeight = FontWeight.Bold)
                            }
                            Icon(Icons.Default.Lock, contentDescription = null, tint = GuardianSuccess)
                        }
                    }

                    GuardianCard(
                        modifier = Modifier.clickable { onNavigateToWallet() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Prava Sandbox Wallet", fontWeight = FontWeight.Bold)
                            }
                            Text("Manage Card", color = GuardianPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    GuardianCard(
                        modifier = Modifier.clickable { onNavigateToHistory() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Escrow Payment History", fontWeight = FontWeight.Bold)
                            }
                            Text("View Receipts", color = GuardianPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    GuardianCard(
                        modifier = Modifier.clickable { onNavigateToContacts() }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ContactPhone, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Emergency Contacts", fontWeight = FontWeight.Bold)
                            }
                            Text("Manage", color = GuardianPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoPaySetupScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val autoPayConfig by viewModel.autoPayConfig.collectAsState()
    var isEnabled by remember(autoPayConfig) { mutableStateOf(autoPayConfig?.isEnabled ?: true) }
    var limitSlider by remember(autoPayConfig) { mutableFloatStateOf(autoPayConfig?.limitAmount?.toFloat() ?: 5000f) }
    var cardLast4 by remember(autoPayConfig) { mutableStateOf(autoPayConfig?.cardLast4 ?: "8892") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prava Emergency AutoPay", fontWeight = FontWeight.Bold) },
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GuardianPrimary.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GuardianPrimary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Prava Sandbox Mandate", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }

                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { isEnabled = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pre-authorizes instant escrow settlement for ambulances and Level 1 trauma deposits when AI verifies a critical crash.",
                            fontSize = 13.sp,
                            color = GuardianTextMuted
                        )
                    }
                }

                GuardianCard {
                    Text(
                        text = "Emergency Pre-authorization Limit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${limitSlider.toInt()} USD",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = GuardianPrimary
                    )

                    Slider(
                        value = limitSlider,
                        onValueChange = { limitSlider = it },
                        valueRange = 1000f..10000f,
                        steps = 18,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = "Funds are reserved in Prava Sandbox Escrow only when Guardian AI confirms an emergency.",
                        fontSize = 12.sp,
                        color = GuardianTextMuted
                    )
                }

                GuardianCard {
                    Text(
                        text = "Prava AI Sandbox API Integration",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Real Prava Sandbox HTTP requests are enabled via OkHttp & Secrets Gradle Plugin.",
                        fontSize = 12.sp,
                        color = GuardianTextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🔑 PRAVA_API_KEY Configured:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GuardianPrimary
                            )
                            Text(
                                text = "To use your custom Prava API Key, open the Secrets panel in AI Studio and add 'PRAVA_API_KEY' or enter it in code/build config.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                GuardianCard {
                    Text(
                        text = "Linked Payment Card",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = cardLast4,
                        onValueChange = { cardLast4 = it.take(4) },
                        label = { Text("Card Last 4 Digits") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            PrimaryButton(
                text = "Save AutoPay Authorization",
                onClick = {
                    viewModel.updateAutoPaySettings(isEnabled, limitSlider.toDouble(), cardLast4)
                    onBack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val contacts by viewModel.contacts.collectAsState()
    val smsLogs by viewModel.smsLogs.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    var nameInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var relInput by remember { mutableStateOf("") }
    var isPrimaryInput by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Contacts & Linq SMS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Contact", tint = GuardianPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                Text(
                    text = "Designated Emergency Contacts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = GuardianPrimary
                )
            }

            items(contacts) { contact ->
                GuardianCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = contact.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (contact.isPrimary) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "PRIMARY",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GuardianPrimary,
                                        modifier = Modifier
                                            .background(GuardianPrimary.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${contact.relationship} • ${contact.phone}",
                                fontSize = 13.sp,
                                color = GuardianTextMuted,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        IconButton(onClick = { viewModel.deleteContact(contact.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = GuardianDanger)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Linq iMessage & SMS Alert History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GuardianPrimary
                    )
                    Surface(
                        color = GuardianSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Linq v2.4 Active",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GuardianSuccess,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            if (smsLogs.isEmpty()) {
                item {
                    Text(
                        text = "No emergency SMS alerts dispatched yet.",
                        color = GuardianTextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(smsLogs) { sms ->
                    GuardianCard {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "To: ${sms.recipientName} (${sms.recipientPhone})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    color = GuardianSuccess.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = sms.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GuardianSuccess,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = sms.message,
                                fontSize = 12.sp,
                                color = GuardianTextMuted,
                                lineHeight = 16.sp
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Log ID: LNQ-LOG-${sms.id}",
                                    fontSize = 10.sp,
                                    color = GuardianPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Linq iMessage Service v2.4",
                                    fontSize = 10.sp,
                                    color = GuardianTextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Emergency Contact") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Name") }
                        )
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text("Phone Number") }
                        )
                        OutlinedTextField(
                            value = relInput,
                            onValueChange = { relInput = it },
                            label = { Text("Relationship") }
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isPrimaryInput,
                                onCheckedChange = { isPrimaryInput = it }
                            )
                            Text("Set as primary contact", fontSize = 13.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (nameInput.isNotEmpty() && phoneInput.isNotEmpty()) {
                            viewModel.addContact(nameInput, phoneInput, relInput.ifEmpty { "Family" }, isPrimaryInput)
                            nameInput = ""
                            phoneInput = ""
                            relInput = ""
                            showAddDialog = false
                        }
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val sampleNotifications = listOf(
        Pair("Prava AutoPay Pre-authorization Active", "Sandbox mandate #PRAVA-ATH-994812 active up to $5,000."),
        Pair("Journey Protection Online", "Accelerometer & G-force collision algorithms loaded."),
        Pair("Emergency Contacts Verified", "Sarah Rivera (+1 555-234-5678) set as primary SMS dispatch.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Dispatch Alerts", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleNotifications) { (title, body) ->
                GuardianCard {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = GuardianPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(body, color = GuardianTextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val searchQuery by viewModel.transactionSearchQuery.collectAsState()
    val filterStatus by viewModel.transactionStatusFilter.collectAsState()
    val selectedDetail by viewModel.selectedTransactionDetail.collectAsState()

    val filteredTransactions = remember(transactions, searchQuery, filterStatus) {
        transactions.filter { tx ->
            val matchesSearch = searchQuery.isBlank() ||
                    tx.hospitalName.contains(searchQuery, ignoreCase = true) ||
                    tx.merchant.contains(searchQuery, ignoreCase = true) ||
                    tx.transactionId.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (filterStatus) {
                "COMPLETED" -> tx.status.equals("COMPLETED", ignoreCase = true) || tx.status.equals("APPROVED", ignoreCase = true)
                "PENDING" -> tx.status.equals("PENDING", ignoreCase = true) || tx.status.equals("PROCESSING", ignoreCase = true)
                "FAILED" -> tx.status.equals("FAILED", ignoreCase = true) || tx.status.equals("DECLINED", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prava Escrow Transactions", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setTransactionSearchQuery(it) },
                placeholder = { Text("Search hospital, merchant, or TX ID...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ALL", "COMPLETED", "PENDING", "FAILED").forEach { status ->
                    val isSelected = filterStatus == status
                    Surface(
                        modifier = Modifier.clickable { viewModel.setTransactionStatusFilter(status) },
                        color = if (isSelected) GuardianPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = status,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No transactions found matching criteria.",
                        color = GuardianTextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredTransactions) { tx ->
                        GuardianCard(
                            modifier = Modifier.clickable { viewModel.selectTransactionDetail(tx) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.hospitalName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${tx.merchant} • ${tx.ambulanceName}",
                                        fontSize = 12.sp,
                                        color = GuardianTextMuted,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Text(
                                        text = "TX ID: ${tx.transactionId}",
                                        fontSize = 11.sp,
                                        color = GuardianPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format("%.2f", tx.netPayableAmount)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = GuardianSuccess
                                    )
                                    Surface(
                                        color = when (tx.status.uppercase()) {
                                            "COMPLETED", "APPROVED" -> GuardianSuccess.copy(alpha = 0.15f)
                                            "FAILED", "DECLINED" -> GuardianDanger.copy(alpha = 0.15f)
                                            else -> GuardianAccent.copy(alpha = 0.15f)
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = tx.status,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (tx.status.uppercase()) {
                                                "COMPLETED", "APPROVED" -> GuardianSuccess
                                                "FAILED", "DECLINED" -> GuardianDanger
                                                else -> GuardianAccent
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Transaction Receipt Detail Modal
        selectedDetail?.let { tx ->
            AlertDialog(
                onDismissRequest = { viewModel.selectTransactionDetail(null) },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Prava Escrow Receipt", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GuardianSuccess)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = tx.hospitalName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GuardianPrimary
                        )
                        Text(
                            text = "Ambulance: ${tx.ambulanceName}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Estimated Cost:", fontSize = 12.sp, color = GuardianTextMuted)
                            Text("$${String.format("%.2f", tx.totalAmount)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Insurance Coverage (80%):", fontSize = 12.sp, color = GuardianTextMuted)
                            Text("-$${String.format("%.2f", tx.insuranceCoverage)}", fontSize = 12.sp, color = GuardianSuccess, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Prava Escrow Fee:", fontSize = 12.sp, color = GuardianTextMuted)
                            Text("$0.00 (Waived)", fontSize = 12.sp, color = GuardianPrimary, fontWeight = FontWeight.SemiBold)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Net Paid via Prava AutoPay:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("$${String.format("%.2f", tx.netPayableAmount)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GuardianSuccess)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Pre-Authorization ID: ${tx.preAuthId}", fontSize = 11.sp, color = GuardianTextMuted)
                        Text("Merchant: ${tx.merchant}", fontSize = 11.sp, color = GuardianTextMuted)
                        Text("Payment Method: ${tx.paymentMethod}", fontSize = 11.sp, color = GuardianTextMuted)
                    }
                },
                confirmButton = {
                    Button(onClick = { viewModel.selectTransactionDetail(null) }) {
                        Text("Close Receipt")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SandboxWalletScreen(
    viewModel: EmergencyViewModel,
    onBack: () -> Unit
) {
    val pm by viewModel.paymentMethod.collectAsState()
    val autoPayConfig by viewModel.autoPayConfig.collectAsState()
    var showFullCardNumber by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prava Sandbox Wallet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Virtual Credit Card Visual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
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
                        Column {
                            Text(
                                text = "Prava Emergency Virtual Card",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Card ID: ${pm?.id ?: "CARD-02"}",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "SANDBOX",
                            color = GuardianAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(GuardianAccent.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showFullCardNumber) "4622  9431  2323  2192" else "••••  ••••  ••••  ${pm?.cardLast4 ?: "2192"}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        IconButton(onClick = { showFullCardNumber = !showFullCardNumber }) {
                            Icon(
                                if (showFullCardNumber) Icons.Default.Security else Icons.Default.Lock,
                                contentDescription = "Toggle Card Number",
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CARD HOLDER", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                            Text(pm?.holderName ?: "Heet Kale", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("EXPIRES", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                            Text("${pm?.expiryMonth ?: "12"}/${pm?.expiryYear ?: "30"}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("CVV", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                            Text(pm?.cvv ?: "652", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            GuardianCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Prava API Gateway", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("https://sandbox.api.prava.space", fontSize = 12.sp, color = GuardianTextMuted)
                    }
                    Surface(
                        color = GuardianSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "● LIVE CONNECTED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GuardianSuccess,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            GuardianCard {
                Text("Sandbox Quotas & Limits", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily Sandbox Limit:", fontSize = 12.sp, color = GuardianTextMuted)
                    Text("30 Transactions / Day", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GuardianPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Pre-authorized Spending Limit:", fontSize = 12.sp, color = GuardianTextMuted)
                    Text("$${String.format("%.0f", autoPayConfig?.limitAmount ?: 5000.0)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GuardianSuccess)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Pre-authorization ID:", fontSize = 12.sp, color = GuardianTextMuted)
                    Text(autoPayConfig?.preAuthId ?: "PRAVA-ATH-994812", fontSize = 11.sp, color = GuardianTextMuted)
                }
            }

            GuardianCard {
                Text("API Secret Credentials", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Publishable Key: pk_test_7aQ...03p0", fontSize = 11.sp, color = GuardianTextMuted)
                Text("Secret Key: sk_test_72e...MBg", fontSize = 11.sp, color = GuardianTextMuted)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var simulateSensitivity by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GuardianCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Crash Detection Sensitivity", fontWeight = FontWeight.Bold)
                        Text("High force threshold (12G+)", fontSize = 12.sp, color = GuardianTextMuted)
                    }
                    Switch(checked = simulateSensitivity, onCheckedChange = { simulateSensitivity = it })
                }
            }

            GuardianCard {
                Text("App Version: Guardian AI 1.0 (Hackathon Release)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Prava Sandbox API Integration Connected", color = GuardianSuccess, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
