package com.example.data.remote

import com.example.BuildConfig
import com.example.data.local.SmsHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LinqService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun sendEmergencySms(
        recipientName: String,
        recipientPhone: String,
        hospitalName: String,
        ambulanceName: String,
        etaMinutes: Int,
        transactionStatus: String,
        locationName: String
    ): SmsHistoryEntity = withContext(Dispatchers.IO) {
        val messageText = "🚨 GUARDIAN AI EMERGENCY ALERT: High-G accident detected at $locationName. Hospital: $hospitalName. Ambulance: $ambulanceName ($etaMinutes min ETA). Prava AutoPay: $transactionStatus. Live Track: https://guardian.ai/live"

        var status: String

        val linqKey = try {
            BuildConfig.LINQ_API_KEY
        } catch (e: Throwable) {
            null
        }

        val linqSender = try {
            BuildConfig.LINQ_SENDER_PHONE
        } catch (e: Throwable) {
            null
        }

        if (linqKey.isNullOrBlank()) {
            return@withContext SmsHistoryEntity(
                recipientName = recipientName,
                recipientPhone = recipientPhone,
                message = messageText,
                timestamp = System.currentTimeMillis(),
                status = "FAILED (API Key Missing)"
            )
        }

        try {
            // Linq Partner API V3 Payload structure
            val jsonPayload = JSONObject().apply {
                put("from", linqSender ?: "+12223334444") // Fallback to placeholder if not in .env
                put("to", org.json.JSONArray().apply { put(recipientPhone) })
                put("message", JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("type", "text")
                            put("value", messageText)
                        })
                    })
                })
            }.toString()

            val request = Request.Builder()
                .url("https://api.linqapp.com/api/partner/v3/chats")
                .addHeader("Authorization", "Bearer $linqKey")
                .addHeader("Content-Type", "application/json")
                .post(jsonPayload.toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                status = "DELIVERED"
            } else {
                val errorBody = response.body?.string()
                status = "FAILED (HTTP ${response.code}: $errorBody)"
            }
        } catch (e: Exception) {
            status = "FAILED (${e.message})"
        }

        SmsHistoryEntity(
            recipientName = recipientName,
            recipientPhone = recipientPhone,
            message = messageText,
            timestamp = System.currentTimeMillis(),
            status = status
        )
    }
}
