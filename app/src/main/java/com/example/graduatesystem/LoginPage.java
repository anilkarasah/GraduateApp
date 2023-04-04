package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {
    EditText text_username;
    EditText text_password;
    Button btn_login;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        text_username = (EditText) findViewById(R.id.textLoginUsername);
        text_password = (EditText) findViewById(R.id.textLoginPassword);
        btn_login = (Button) findViewById(R.id.buttonLogin);
        btn_signup = (Button) findViewById(R.id.buttonLoginSignup);

        btn_login.setOnClickListener(view -> {
            String username = text_username.getText().toString();
            String password = text_password.getText().toString();

            User user = User.getUser(username);
            if (user == null) {
                Toast.makeText(getApplicationContext(), "Username is not correct.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!user.getPassword().equals(password)) {
                Toast.makeText(getApplicationContext(), "Password is not correct.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent loginIntent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(loginIntent);
        });

        btn_signup.setOnClickListener(view -> {
            Intent signupIntent = new Intent(getApplicationContext(), SignupPage.class);
            startActivity(signupIntent);
        });
    }
}