package com.example.data.model

data class TelemetryData(
    val speedMph: Int = 68,
    val impactForceG: Float = 38.4f,
    val decelerationG: Float = 12.2f,
    val axisX: Float = 4.2f,
    val axisY: Float = 36.8f,
    val axisZ: Float = 8.1f,
    val rollAngleDeg: Int = 42,
    val driverUnresponsiveSeconds: Int = 18,
    val locationName: String = "Interstate 280, Mile Marker 42 (Palo Alto, CA)",
    val latitude: Double = 37.4220,
    val longitude: Double = -122.0841
)

data class Hospital(
    val id: String,
    val name: String,
    val distanceKm: Double,
    val etaMinutes: Int,
    val traumaLevel: String, // e.g. "Level 1 Trauma Center"
    val rating: Float,
    val availableIcuBeds: Int,
    val address: String,
    val isAiRecommended: Boolean = false,
    val baseAdmissionCost: Double = 3200.0
)

data class Ambulance(
    val id: String,
    val name: String,
    val type: String, // "Advanced Life Support (ALS)", "Air Medical Evac", "Standard Life Support"
    val etaMinutes: Int,
    val driverName: String,
    val vehiclePlate: String,
    val paramedicCount: Int,
    val price: Double,
    val isAiRecommended: Boolean = false
)

data class AiEmergencyAnalysis(
    val confidenceScore: Int = 97,
    val severityLevel: String = "CRITICAL (Level 1 Trauma)",
    val reasoningText: String = "Vehicle experienced a high-impact 38.4G collision on I-280. Telematics detect 42-degree roll and 18s driver unresponsiveness. AI confidence is 97%. Immediate trauma hospital pre-reservation & dispatch recommended.",
    val riskScore: Float = 9.8f,
    val recommendedHospital: Hospital,
    val recommendedAmbulance: Ambulance
)

data class PaymentTransaction(
    val transactionId: String,
    val merchant: String = "Prava Emergency Health Escrow",
    val hospitalName: String,
    val ambulanceName: String,
    val totalAmount: Double,
    val insuranceCoverage: Double,
    val netPayableAmount: Double,
    val status: String, // "PROCESSING", "AUTHORIZED", "COMPLETED", "FAILED"
    val timestamp: Long = System.currentTimeMillis(),
    val preAuthId: String = "PRAVA-ATH-994812"
)

data class UserProfile(
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@guardian.ai",
    val phone: String = "+1 (555) 019-2834",
    val bloodGroup: String = "O Positive",
    val medicalNotes: String = "No known drug allergies. Penicillin safe.",
    val insuranceProvider: String = "Blue Cross Shield Premier",
    val policyNumber: String = "BCS-99482711-X",
    val autoPayLimit: Double = 5000.0,
    val isAutoPayEnabled: Boolean = true
)
