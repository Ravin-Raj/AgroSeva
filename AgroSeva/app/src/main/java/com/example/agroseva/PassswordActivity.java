package com.example.agroseva;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class PassswordActivity extends AppCompatActivity {

    EditText email;
    Button btn_back;
    Button btn_reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passsword);
        getWindow().setStatusBarColor(ContextCompat.getColor(PassswordActivity.this,R.color.green));

        // Initialize UI elements
        email = findViewById(R.id.email);
        btn_back = findViewById(R.id.btn_back);
        btn_reset = findViewById(R.id.btn_reset);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Reset Password Button Click Listener
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();

                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Email is required");
                } else {
                    // Send password reset email
                    mAuth.sendPasswordResetEmail(emailText)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PassswordActivity.this, "Password reset email sent. Check your email.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PassswordActivity.this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Back Button Click Listener
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(PassswordActivity.this, "Login Page!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
