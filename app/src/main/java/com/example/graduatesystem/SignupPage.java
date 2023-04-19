package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduatesystem.db.DbHandler;
import com.example.graduatesystem.entities.User;

public class SignupPage extends AppCompatActivity {
    EditText text_fullName;
    EditText text_username;
    EditText text_password;
    EditText text_registerYear;
    EditText text_graduationYear;
    Button btn_signup;

    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        dbHandler = new DbHandler(SignupPage.this);

        text_fullName = (EditText) findViewById(R.id.textSignupFullName);
        text_username = (EditText) findViewById(R.id.textSignupUsername);
        text_password = (EditText) findViewById(R.id.editTextPassword);
        text_registerYear = (EditText) findViewById(R.id.editTextRegisterYear);
        text_graduationYear = (EditText) findViewById(R.id.editTextGraduationYear);

        btn_signup = (Button) findViewById(R.id.buttonSignup);
        btn_signup.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            String username = text_username.getText().toString();
            String password = text_password.getText().toString();
            int registerYear = Integer.parseInt(text_registerYear.getText().toString());
            int graduationYear = Integer.parseInt(text_graduationYear.getText().toString());

            if (!User.validateFullName(fullName)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validateUsername(username)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir kullanıcı adı giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validatePassword(password)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir parola giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (User.getUser(username) != null) {
//                Toast.makeText(getApplicationContext(), "Bu kullanıcı adına sahip bir kullanıcı zaten bulunuyor.", Toast.LENGTH_SHORT).show();
//                return;
//            }

            User user = new User(
                    fullName,
                    username,
                    password,
                    registerYear,
                    graduationYear);

//            User.addUser(user);

            dbHandler.addNewUser(user);

            Toast.makeText(getApplicationContext(), "Başarıyla kayıt oldunuz!", Toast.LENGTH_LONG).show();

            Intent loginActivity = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(loginActivity);
        });
    }
}