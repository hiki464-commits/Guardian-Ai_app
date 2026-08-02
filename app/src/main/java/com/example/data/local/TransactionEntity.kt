package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val transactionId: String,
    val merchant: String = "Prava Emergency Health Escrow Sandbox",
    val hospitalName: String,
    val ambulanceName: String,
    val totalAmount: Double,
    val insuranceCoverage: Double,
    val netPayableAmount: Double,
    val status: String, // "COMPLETED", "AUTHORIZED", "PENDING", "FAILED"
    val timestamp: Long = System.currentTimeMillis(),
    val preAuthId: String = "PRAVA-ATH-994812",
    val paymentMethod: String = "Prava AI Wallet",
    val emergencyId: String = "EMG-280-PALO"
)
