package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, id ASC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteContact(id: Long)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionId = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: TransactionEntity)
}

@Dao
interface PaymentMethodDao {
    @Query("SELECT * FROM payment_methods WHERE isDefault = 1 LIMIT 1")
    fun getDefaultPaymentMethod(): Flow<PaymentMethodEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePaymentMethod(pm: PaymentMethodEntity)
}

@Dao
interface SmsDao {
    @Query("SELECT * FROM sms_history ORDER BY timestamp DESC")
    fun getAllSmsLogs(): Flow<List<SmsHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSmsLog(sms: SmsHistoryEntity)
}

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incident_reports ORDER BY timestamp DESC")
    fun getAllIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incident_reports WHERE id = :id")
    suspend fun getIncidentById(id: String): IncidentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity)
}

@Dao
interface TripDao {
    @Query("SELECT * FROM user_trips ORDER BY startTime DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)
}

@Dao
interface AutoPayDao {
    @Query("SELECT * FROM autopay_config WHERE id = 1")
    fun getAutoPayConfig(): Flow<AutoPayConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAutoPayConfig(config: AutoPayConfigEntity)
}
