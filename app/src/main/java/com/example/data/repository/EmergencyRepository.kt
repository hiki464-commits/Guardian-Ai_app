package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.AutoPayConfigEntity
import com.example.data.local.ContactEntity
import com.example.data.local.IncidentEntity
import com.example.data.local.PaymentMethodEntity
import com.example.data.local.SmsHistoryEntity
import com.example.data.local.TransactionEntity
import com.example.data.local.TripEntity
import com.example.data.model.AiEmergencyAnalysis
import com.example.data.model.Ambulance
import com.example.data.model.Hospital
import com.example.data.model.PaymentTransaction
import com.example.data.model.TelemetryData
import com.example.data.model.UserProfile
import com.example.data.remote.AiReasoningEngine
import com.example.data.remote.LinqService
import com.example.data.remote.MockPravaSandboxService
import com.example.data.remote.PaymentService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class EmergencyRepository(
    private val database: AppDatabase,
    private val aiEngine: AiReasoningEngine = AiReasoningEngine(),
    private val paymentService: PaymentService = MockPravaSandboxService(),
    private val linqService: LinqService = LinqService()
) {

    val contacts: Flow<List<ContactEntity>> = database.contactDao().getAllContacts()
    val incidents: Flow<List<IncidentEntity>> = database.incidentDao().getAllIncidents()
    val trips: Flow<List<TripEntity>> = database.tripDao().getAllTrips()
    val autoPayConfig: Flow<AutoPayConfigEntity?> = database.autoPayDao().getAutoPayConfig()
    val transactions: Flow<List<TransactionEntity>> = database.transactionDao().getAllTransactions()
    val paymentMethod: Flow<PaymentMethodEntity?> = database.paymentMethodDao().getDefaultPaymentMethod()
    val smsLogs: Flow<List<SmsHistoryEntity>> = database.smsDao().getAllSmsLogs()

    suspend fun initDefaultDataIfEmpty() {
        val currentContacts = database.contactDao().getAllContacts().first()
        if (currentContacts.isEmpty()) {
            database.contactDao().insertContact(
                ContactEntity(
                    name = "Heet Kale",
                    phone = "+1 (555) 462-2192",
                    relationship = "Primary Emergency Contact",
                    isPrimary = true
                )
            )
            database.contactDao().insertContact(
                ContactEntity(
                    name = "Sarah Rivera (Spouse)",
                    phone = "+1 (555) 234-5678",
                    relationship = "Family Contact",
                    isPrimary = false
                )
            )
            database.contactDao().insertContact(
                ContactEntity(
                    name = "Dr. Robert Vance (Family Doctor)",
                    phone = "+1 (555) 987-6543",
                    relationship = "Physician",
                    isPrimary = false
                )
            )
        }

        val currentSmsLogs = database.smsDao().getAllSmsLogs().first()
        if (currentSmsLogs.isEmpty()) {
            database.smsDao().insertSmsLog(
                SmsHistoryEntity(
                    recipientName = "Heet Kale",
                    recipientPhone = "+1 (555) 462-2192",
                    message = "🚨 CRITICAL ACCIDENT ALERT [Linq Messaging Service]: Crash detected at I-280 Palo Alto. Pre-hospital trauma bed reserved at Stanford Health Trauma Center. ALS Emergency Unit 42 dispatched (ETA: 4 mins). Prava Escrow AutoPay $850.00 Pre-Authorized.",
                    status = "DELIVERED (Linq iMessage)",
                    timestamp = System.currentTimeMillis() - 86400000L
                )
            )
        }

        val currentAutoPay = database.autoPayDao().getAutoPayConfig().first()
        if (currentAutoPay == null) {
            database.autoPayDao().saveAutoPayConfig(
                AutoPayConfigEntity(
                    isEnabled = true,
                    limitAmount = 5000.0,
                    paymentMethodName = "Prava Express Direct Sandbox",
                    cardLast4 = "2192",
                    preAuthId = "PRAVA-ATH-994812"
                )
            )
        }

        val currentPm = database.paymentMethodDao().getDefaultPaymentMethod().first()
        if (currentPm == null) {
            database.paymentMethodDao().savePaymentMethod(
                PaymentMethodEntity(
                    id = "CARD-02",
                    holderName = "Heet Kale",
                    cardNumber = "4622943123232192",
                    cardLast4 = "2192",
                    cvv = "652",
                    expiryMonth = "12",
                    expiryYear = "30",
                    brand = "VISA / Prava Sandbox",
                    isDefault = true,
                    isAutoPayEnabled = true,
                    limitAmount = 5000.0,
                    dailyTransactionLimit = 30,
                    connectionStatus = "CONNECTED"
                )
            )
        }

        val currentTxs = database.transactionDao().getAllTransactions().first()
        if (currentTxs.isEmpty()) {
            database.transactionDao().insertTransaction(
                TransactionEntity(
                    transactionId = "PRV-EMG-88192A",
                    merchant = "Prava Emergency Health Escrow Sandbox",
                    hospitalName = "Stanford Health Trauma Center",
                    ambulanceName = "ALS Emergency Unit 42",
                    totalAmount = 3970.0,
                    insuranceCoverage = 3120.0,
                    netPayableAmount = 850.0,
                    status = "COMPLETED",
                    timestamp = System.currentTimeMillis() - 86400000L,
                    preAuthId = "PRAVA-ATH-994812",
                    paymentMethod = "Prava AI Wallet"
                )
            )
        }

        val currentTrips = database.tripDao().getAllTrips().first()
        if (currentTrips.isEmpty()) {
            database.tripDao().insertTrip(
                TripEntity(
                    id = "trip_101",
                    startAddress = "Home (Palo Alto)",
                    endAddress = "Office (Downtown SF)",
                    startTime = System.currentTimeMillis() - 86400000L,
                    endTime = System.currentTimeMillis() - 82800000L,
                    distanceKm = 42.5,
                    safetyScore = 98,
                    status = "COMPLETED"
                )
            )
        }
    }

    suspend fun analyzeEmergency(telemetry: TelemetryData): AiEmergencyAnalysis {
        return aiEngine.analyzeEmergency(telemetry)
    }

    suspend fun executePravaAutoPay(
        hospital: Hospital,
        ambulance: Ambulance,
        totalCost: Double,
        insuranceCoverage: Double,
        payableAmount: Double,
        onProgressUpdate: (stepIndex: Int, stepName: String) -> Unit
    ): PaymentTransaction {
        val currentConfig = database.autoPayDao().getAutoPayConfig().first()
        val preAuth = currentConfig?.preAuthId ?: "PRAVA-ATH-994812"

        val tx = paymentService.authorizeEmergencyAutoPay(
            hospitalName = hospital.name,
            ambulanceName = ambulance.name,
            totalCost = totalCost,
            insuranceCoverage = insuranceCoverage,
            payableAmount = payableAmount,
            preAuthId = preAuth,
            onProgressUpdate = onProgressUpdate
        )

        // Store transaction locally in Room Database
        database.transactionDao().insertTransaction(
            TransactionEntity(
                transactionId = tx.transactionId,
                merchant = tx.merchant,
                hospitalName = hospital.name,
                ambulanceName = ambulance.name,
                totalAmount = totalCost,
                insuranceCoverage = insuranceCoverage,
                netPayableAmount = payableAmount,
                status = tx.status,
                timestamp = tx.timestamp,
                preAuthId = preAuth,
                paymentMethod = "Prava AI Wallet"
            )
        )

        // Save incident report locally
        database.incidentDao().insertIncident(
            IncidentEntity(
                id = tx.transactionId,
                timestamp = tx.timestamp,
                locationName = "I-280 Mile Marker 42, Palo Alto",
                latitude = 37.4220,
                longitude = -122.0841,
                severity = "CRITICAL (Level 1 Trauma)",
                confidence = 97,
                aiReasoning = "38.4G collision at 68mph. Driver unresponsive. Auto-reserve trauma bed & dispatch ambulance.",
                hospitalName = hospital.name,
                ambulanceType = ambulance.name,
                totalCost = totalCost,
                insuranceCovered = insuranceCoverage,
                payableAmount = payableAmount,
                transactionId = tx.transactionId,
                status = "RESOLVED & DISPATCHED"
            )
        )

        // Trigger Linq Emergency SMS Notifications to Emergency Contacts
        val contactList = database.contactDao().getAllContacts().first()
        for (c in contactList) {
            val smsLog = linqService.sendEmergencySms(
                recipientName = c.name,
                recipientPhone = c.phone,
                hospitalName = hospital.name,
                ambulanceName = ambulance.name,
                etaMinutes = ambulance.etaMinutes,
                transactionStatus = tx.status,
                locationName = "I-280 Palo Alto"
            )
            database.smsDao().insertSmsLog(smsLog)
        }

        return tx
    }

    suspend fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        database.contactDao().insertContact(
            ContactEntity(name = name, phone = phone, relationship = relationship, isPrimary = isPrimary)
        )
    }

    suspend fun deleteContact(id: Long) {
        database.contactDao().deleteContact(id)
    }

    suspend fun updateAutoPayConfig(isEnabled: Boolean, limitAmount: Double, cardLast4: String) {
        database.autoPayDao().saveAutoPayConfig(
            AutoPayConfigEntity(
                isEnabled = isEnabled,
                limitAmount = limitAmount,
                paymentMethodName = "Prava Express Direct Sandbox",
                cardLast4 = cardLast4,
                preAuthId = "PRAVA-ATH-" + UUID.randomUUID().toString().take(6).uppercase()
            )
        )
        database.paymentMethodDao().savePaymentMethod(
            PaymentMethodEntity(
                id = "pm_prava_default",
                holderName = "Alex Rivera",
                cardLast4 = cardLast4,
                expiryMonth = "12",
                expiryYear = "28",
                brand = "VISA",
                isDefault = true,
                isAutoPayEnabled = isEnabled,
                limitAmount = limitAmount,
                connectionStatus = "CONNECTED"
            )
        )
    }

    fun getHospitals(): List<Hospital> = aiEngine.defaultHospitals
    fun getAmbulances(): List<Ambulance> = aiEngine.defaultAmbulances
    fun getUserProfile(): UserProfile = UserProfile()
}
