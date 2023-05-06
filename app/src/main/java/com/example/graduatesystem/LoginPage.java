package com.example.graduatesystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private TextView btn_signup;
    private TextView btn_forgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        text_emailAddress = (EditText) findViewById(R.id.textLoginEmailAddress);
        text_password = (EditText) findViewById(R.id.textLoginPassword);
        btn_login = (Button) findViewById(R.id.buttonLogin);
        btn_signup = (TextView) findViewById(R.id.buttonRedirectSignup);
        btn_forgotPassword = (TextView) findViewById(R.id.buttonRedirectForgotPassword);

        btn_login.setOnClickListener(view -> {
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();

            if (TextUtils.isEmpty(emailAddress)) {
                Toast.makeText(this, "Lütfen email adresinizi girin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Lütfen parolanızı girin!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnFailureListener(e -> Toast.makeText(this, "Email adresi veya parola hatalı!", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(this, authResult -> {
                    Toast.makeText(this, "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();

                    Intent mainPageIntent = new Intent(this, MainPage.class);
                    String uid = authResult.getUser().getUid();
                    mainPageIntent.putExtra("uid", uid);
                    startActivity(mainPageIntent);
                });
        });

        btn_signup.setOnClickListener(view -> {
            Intent signupIntent = new Intent(this, SignupPage.class);
            startActivity(signupIntent);
        });

        btn_forgotPassword.setOnClickListener(view -> {
            Intent forgotPasswordIntent = new Intent(this, ForgotPassword.class);
            startActivity(forgotPasswordIntent);
        });
    }
}