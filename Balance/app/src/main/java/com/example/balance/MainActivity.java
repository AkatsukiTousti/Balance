package com.example.balance;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private WiFiManagerHelper wifiHelper;
    private BalanceDataReceiver balanceReceiver;
    private TextView weightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiHelper = new WiFiManagerHelper(this);
        balanceReceiver = new BalanceDataReceiver("192.168.1.100", 8080); // Remplace par l'IP et le port de ta balanc

        wifiHelper.enableWiFi();


    }

    // Classe interne pour envoyer les données à Postman
    private static class SendToPostmanTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                String weight = params[0];
                URL url = new URL("https://api.postman.com/your-endpoint"); // Remplace par ton URL Postman
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInputString = "{\"weight\": \"" + weight + "\"}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                Log.d("Postman", "Response Code: " + responseCode);

            } catch (Exception e) {
                Log.e("Postman", "Erreur lors de l'envoi des données", e);
            }
            return null;
        }
    }
}
