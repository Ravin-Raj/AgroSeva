package com.example.agroseva;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btn_login;
    TextView register3, f_p, skiplogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.green));

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI elements
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        register3 = findViewById(R.id.register3);
        f_p = findViewById(R.id.f_p);
        skiplogin = findViewById(R.id.skiplogin);


        // Handle Login Button Click
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                // Input Validation
                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Enter Email");
                } else if (TextUtils.isEmpty(passwordText)) {
                    password.setError("Enter Password");
                } else {
                    // Firebase Authentication - Login
                    mAuth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(LoginActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // If login is successful
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Retrieve additional user info from Firestore
                                        String userId = user.getUid();
                                        db.collection("users").document(userId).get()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        DocumentSnapshot document = task1.getResult();
                                                        if (document.exists()) {
                                                            // Retrieve user data from Firestore (example: username)
                                                            String username = document.getString("username");

                                                            // You can now use this data as needed (e.g., display it, process it)
                                                            Toast.makeText(LoginActivity.this, "Login Successful!",Toast.LENGTH_SHORT).show();

                                                            // Redirect to the HomeActivity
                                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            Toast.makeText(LoginActivity.this, "WELCOME! " + username, Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(LoginActivity.this, "Home Page", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "User data not found in Firestore", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Failed to retrieve user data from Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    // If login fails
                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Handle Register Link Click (Navigating to SignupActivity)
        register3.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, "Register Page!", Toast.LENGTH_SHORT).show();
        });

        // Handle Forgot Password Link Click (Navigating to PasswordActivity)
        f_p.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, PassswordActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, "Reset Page!", Toast.LENGTH_SHORT).show();
        });
        skiplogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, "Home Page!", Toast.LENGTH_SHORT).show();
        });
    }
}
