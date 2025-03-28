package com.example.balance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de";
    private static final String CLIENT_SECRET = "bd6bad12b42dc4b2b435f87e361b3d92942d683bac8dbe1d7c5fd922af67cf19";
    private static final String REDIRECT_URI = "https://oauth.pstmn.io/v1/callback";
    private static final String AUTH_URL = "https://account.withings.com/oauth2_user/authorize2?response_type=code&client_id=82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback&state=BzWss_WUVNsv6mA&scope=user.metrics%2Cuser.activity";
    private static final String TOKEN_URL = "https://wbsapi.withings.net";
    private static String ACCESS_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bouton de connexion à Withings
        Button btnLogin = findViewById(R.id.connect);
        btnLogin.setOnClickListener(v -> openWithingsOAuth());

        // Bouton retour pour quitter l'application
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Ferme l'activité

        // Bouton d'inscription qui ouvre RegisterActivity
        Button btnInscription = findViewById(R.id.btnInscription);

        if (btnInscription == null) {
            Log.e("DEBUG", "❌ ERREUR : btnInscription est NULL !");
        } else {
            Log.d("DEBUG", "✅ btnInscription trouvé !");

            btnInscription.setOnClickListener(v -> {
                Log.d("DEBUG", "✅ Bouton Inscription cliqué !");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }

    }

    private void openWithingsOAuth() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL));
        startActivity(browserIntent);
    }

    // Récupération du token après redirection
    private void getAccessToken(String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("action", "requesttoken")
                .add("grant_type", "authorization_code")
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("code", code)
                .add("redirect_uri", REDIRECT_URI)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("OAuth", "Réponse : " + responseData);
                // Extraire ACCESS_TOKEN ici (utiliser un parser JSON)
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}