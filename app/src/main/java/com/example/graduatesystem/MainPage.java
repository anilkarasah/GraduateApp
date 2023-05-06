package com.example.graduatesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Locale;
import java.util.Map;

public class MainPage extends AppCompatActivity {

    private TextView text_id;
    private TextView text_fullName;
    private TextView text_registrationYear;
    private TextView text_graduationYear;
    private TextView text_phoneNumber;
    private TextView text_currentCompany;
    private TextView text_graduationDegree;
    private ImageView imageView_avatar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        text_id = findViewById(R.id.textViewId);
        text_fullName = findViewById(R.id.textViewFullName);
        text_registrationYear = findViewById(R.id.textViewRegistrationYear);
        text_graduationYear = findViewById(R.id.textViewGraduationYear);
        text_phoneNumber = findViewById(R.id.textViewPhoneNumber);
        text_currentCompany = findViewById(R.id.textViewCurrentCompany);
        text_graduationDegree = findViewById(R.id.textViewGraduationDegree);
        imageView_avatar = findViewById(R.id.imageViewMainAvatar);

        imageView_avatar.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
        });

        String uid = getIntent().getExtras().getString("uid");
        if (uid == null || TextUtils.isEmpty(uid)) {
            Intent loginIntent = new Intent(this, LoginPage.class);
            Toast.makeText(this, "Lütfen tekrar giriş yapın!", Toast.LENGTH_SHORT).show();
            startActivity(loginIntent);
            return;
        }

        text_id.setText(uid);

        db.collection("users")
            .document(uid)
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(documentSnapshot -> {
                Map<String, Object> map = documentSnapshot.getData();
                String fullName = map.get(User.FULL_NAME).toString();
                String registrationYear = map.get(User.REGISTRATION_YEAR).toString();
                String graduationYear = map.get(User.GRADUATION_YEAR).toString();

                Object phoneNumber = map.get(User.PHONE_NUMBER);
                if (phoneNumber != null && !TextUtils.isEmpty(phoneNumber.toString()))
                    text_phoneNumber.setText(phoneNumber.toString());

                Object currentCompany = map.get(User.CURRENT_COMPANY);
                if (currentCompany != null && !TextUtils.isEmpty(currentCompany.toString()))
                    text_currentCompany.setText(currentCompany.toString());

                Object degree = map.get(User.GRADUATION_DEGREE);
                if (degree != null && !TextUtils.isEmpty(degree.toString()))
                    text_graduationDegree.setText(degree.toString());

                Toast.makeText(getApplicationContext(), "Başarıyla giriş yapıldı!", Toast.LENGTH_SHORT).show();

                text_fullName.setText(fullName);
                text_registrationYear.setText(registrationYear);
                text_graduationYear.setText(graduationYear);
            });

        final long TWO_MEGABYTES = 2 * 1024 * 1024;
        storage.getReference()
            .child("profiles/" + uid + ".jpg")
            .getBytes(TWO_MEGABYTES)
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView_avatar.setImageBitmap(bitmap);
            });
    }
}