package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;

public class LoginPage extends AppCompatActivity {
    EditText text_username;
    EditText text_password;
    Button btn_login;
    TextView text_signup;
    TextView text_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        text_username = (EditText) findViewById(R.id.textLoginUsername);
        text_password = (EditText) findViewById(R.id.textLoginPassword);
        btn_login = (Button) findViewById(R.id.buttonLogin);
        text_signup = (TextView) findViewById(R.id.textViewSignupButton);
        text_forgot_password = (TextView) findViewById(R.id.textViewForgotPasswordButton);

        btn_login.setOnClickListener(view -> {
            String username = text_username.getText().toString();
            String password = text_password.getText().toString();

//            User user = User.getUser(username);
//            if (user == null) {
//                Toast.makeText(getApplicationContext(), "Bu kullanıcı adına sahip kayıt bulunamadı.", Toast.LENGTH_SHORT).show();
//                return;
//            }

//            if (!user.getPassword().equals(password)) {
//                Toast.makeText(getApplicationContext(), "Girdiğiniz parola hatalıdır.", Toast.LENGTH_SHORT).show();
//                return;
//            }

            Intent loginIntent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(loginIntent);
        });

        text_signup.setOnClickListener(view -> {
            Intent signupIntent = new Intent(getApplicationContext(), SignupPage.class);
            startActivity(signupIntent);
        });

        text_forgot_password.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Buraya şifre sıfırlama eklenecek!", Toast.LENGTH_SHORT).show();
        });
    }
}