package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_reports")
data class IncidentEntity(
    @PrimaryKey
    val id: String,
    val timestamp: Long,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val severity: String,
    val confidence: Int,
    val aiReasoning: String,
    val hospitalName: String,
    val ambulanceType: String,
    val totalCost: Double,
    val insuranceCovered: Double,
    val payableAmount: Double,
    val transactionId: String,
    val status: String
)
