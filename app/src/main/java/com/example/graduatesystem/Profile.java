package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class Profile extends AppCompatActivity {
    private ImageView image_profilePicture;
    private Button btn_takePicture;
    private Button btn_uploadPicture;
    private Button btn_updateProfilePicture;

    private EditText text_fullName;
    private EditText text_emailAddress;
    private EditText text_registrationYear;
    private EditText text_graduationYear;
    private Button btn_updateProfileInfo;

    private EditText text_currentPassword;
    private EditText text_newPassword;
    private Button btn_updatePassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private FirebaseUser firebaseUser;
    private User user;

    private Bitmap profilePictureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        image_profilePicture = (ImageView) findViewById(R.id.imageViewAvatar);
        btn_takePicture = (Button) findViewById(R.id.buttonProfileTakePicture);
        btn_uploadPicture = (Button) findViewById(R.id.buttonProfileUploadPicture);
        btn_updateProfilePicture = (Button) findViewById(R.id.buttonUpdatePicture);

        text_fullName = (EditText) findViewById(R.id.textProfileFullName);
        text_emailAddress = (EditText) findViewById(R.id.textProfileEmailAddress);
        text_registrationYear = (EditText) findViewById(R.id.textProfileRegistrationYear);
        text_graduationYear = (EditText) findViewById(R.id.textProfileGraduationYear);
        btn_updateProfileInfo = (Button) findViewById(R.id.buttonUpdateProfile);

        text_currentPassword = (EditText) findViewById(R.id.textProfileCurrentPassword);
        text_newPassword = (EditText) findViewById(R.id.textProfileNewPassword);
        btn_updatePassword = (Button) findViewById(R.id.buttonUpdatePassword);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            redirectToLogin();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("users")
            .document(firebaseUser.getUid())
            .get()
            .addOnFailureListener(e -> redirectToLogin())
            .addOnSuccessListener(documentSnapshot -> {
                Map<String, Object> map = documentSnapshot.getData();
                String fullName = map.get(User.FULL_NAME).toString();
                String emailAddress = map.get(User.EMAIL_ADDRESS).toString();
                String registrationYear = map.get(User.REGISTRATION_YEAR).toString();
                String graduationYear = map.get(User.GRADUATION_YEAR).toString();

                text_fullName.setText(fullName);
                text_emailAddress.setText(emailAddress);
                text_registrationYear.setText(registrationYear);
                text_graduationYear.setText(graduationYear);
            });

        final long TWO_MEGABYTES = 2 * 1024 * 1024;
        storage.getReference()
            .child("profiles/" + firebaseUser.getUid() + ".jpg")
            .getBytes(TWO_MEGABYTES)
            .addOnFailureListener(e -> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(bytes -> {
                profilePictureBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image_profilePicture.setImageBitmap(profilePictureBitmap);
            });
    }

    private void redirectToLogin() {
        Toast.makeText(this, "Lütfen tekrar giriş yapınız.", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(this, LoginPage.class);
        startActivity(loginIntent);
    }
}