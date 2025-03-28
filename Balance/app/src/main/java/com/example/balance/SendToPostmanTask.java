package com.example.balance;


import android.os.AsyncTask;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendToPostmanTask extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://your-postman-api-url.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"weight\": \"" + params[0] + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            return "Réponse: " + responseCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur d'envoi";
        }
    }
}