// LoginActivity.java
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
    private static final String AUTH_URL = "https://balance-xln2.onrender.com/auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.connect);
        btnLogin.setOnClickListener(v -> openWithingsOAuth());

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnInscription = findViewById(R.id.btnInscription);
        if (btnInscription != null) {
            btnInscription.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }
    }

    private void openWithingsOAuth() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URL));
        startActivity(browserIntent);
    }

    private void fetchDataFromBackend() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://balance-xln2.onrender.com/data")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("BackendData", "Réponse : " + responseData);
                } else {
                    Log.e("BackendData", "Erreur : " + response.message());
                }
            }
        });
    }
}