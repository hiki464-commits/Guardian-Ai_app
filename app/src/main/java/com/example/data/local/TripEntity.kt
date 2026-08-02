package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_trips")
data class TripEntity(
    @PrimaryKey
    val id: String,
    val startAddress: String,
    val endAddress: String,
    val startTime: Long,
    val endTime: Long,
    val distanceKm: Double,
    val safetyScore: Int,
    val status: String // e.g. "COMPLETED", "EMERGENCY_TRIGGERED"
)
