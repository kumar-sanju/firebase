package com.sanju.text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private ProgressBar loginProgress;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ImageView loginPhoto;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        loginProgress = findViewById(R.id.login_progress);
        btnLogin = findViewById(R.id.loginBtn);
        loginPhoto = findViewById(R.id.login_photo);

        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in successfully!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, Home.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login...", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                loginProgress.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty()) {
                    userEmail.setError("Please enter your email id...");
                    userEmail.requestFocus();
                } else if (password.isEmpty()) {
                    userPassword.setError("Please enter your password...");
                    userPassword.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Field are Empty!", Toast.LENGTH_SHORT);
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                loginProgress.setVisibility(View.INVISIBLE);
                                btnLogin.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "Signin Error, Please login Again!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Signin Successful!", Toast.LENGTH_LONG).show();
                                loginProgress.setVisibility(View.INVISIBLE);
                                btnLogin.setVisibility(View.VISIBLE);

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Ocurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

        }
    }
