package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class SignupPage extends AppCompatActivity {
    EditText text_username;
    EditText text_password;
    EditText text_passwordConfirm;
    EditText text_birthDate;
    EditText text_graduationDate;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        text_username = (EditText) findViewById(R.id.textSignupUsername);
        text_password = (EditText) findViewById(R.id.editTextPassword);
        text_passwordConfirm = (EditText) findViewById(R.id.editTextPasswordConfirm);
        text_birthDate = (EditText) findViewById(R.id.editTextBirthDate);
        text_graduationDate = (EditText) findViewById(R.id.editTextGraduationDate);

        btn_signup = (Button) findViewById(R.id.buttonSignup);
        btn_signup.setOnClickListener(view -> {
            String username = text_username.getText().toString();
            String password = text_password.getText().toString();
            String passwordConfirm = text_passwordConfirm.getText().toString();
            String[] birthDates = text_birthDate.getText().toString().split(".");
            String[] graduationDates = text_graduationDate.getText().toString().split(".");

            if (User.getUser(username) != null) {
                Toast.makeText(getApplicationContext(), "This username already exists.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordConfirm)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (birthDates.length != 3 || graduationDates.length != 3) {
                Toast.makeText(getApplicationContext(), "Either one of the dates are not in correct format. Please provide 'DD.MM.YYYY'", Toast.LENGTH_LONG).show();
                return;
            }

            int[] birthDateValues = new int[3];
            int[] graduationDateValues = new int[3];
            for (int i = 0; i < birthDates.length; i++) {
                birthDateValues[i] = Integer.parseInt(birthDates[i]);
                graduationDateValues[i] = Integer.parseInt(graduationDates[i]);
            }

            User user = new User(
                    username,
                    password,
                    new Date(birthDateValues[0], birthDateValues[1], birthDateValues[2]),
                    new Date(graduationDateValues[0], graduationDateValues[1], graduationDateValues[2]));

            User.addUser(user);

            Toast.makeText(getApplicationContext(), "Signed up successfully!", Toast.LENGTH_LONG).show();

            Intent loginActivity = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(loginActivity);
        });
    }
}