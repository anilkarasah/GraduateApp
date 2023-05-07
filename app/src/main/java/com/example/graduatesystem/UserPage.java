package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPage extends AppCompatActivity {
    private String uid;

    private TextView text_uid;
    private TextView text_fullName;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        User.assertAuthentication(this);

        Bundle bundle = getIntent().getExtras();
        uid = bundle.get("uid").toString();
        text_fullName = findViewById(R.id.textUserPageFullName);

        if (TextUtils.isEmpty(uid)) {
            Intent mainPageIntent = new Intent(this, MainPage.class);
            Toast.makeText(this, "Bir sorun oluştu.", Toast.LENGTH_SHORT).show();
            startActivity(mainPageIntent);
            return;
        }

        text_fullName.setText(uid);

        db = FirebaseFirestore.getInstance();

        db.collection("users")
            .document(uid)
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(documentSnapshot -> {
            });
    }
}