package com.example.balance;


import android.content.Context;
import android.net.wifi.WifiManager;


public class WiFiManagerHelper {
    private WifiManager wifiManager;

    public WiFiManagerHelper(Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void enableWiFi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }
}