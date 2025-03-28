package com.example.balance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class BalanceDataReceiver {
    private String balanceIP;
    private int balancePort;

    public BalanceDataReceiver(String balanceIP, int balancePort) {
        this.balanceIP = balanceIP;
        this.balancePort = balancePort;
    }

    public String getDataFromBalance() {
        try {
            Socket socket = new Socket(balanceIP, balancePort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = reader.readLine();
            socket.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur de connexion";
        }
    }
}