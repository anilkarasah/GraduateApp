package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText editText_emailAddress;
    private Button btn_confirmEmailAddress;
    private Button btn_goBack;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        editText_emailAddress = (EditText) findViewById(R.id.textProfileEmailAddress);
        btn_confirmEmailAddress = (Button) findViewById(R.id.buttonSendConfirmationEmail);
        btn_goBack = (Button) findViewById(R.id.buttonGoBack);

        Intent loginIntent = new Intent(this, LoginPage.class);

        btn_confirmEmailAddress.setOnClickListener(view -> {
            String emailAddress = editText_emailAddress.getText().toString();

            mAuth.sendPasswordResetEmail(emailAddress)
                .addOnFailureListener(e -> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Doğrulama emaili gönderildi.", Toast.LENGTH_SHORT).show();
                    startActivity(loginIntent);
                });
        });

        btn_goBack.setOnClickListener(view -> startActivity(loginIntent));
    }
}