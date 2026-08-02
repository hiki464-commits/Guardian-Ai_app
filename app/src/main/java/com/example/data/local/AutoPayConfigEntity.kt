package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autopay_config")
data class AutoPayConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val isEnabled: Boolean = true,
    val limitAmount: Double = 5000.0,
    val paymentMethodName: String = "Prava Express Direct",
    val cardLast4: String = "8892",
    val preAuthId: String = "PRAVA-ATH-994812",
    val status: String = "ACTIVE_MANDATE"
)
