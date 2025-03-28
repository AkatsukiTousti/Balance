package com.example.balance2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val CLIENT_ID = "TON_CLIENT_ID"
    private val CLIENT_SECRET = "TON_CLIENT_SECRET"
    private val REDIRECT_URI = "tonapp://callback"  // Assure-toi de l’enregistrer dans Withings Developer
    private val AUTH_URL = "https://account.withings.com/oauth2_user/authorize2" +
            "?response_type=code" +
            "&client_id=$CLIENT_ID" +
            "&redirect_uri=$REDIRECT_URI" +
            "&scope=user.metrics" +
            "&state=12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ouvre la page d'authentification de Withings
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL))
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            val authCode = uri.getQueryParameter("code")
            if (authCode != null) {
                Log.d("OAuth", "Code reçu : $authCode")
                getAccessToken(authCode)
            } else {
                Log.e("OAuth", "Erreur lors de la récupération du code d'authentification")
            }
        }
    }

    private fun getAccessToken(authCode: String) {
        val url = "https://wbsapi.withings.net/v2/oauth2"

        // Configuration sécurisée du client OkHttp
        val client = OkHttpClient.Builder()
            .build()

        val body = FormBody.Builder()
            .add("action", "requesttoken")
            .add("client_id", CLIENT_ID)
            .add("client_secret", CLIENT_SECRET)
            .add("code", authCode)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", REDIRECT_URI)
            .build()

        val request = Request.Builder()
            .url(url)  // 🚀 Toujours en HTTPS
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback
        {
            override fun onFailure(call: Call, e: IOException)
            {
                Log.e("OAuth", "Échec de la requête HTTPS : ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful) {
                    val json = JSONObject(responseData ?: "")
                    val accessToken = json.getJSONObject("body").getString("access_token")
                    Log.d("OAuth", "Access Token sécurisé reçu : $accessToken")
                    // Stocker le token de manière sécurisée (ex: EncryptedSharedPreferences)
                } else {
                    Log.e("OAuth", "Erreur dans la réponse HTTPS : $responseData")
                }
            }
        })
    }
}