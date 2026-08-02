package com.example.data.remote

import com.example.BuildConfig
import com.example.data.model.PaymentTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

interface PaymentService {
    suspend fun authorizeEmergencyAutoPay(
        hospitalName: String,
        ambulanceName: String,
        totalCost: Double,
        insuranceCoverage: Double,
        payableAmount: Double,
        preAuthId: String,
        customApiKey: String? = null,
        onProgressUpdate: (stepIndex: Int, stepName: String) -> Unit
    ): PaymentTransaction
}

class MockPravaSandboxService : PaymentService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    override suspend fun authorizeEmergencyAutoPay(
        hospitalName: String,
        ambulanceName: String,
        totalCost: Double,
        insuranceCoverage: Double,
        payableAmount: Double,
        preAuthId: String,
        customApiKey: String?,
        onProgressUpdate: (stepIndex: Int, stepName: String) -> Unit
    ): PaymentTransaction = withContext(Dispatchers.IO) {
        var secretKey = when {
            !customApiKey.isNullOrBlank() -> customApiKey.trim()
            try { BuildConfig.PRAVA_SECRET_KEY } catch (e: Throwable) { "" }.isNotBlank() -> BuildConfig.PRAVA_SECRET_KEY
            try { BuildConfig.PRAVA_API_KEY } catch (e: Throwable) { "" }.isNotBlank() -> BuildConfig.PRAVA_API_KEY
            else -> throw IllegalStateException("Prava API Key not configured. Please set PRAVA_SECRET_KEY in your .env file.")
        }

        // Step 1: Connecting Gateway
        onProgressUpdate(0, "Creating Prava Session on sandbox.api.prava.space...")
        delay(600)

        var sessionId = ""
        var txId = "PRV-EMG-" + UUID.randomUUID().toString().take(8).uppercase()
        var status = "COMPLETED"

        try {
            // Create Prava Session
            val sessionJson = JSONObject().apply {
                put("user_id", "usr_alex_rivera")
                put("user_email", "alex.rivera@guardian.ai")
                put("total_amount", payableAmount)
                put("currency", "USD")
                put("integration_type", "embedding")
                put("purchase_context", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("merchant_details", JSONObject().apply {
                            put("name", "Prava Emergency Health Escrow Sandbox")
                            put("url", "https://prava.space")
                            put("country_code_iso2", "US")
                        })
                        put("product_details", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("description", "Emergency ICU Bed & ALS Ambulance Settlement")
                                put("unit_price", payableAmount)
                                put("quantity", 1)
                            })
                        })
                    })
                })
            }.toString()

            val sessionReq = Request.Builder()
                .url("https://sandbox.api.prava.space/v1/sessions")
                .addHeader("Authorization", "Bearer $secretKey")
                .addHeader("Content-Type", "application/json")
                .post(sessionJson.toRequestBody("application/json".toMediaType()))
                .build()

            val resp = client.newCall(sessionReq).execute()
            if (resp.isSuccessful) {
                val bodyStr = resp.body?.string()
                if (!bodyStr.isNullOrEmpty()) {
                    val resJson = JSONObject(bodyStr)
                    sessionId = resJson.optString("session_id", "")
                    if (resJson.has("order_id")) txId = resJson.getString("order_id")
                }
            }
        } catch (e: Exception) {
            // Soft fallback if server is unreachable
        }

        // Step 2: Verification
        onProgressUpdate(1, "Authenticating Emergency AutoPay Mandate & Incident Proof...")
        delay(700)

        // Step 3: Result Polling
        onProgressUpdate(2, "Verifying One-time Prava Virtual Credentials...")
        delay(700)

        if (sessionId.isNotEmpty()) {
            try {
                val pollReq = Request.Builder()
                    .url("https://sandbox.api.prava.space/v1/sessions/$sessionId/payment-result")
                    .addHeader("Authorization", "Bearer $secretKey")
                    .get()
                    .build()
                client.newCall(pollReq).execute()
            } catch (e: Exception) { }
        }

        // Step 4: Escrow Settlement
        onProgressUpdate(3, "Securing Escrow Settlement for $hospitalName...")
        delay(700)

        if (sessionId.isNotEmpty()) {
            try {
                val reportJson = JSONObject().apply {
                    put("txn_ref_id", "ref_$txId")
                    put("txn_status", "APPROVED")
                }.toString()

                val reportReq = Request.Builder()
                    .url("https://sandbox.api.prava.space/v1/sessions/$sessionId/report-status")
                    .addHeader("Authorization", "Bearer $secretKey")
                    .addHeader("Content-Type", "application/json")
                    .post(reportJson.toRequestBody("application/json".toMediaType()))
                    .build()
                client.newCall(reportReq).execute()
            } catch (e: Exception) { }
        }

        // Step 5: Complete
        onProgressUpdate(4, "Prava Escrow Payment Approved & Token Settled.")
        delay(500)

        PaymentTransaction(
            transactionId = txId,
            merchant = "Prava Emergency Health Escrow Sandbox",
            hospitalName = hospitalName,
            ambulanceName = ambulanceName,
            totalAmount = totalCost,
            insuranceCoverage = insuranceCoverage,
            netPayableAmount = payableAmount,
            status = status,
            timestamp = System.currentTimeMillis(),
            preAuthId = preAuthId
        )
    }
}

