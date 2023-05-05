package com.example.graduatesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView image_profilePicture;
    private Button btn_takePicture;
    private Button btn_uploadPicture;
    private Button btn_updateProfilePicture;

    private EditText text_fullName;
    private EditText text_registrationYear;
    private EditText text_graduationYear;
    private EditText text_currentCompany;
    private Spinner spinner_degree;
    private Button btn_updateProfileInfo;

    private EditText text_emailAddress;
    private Button btn_updateEmailAddress;

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
        text_registrationYear = (EditText) findViewById(R.id.textProfileRegistrationYear);
        text_graduationYear = (EditText) findViewById(R.id.textProfileGraduationYear);
        text_currentCompany = (EditText) findViewById(R.id.textProfileCurrentCompany);
        spinner_degree = (Spinner) findViewById(R.id.spinnerDegree);
        btn_updateProfileInfo = (Button) findViewById(R.id.buttonUpdateProfile);

        text_emailAddress = (EditText) findViewById(R.id.textProfileEmailAddress);
        btn_updateEmailAddress = (Button) findViewById(R.id.buttonUpdateEmailAddress);

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

        // SET USER INFORMATION IN CORRESPONDING EDIT TEXT
        db.collection("users")
            .document(firebaseUser.getUid())
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
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

        // SET PROFILE PICTURE AS IMAGE VIEW
        final long TWO_MEGABYTES = 2 * 1024 * 1024;
        storage.getReference()
            .child("profiles/" + firebaseUser.getUid() + ".jpg")
            .getBytes(TWO_MEGABYTES)
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(bytes -> {
                profilePictureBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image_profilePicture.setImageBitmap(profilePictureBitmap);
            });

        // SET SPINNER VALUES FOR STUDENT DEGREE
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.degree_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_degree.setAdapter(adapter);
        spinner_degree.setOnItemSelectedListener(this);


        // UPDATE EMAIL ADDRESS SECTION
        btn_updateEmailAddress.setOnClickListener(view -> {
            String emailAddress = text_emailAddress.getText().toString();
            firebaseUser.verifyBeforeUpdateEmail(emailAddress)
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> db.collection("users")
                    .document(firebaseUser.getUid())
                    .update(User.EMAIL_ADDRESS, emailAddress)
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(unused1 -> redirectToLogin()));
        });

        // UPDATE PASSWORD SECTION
        btn_updatePassword.setOnClickListener(view -> {
            String emailAddress;
            if (firebaseUser == null || (emailAddress = firebaseUser.getEmail()) == null) {
                redirectToLogin();
                return;
            }

            String currentPassword = text_currentPassword.getText().toString();

            AuthCredential credential = EmailAuthProvider
                .getCredential(emailAddress, currentPassword);

            firebaseUser.reauthenticate(credential)
                .addOnFailureListener(e -> Toast.makeText(this, "Geçerli parola yanlış girildi.", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> {
                    String newPassword = text_newPassword.getText().toString();
                    firebaseUser.updatePassword(newPassword)
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnSuccessListener(unused1 -> {
                            Toast.makeText(this, "Parola başarıyla güncellendi.", Toast.LENGTH_SHORT).show();
                            text_currentPassword.setText("");
                            text_newPassword.setText("");
                            redirectToLogin();
                        });
                });
        });
    }

    private void redirectToLogin() {
        Toast.makeText(this, "Lütfen tekrar giriş yapınız.", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(this, LoginPage.class);
        startActivity(loginIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}