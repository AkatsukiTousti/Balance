package com.example.balance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText nom = findViewById(R.id.nom);
        EditText prenom = findViewById(R.id.prenom);
        EditText email = findViewById(R.id.email);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String nomText = nom.getText().toString().trim();
            String prenomText = prenom.getText().toString().trim();
            String emailText = email.getText().toString().trim();

            if (nomText.isEmpty() || prenomText.isEmpty() || emailText.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}