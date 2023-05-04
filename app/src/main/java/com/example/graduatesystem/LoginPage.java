package com.example.graduatesystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    private EditText text_emailAddress;
    private EditText text_password;
    private Button btn_login;
    private TextView text_signup;
    private TextView text_forgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        text_emailAddress = (EditText) findViewById(R.id.textLoginEmailAddress);
        text_password = (EditText) findViewById(R.id.textLoginPassword);
        btn_login = (Button) findViewById(R.id.buttonLogin);
        text_signup = (TextView) findViewById(R.id.textViewSignupButton);
        text_forgotPassword = (TextView) findViewById(R.id.textViewForgotPasswordButton);

        btn_login.setOnClickListener(view -> {
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();

            mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Email adresi veya parola hatalı!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent loginIntent = new Intent(getApplicationContext(), MainPage.class);
                    startActivity(loginIntent);
                });
        });

        text_signup.setOnClickListener(view -> {
            Intent signupIntent = new Intent(getApplicationContext(), SignupPage.class);
            startActivity(signupIntent);
        });

        text_forgotPassword.setOnClickListener(view -> Toast.makeText(
                getApplicationContext(),
                "Buraya şifre sıfırlama eklenecek!",
                Toast.LENGTH_SHORT)
            .show());
    }
}