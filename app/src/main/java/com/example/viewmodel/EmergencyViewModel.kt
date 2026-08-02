package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.EmergencyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmergencyRepository(AppDatabase.getInstance(application))

    private val _userProfile = MutableStateFlow(repository.getUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _telemetryState = MutableStateFlow(TelemetryData())
    val telemetryState: StateFlow<TelemetryData> = _telemetryState.asStateFlow()

    private val _isJourneyActive = MutableStateFlow(false)
    val isJourneyActive: StateFlow<Boolean> = _isJourneyActive.asStateFlow()

    private val _journeyDurationSeconds = MutableStateFlow(0)
    val journeyDurationSeconds: StateFlow<Int> = _journeyDurationSeconds.asStateFlow()

    private val _isAiAnalyzing = MutableStateFlow(false)
    val isAiAnalyzing: StateFlow<Boolean> = _isAiAnalyzing.asStateFlow()

    private val _currentAiAnalysis = MutableStateFlow<AiEmergencyAnalysis?>(null)
    val currentAiAnalysis: StateFlow<AiEmergencyAnalysis?> = _currentAiAnalysis.asStateFlow()

    private val _countdownSeconds = MutableStateFlow(10)
    val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()

    private val _selectedHospital = MutableStateFlow<Hospital?>(null)
    val selectedHospital: StateFlow<Hospital?> = _selectedHospital.asStateFlow()

    private val _selectedAmbulance = MutableStateFlow<Ambulance?>(null)
    val selectedAmbulance: StateFlow<Ambulance?> = _selectedAmbulance.asStateFlow()

    private val _paymentStepIndex = MutableStateFlow(0)
    val paymentStepIndex: StateFlow<Int> = _paymentStepIndex.asStateFlow()

    private val _paymentStepMessage = MutableStateFlow("Initializing Prava Emergency AutoPay Sandbox...")
    val paymentStepMessage: StateFlow<String> = _paymentStepMessage.asStateFlow()

    private val _isPaymentProcessing = MutableStateFlow(false)
    val isPaymentProcessing: StateFlow<Boolean> = _isPaymentProcessing.asStateFlow()

    private val _activeTransaction = MutableStateFlow<PaymentTransaction?>(null)
    val activeTransaction: StateFlow<PaymentTransaction?> = _activeTransaction.asStateFlow()

    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> = _contacts.asStateFlow()

    private val _incidents = MutableStateFlow<List<IncidentEntity>>(emptyList())
    val incidents: StateFlow<List<IncidentEntity>> = _incidents.asStateFlow()

    private val _trips = MutableStateFlow<List<TripEntity>>(emptyList())
    val trips: StateFlow<List<TripEntity>> = _trips.asStateFlow()

    private val _autoPayConfig = MutableStateFlow<AutoPayConfigEntity?>(null)
    val autoPayConfig: StateFlow<AutoPayConfigEntity?> = _autoPayConfig.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions.asStateFlow()

    private val _paymentMethod = MutableStateFlow<PaymentMethodEntity?>(null)
    val paymentMethod: StateFlow<PaymentMethodEntity?> = _paymentMethod.asStateFlow()

    private val _smsLogs = MutableStateFlow<List<SmsHistoryEntity>>(emptyList())
    val smsLogs: StateFlow<List<SmsHistoryEntity>> = _smsLogs.asStateFlow()

    private val _transactionSearchQuery = MutableStateFlow("")
    val transactionSearchQuery: StateFlow<String> = _transactionSearchQuery.asStateFlow()

    private val _transactionStatusFilter = MutableStateFlow("ALL") // "ALL", "COMPLETED", "PENDING", "FAILED"
    val transactionStatusFilter: StateFlow<String> = _transactionStatusFilter.asStateFlow()

    private val _selectedTransactionDetail = MutableStateFlow<TransactionEntity?>(null)
    val selectedTransactionDetail: StateFlow<TransactionEntity?> = _selectedTransactionDetail.asStateFlow()

    private var journeyTimerJob: Job? = null
    private var countdownJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initDefaultDataIfEmpty()
        }
        viewModelScope.launch {
            repository.contacts.collect { _contacts.value = it }
        }
        viewModelScope.launch {
            repository.incidents.collect { _incidents.value = it }
        }
        viewModelScope.launch {
            repository.trips.collect { _trips.value = it }
        }
        viewModelScope.launch {
            repository.autoPayConfig.collect { _autoPayConfig.value = it }
        }
        viewModelScope.launch {
            repository.transactions.collect { _transactions.value = it }
        }
        viewModelScope.launch {
            repository.paymentMethod.collect { _paymentMethod.value = it }
        }
        viewModelScope.launch {
            repository.smsLogs.collect { _smsLogs.value = it }
        }
    }

    fun setTransactionSearchQuery(query: String) {
        _transactionSearchQuery.value = query
    }

    fun setTransactionStatusFilter(filter: String) {
        _transactionStatusFilter.value = filter
    }

    fun selectTransactionDetail(tx: TransactionEntity?) {
        _selectedTransactionDetail.value = tx
    }

    fun startJourney() {
        _isJourneyActive.value = true
        _journeyDurationSeconds.value = 0
        journeyTimerJob?.cancel()
        journeyTimerJob = viewModelScope.launch {
            while (_isJourneyActive.value) {
                delay(1000)
                _journeyDurationSeconds.value += 1
                // Fluctuate telematics subtly to simulate active vehicle motion
                val speed = 62 + ((-3..4).random())
                _telemetryState.value = _telemetryState.value.copy(
                    speedMph = speed,
                    impactForceG = 1.0f + ((0..3).random() * 0.1f)
                )
            }
        }
    }

    fun stopJourney() {
        _isJourneyActive.value = false
        journeyTimerJob?.cancel()
    }

    fun triggerSimulatedAccident(onAnalysisComplete: () -> Unit) {
        viewModelScope.launch {
            _isAiAnalyzing.value = true
            // Update telemetry to severe collision values
            _telemetryState.value = TelemetryData(
                speedMph = 68,
                impactForceG = 38.4f,
                decelerationG = 14.8f,
                rollAngleDeg = 42,
                driverUnresponsiveSeconds = 18,
                locationName = "I-280 Mile Marker 42, Palo Alto, CA"
            )

            val analysis = repository.analyzeEmergency(_telemetryState.value)
            _currentAiAnalysis.value = analysis
            _selectedHospital.value = analysis.recommendedHospital
            _selectedAmbulance.value = analysis.recommendedAmbulance
            _isAiAnalyzing.value = false

            onAnalysisComplete()
        }
    }

    fun startEmergencyCountdown(onCountdownFinish: () -> Unit) {
        _countdownSeconds.value = 10
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_countdownSeconds.value > 0) {
                delay(1000)
                _countdownSeconds.value -= 1
            }
            onCountdownFinish()
        }
    }

    fun cancelEmergencyCountdown() {
        countdownJob?.cancel()
        _countdownSeconds.value = 10
    }

    fun selectHospital(hospital: Hospital) {
        _selectedHospital.value = hospital
    }

    fun selectAmbulance(ambulance: Ambulance) {
        _selectedAmbulance.value = ambulance
    }

    fun executePravaPayment(onPaymentSuccess: () -> Unit) {
        val hospital = _selectedHospital.value ?: repository.getHospitals().first()
        val ambulance = _selectedAmbulance.value ?: repository.getAmbulances().first()

        val totalCost = hospital.baseAdmissionCost + ambulance.price
        val insuranceDiscount = totalCost * 0.80 // 80% coverage
        val payableAmount = totalCost - insuranceDiscount

        viewModelScope.launch {
            _isPaymentProcessing.value = true
            _paymentStepIndex.value = 0
            _paymentStepMessage.value = "Connecting to Prava Emergency AutoPay..."

            val tx = repository.executePravaAutoPay(
                hospital = hospital,
                ambulance = ambulance,
                totalCost = totalCost,
                insuranceCoverage = insuranceDiscount,
                payableAmount = payableAmount,
                onProgressUpdate = { index, msg ->
                    _paymentStepIndex.value = index
                    _paymentStepMessage.value = msg
                }
            )

            _activeTransaction.value = tx
            _isPaymentProcessing.value = false
            onPaymentSuccess()
        }
    }

    fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        viewModelScope.launch {
            repository.addContact(name, phone, relationship, isPrimary)
        }
    }

    fun deleteContact(id: Long) {
        viewModelScope.launch {
            repository.deleteContact(id)
        }
    }

    fun updateAutoPaySettings(isEnabled: Boolean, limit: Double, cardLast4: String) {
        viewModelScope.launch {
            repository.updateAutoPayConfig(isEnabled, limit, cardLast4)
        }
    }

    fun getAvailableHospitals(): List<Hospital> = repository.getHospitals()
    fun getAvailableAmbulances(): List<Ambulance> = repository.getAmbulances()
}
