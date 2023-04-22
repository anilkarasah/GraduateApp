package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.db.UserRepository;
import com.example.graduatesystem.entities.User;

public class SignupPage extends AppCompatActivity {
    EditText text_fullName;
    EditText text_emailAddress;
    EditText text_password;
    EditText text_registerYear;
    EditText text_graduationYear;
    Button btn_signup;
    TextView text_login;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        userRepository = new UserRepository(getApplicationContext());

        text_fullName = (EditText) findViewById(R.id.textSignupFullName);
        text_emailAddress = (EditText) findViewById(R.id.textSignupEmailAddress);
        text_password = (EditText) findViewById(R.id.editTextPassword);
        text_registerYear = (EditText) findViewById(R.id.editTextRegisterYear);
        text_graduationYear = (EditText) findViewById(R.id.editTextGraduationYear);
        text_login = (TextView) findViewById(R.id.textViewLoginButton);

        Intent loginActivity = new Intent(getApplicationContext(), LoginPage.class);

        text_login.setOnClickListener(view -> startActivity(loginActivity));

        btn_signup = (Button) findViewById(R.id.buttonSignup);
        btn_signup.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();
            int registerYear = Integer.parseInt(text_registerYear.getText().toString());
            int graduationYear = Integer.parseInt(text_graduationYear.getText().toString());

            if (!User.validateFullName(fullName)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validateEmailAddress(emailAddress)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validatePassword(password)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir parola giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(
                    fullName,
                    emailAddress,
                    password,
                    registerYear,
                    graduationYear);

            userRepository.createUser(user);

            Toast.makeText(getApplicationContext(), "Başarıyla kayıt oldunuz!", Toast.LENGTH_LONG).show();

            startActivity(loginActivity);
        });
    }
}