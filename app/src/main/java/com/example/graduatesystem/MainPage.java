package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainPage extends AppCompatActivity {

    private TextView text_id;
    private TextView text_fullName;
    private TextView text_emailAddress;
    private TextView text_password;
    private TextView text_registerYear;
    private TextView text_graduationYear;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        text_registerYear = (TextView) findViewById(R.id.textViewRegisterYear);
        text_graduationYear = (TextView) findViewById(R.id.textViewGraduationYear);

        FirebaseUser user = mAuth.getCurrentUser();

        Intent loginIntent = new Intent(getApplicationContext(), LoginPage.class);
        if (user == null) {
            startActivity(loginIntent);
            return;
        }

        Toast.makeText(getApplicationContext(), "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();

        text_id.setText(user.getUid());
        text_emailAddress.setText(user.getEmail());

        db.collection("users")
            .document(user.getUid())
            .get()
            .addOnSuccessListener(document -> {
                if (document.exists()) {
                    User userRecord = document.toObject(User.class);
                    text_fullName.setText(userRecord.getFullName());
                    text_registerYear.setText(userRecord.getRegisterYear());
                    text_graduationYear.setText(userRecord.getGraduationYear());

                    startActivity(loginIntent);
                }
            });
    }
}