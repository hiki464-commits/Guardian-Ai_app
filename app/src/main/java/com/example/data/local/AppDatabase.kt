package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ContactEntity::class,
        IncidentEntity::class,
        TripEntity::class,
        AutoPayConfigEntity::class,
        TransactionEntity::class,
        PaymentMethodEntity::class,
        SmsHistoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun incidentDao(): IncidentDao
    abstract fun tripDao(): TripDao
    abstract fun autoPayDao(): AutoPayDao
    abstract fun transactionDao(): TransactionDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun smsDao(): SmsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "guardian_ai_db"
                ).fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
