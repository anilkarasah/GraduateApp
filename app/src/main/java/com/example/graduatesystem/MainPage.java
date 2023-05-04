package com.example.graduatesystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainPage extends AppCompatActivity {

    private TextView text_id;
    private TextView text_fullName;
    private TextView text_emailAddress;
    private TextView text_password;
    private TextView text_registrationYear;
    private TextView text_graduationYear;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Map<String, Object> map;
    private boolean retrievedUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        text_id = (TextView) findViewById(R.id.textViewId);
        text_fullName = (TextView) findViewById(R.id.textViewFullName);
        text_emailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
        text_password = (TextView) findViewById(R.id.textViewPassword);
        text_registrationYear = (TextView) findViewById(R.id.textViewRegistrationYear);
        text_graduationYear = (TextView) findViewById(R.id.textViewGraduationYear);

        retrievedUserData = savedInstanceState.getBoolean("retrievedUserData");
        if (!retrievedUserData) {
            Intent loginIntent = new Intent(this, LoginPage.class);
            startActivity(loginIntent);
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();

        text_id.setText(savedInstanceState.getString(User.UID));
        text_emailAddress.setText(savedInstanceState.getString(User.EMAIL_ADDRESS));

        text_fullName.setText(savedInstanceState.getString(User.FULL_NAME));
        text_registrationYear.setText(savedInstanceState.getString(User.REGISTRATION_YEAR));
        text_graduationYear.setText(savedInstanceState.getString(User.GRADUATION_YEAR));
    }
}