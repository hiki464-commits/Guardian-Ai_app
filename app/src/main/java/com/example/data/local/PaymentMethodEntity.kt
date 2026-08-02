package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethodEntity(
    @PrimaryKey
    val id: String = "CARD-02",
    val holderName: String = "Heet Kale",
    val cardNumber: String = "4622943123232192",
    val cardLast4: String = "2192",
    val cvv: String = "652",
    val expiryMonth: String = "12",
    val expiryYear: String = "30",
    val brand: String = "VISA / Prava Sandbox",
    val isDefault: Boolean = true,
    val isAutoPayEnabled: Boolean = true,
    val limitAmount: Double = 5000.0,
    val dailyTransactionLimit: Int = 30,
    val connectionStatus: String = "CONNECTED" // "CONNECTED", "VERIFYING", "DISCONNECTED"
)

