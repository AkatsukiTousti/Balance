package com.example.withingsauthapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de\n";
    private static final String REDIRECT_URI = "https://oauth.pstmn.io/v1/callback"; // URL de redirection
    private static final String AUTH_URL = "https://account.withings.com/oauth2_user/authorize2?response_type=code&client_id=82108352dc5fb2557c0686664160c4dcd047357dd0bb9110d3383bd9f0f1e3de&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback&state=8aBKns8AReUfdxC&scope=user.metrics%2Cuser.activity" +
            "?response_type=code" +
            "&client_id=" + CLIENT_ID +
            "&redirect_uri=" + REDIRECT_URI +
            "&scope=user.info,user.metrics" +  // Ajouter les scopes nécessaires
            "&state=random_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button authButton = findViewById(R.id.authButton);
        authButton.setOnClickListener(view -> startOAuth());
    }

    private void startOAuth() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL));
        startActivity(browserIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();

        if (uri != null && uri.toString().startsWith("withingsauthapp://callback")) {
            String authCode = uri.getQueryParameter("code");
            if (authCode != null) {
                Log.d("OAuth", "Code reçu : " + authCode);
                getAccessToken(authCode);
            } else {
                Log.e("OAuth", "Erreur lors de la récupération du code");
            }
        }
    }

    private void getAccessToken(String authCode) {
        // Ici, tu dois envoyer une requête POST à Withings pour échanger le code contre un access_token
        Log.d("OAuth", "Échange du code contre un access_token...");
    }
}