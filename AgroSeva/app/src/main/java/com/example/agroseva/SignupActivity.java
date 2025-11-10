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

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText username, email, password, cpassword;
    Button btn_register;
    TextView login2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(ContextCompat.getColor(SignupActivity.this,R.color.green));


        // Initialize UI elements
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        btn_register = findViewById(R.id.btn_register);
        login2 = findViewById(R.id.login2);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Register Button Click Listener
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch input values
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String cpasswordText = cpassword.getText().toString().trim();

                // Check if fields are empty or passwords don't match
                if (TextUtils.isEmpty(usernameText)) {
                    username.setError("Username is required");
                    return;
                }
                if (TextUtils.isEmpty(emailText)) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(passwordText)) {
                    password.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(cpasswordText)) {
                    cpassword.setError("Please confirm your password");
                    return;
                }
                if (!passwordText.equals(cpasswordText)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create a map of the user data to store in Firestore
                Map<String, Object> user = new HashMap<>();
                user.put("username", usernameText);
                user.put("email", emailText);
                user.put("password", passwordText);
                user.put("cpassword", cpasswordText);

                // Disable the register button to prevent multiple clicks
                btn_register.setEnabled(false);

                // Create user with email and password
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            // Re-enable the register button
                            btn_register.setEnabled(true);

                            if (task.isSuccessful()) {
                                // Get the registered user
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();

                                    // Save user data to Firestore
                                    db.collection("users").document(userId).set(user)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    // Navigate to Login Activity
                                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignupActivity.this, "Failed to store user data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // Registration failed
                                Toast.makeText(SignupActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Login Text Click Listener
        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(SignupActivity.this, "Login Page", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
