package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_history")
data class SmsHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipientName: String,
    val recipientPhone: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "DELIVERED" // "DELIVERED", "SENT", "FAILED"
)
