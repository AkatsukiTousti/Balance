// MainActivity.java
package com.example.balance;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

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
        balanceReceiver = new BalanceDataReceiver("192.168.1.100", 8080); // IP de la balance

        wifiHelper.enableWiFi();
        // Ajoute ici un exemple de réception de poids et d'envoi au backend Render
        String poidsExemple = "75.2";
        new Thread(() -> sendWeightToBackend(poidsExemple)).start();
    }

    // Envoie de données vers ton serveur Node.js sur Render
    private void sendWeightToBackend(String weight) {
        try {
            URL url = new URL("https://balance-xln2.onrender.com/data");
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
            Log.d("Backend", "Code réponse Render : " + responseCode);

        } catch (Exception e) {
            Log.e("Backend", "Erreur d'envoi des données à Render", e);
        }
    }
}