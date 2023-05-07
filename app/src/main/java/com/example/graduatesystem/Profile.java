package com.example.graduatesystem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private EditText text_phoneNumber;
    private Spinner spinner_degree;
    private int selectedDegreeIndex = 0;
    private Button btn_updateProfileInfo;

    private EditText text_emailAddress;
    private Button btn_updateEmailAddress;

    private EditText text_currentPassword;
    private EditText text_newPassword;
    private Button btn_updatePassword;

    private Button btn_menu;

    private TextView btn_logout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private FirebaseUser firebaseUser;
    private User user;
    Map<String, Object> map;

    private Bitmap profilePictureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User.assertAuthentication(this);

        image_profilePicture = findViewById(R.id.imageProfileAvatar);
        btn_takePicture = findViewById(R.id.buttonProfileTakePicture);
        btn_uploadPicture = findViewById(R.id.buttonProfileUploadPicture);
        btn_updateProfilePicture = findViewById(R.id.buttonUpdatePicture);

        text_fullName = findViewById(R.id.textProfileFullName);
        text_registrationYear = findViewById(R.id.textProfileRegistrationYear);
        text_graduationYear = findViewById(R.id.textProfileGraduationYear);
        text_currentCompany = findViewById(R.id.textProfileCurrentCompany);
        text_phoneNumber = findViewById(R.id.textProfilePhoneNumber);
        spinner_degree = findViewById(R.id.spinnerDegree);
        btn_updateProfileInfo = findViewById(R.id.buttonUpdateProfile);

        text_emailAddress = findViewById(R.id.textProfileEmailAddress);
        btn_updateEmailAddress = findViewById(R.id.buttonUpdateEmailAddress);

        text_currentPassword = findViewById(R.id.textProfileCurrentPassword);
        text_newPassword = findViewById(R.id.textProfileNewPassword);
        btn_updatePassword = findViewById(R.id.buttonUpdatePassword);

        btn_logout = findViewById(R.id.textViewLogout);

        btn_menu = findViewById(R.id.buttonProfileMenu);

        btn_menu.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, Menu.class);
            startActivity(menuIntent);
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        firebaseUser = mAuth.getCurrentUser();

        String emailAddress = firebaseUser.getEmail();
        text_emailAddress.setText(emailAddress);

        // SET USER INFORMATION IN CORRESPONDING EDIT TEXT
        db.collection("users")
            .document(firebaseUser.getUid())
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(documentSnapshot -> {
                map = documentSnapshot.getData();
                String fullName = map.get(User.FULL_NAME).toString();
                String registrationYear = map.get(User.REGISTRATION_YEAR).toString();
                String graduationYear = map.get(User.GRADUATION_YEAR).toString();

                text_fullName.setText(fullName);
                text_registrationYear.setText(registrationYear);
                text_graduationYear.setText(graduationYear);

                Object phoneNumber = map.get(User.PHONE_NUMBER);
                if (phoneNumber != null && !TextUtils.isEmpty(phoneNumber.toString()))
                    text_phoneNumber.setText(phoneNumber.toString());

                Object currentCompany = map.get(User.CURRENT_COMPANY);
                if (currentCompany != null)
                    text_currentCompany.setText(currentCompany.toString());

                Object degree = map.get(User.GRADUATION_DEGREE);
                if (degree != null) {
                    // select user's saved degree on the spinner
                    int i = 0;
                    String[] degreesList = getResources().getStringArray(R.array.degree_types);
                    while (i < degreesList.length && !degree.equals(degreesList[i])) i++;

                    if (i < degreesList.length) {
                        spinner_degree.setSelection(i);
                    }
                }
            });

        // SET PROFILE PICTURE AS IMAGE VIEW
        final File profilePictureFile = CacheUtils.getCacheChildDir(this, firebaseUser.getUid());
        Bitmap profilePicture = CacheUtils.getBitmap(profilePictureFile);

        if (profilePicture != null) {
            image_profilePicture.setImageBitmap(profilePicture);
        } else {
            storage.getReference()
                .child("profiles/" + firebaseUser.getUid() + ".jpg")
                .getBytes(CameraUtils.TWO_MEGABYTES)
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    CacheUtils.setBitmap(profilePictureFile, bitmap);
                    image_profilePicture.setImageBitmap(bitmap);
                });
        }

        // SET SPINNER VALUES FOR STUDENT DEGREE
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
            .createFromResource(this, R.array.degree_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_degree.setAdapter(adapter);
        spinner_degree.setOnItemSelectedListener(this);

        // TAKE PICTURE USING CAMERA
        ActivityResultLauncher<Intent> takePictureLauncher = CameraUtils
            .configureTakePictureLauncher(this, croppedBitmap -> image_profilePicture.setImageBitmap(croppedBitmap));

        btn_takePicture.setOnClickListener(view -> {
            CameraUtils.askCameraPermissions(this);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (getPackageManager().resolveActivity(intent, 0) != null) {
                takePictureLauncher.launch(intent);
            } else {
                Toast.makeText(this, R.string.no_app_supporting, Toast.LENGTH_SHORT).show();
            }
        });

        // UPLOAD PICTURE FROM EXTERNAL SOURCES
        ActivityResultLauncher<Intent> uploadPictureLauncher = CameraUtils
            .configureUploadPictureLauncher(this, croppedBitmap -> image_profilePicture.setImageBitmap(croppedBitmap));

        btn_uploadPicture.setOnClickListener(view -> {
            CameraUtils.askGalleryPermissions(this);

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            if (getPackageManager().resolveActivity(intent, 0) != null) {
                uploadPictureLauncher.launch(intent);
            } else {
                Toast.makeText(this, R.string.no_app_supporting, Toast.LENGTH_SHORT).show();
            }
        });

        btn_updateProfilePicture.setOnClickListener(view -> {
            Bitmap updatedProfilePictureBitmap = ((BitmapDrawable) image_profilePicture.getDrawable()).getBitmap();
            byte[] updatedProfilePictureBytes = getBitmapData(updatedProfilePictureBitmap);

            storage.getReference()
                .child("profiles/" + firebaseUser.getUid() + ".jpg")
                .putBytes(updatedProfilePictureBytes)
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(this, "Profil fotoğrafı başarıyla güncellendi!", Toast.LENGTH_SHORT).show();

                    CacheUtils.setBitmap(profilePictureFile, updatedProfilePictureBitmap);

                    Intent mainPageIntent = new Intent(this, MainPage.class);
                    startActivity(mainPageIntent);
                });
        });

        // UPDATE USER INFORMATION SECTION
        btn_updateProfileInfo.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            Integer registrationYear = Integer.parseInt(text_registrationYear.getText().toString());
            Integer graduationYear = Integer.parseInt(text_graduationYear.getText().toString());
            String phoneNumber = text_phoneNumber.getText().toString();
            String currentCompany = text_currentCompany.getText().toString();

            String[] degreesList = getResources().getStringArray(R.array.degree_types);
            String selectedDegree = degreesList[this.selectedDegreeIndex];

            String uid = firebaseUser.getUid();

            map.put(User.FULL_NAME, fullName);
            map.put(User.REGISTRATION_YEAR, registrationYear);
            map.put(User.GRADUATION_YEAR, graduationYear);
            map.put(User.PHONE_NUMBER, phoneNumber);
            map.put(User.CURRENT_COMPANY, currentCompany);
            map.put(User.GRADUATION_DEGREE, selectedDegree);

            db.collection("users")
                .document(uid)
                .update(map)
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profil bilgileri başarıyla güncellendi.", Toast.LENGTH_SHORT).show();

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                    firebaseUser.updateProfile(profileChangeRequest);

                    Intent mainPageIntent = new Intent(this, MainPage.class);
                    startActivity(mainPageIntent);
                });
        });

        // UPDATE EMAIL ADDRESS SECTION
        btn_updateEmailAddress.setOnClickListener(view -> {
            String newEmailAddress = text_emailAddress.getText().toString();
            firebaseUser.verifyBeforeUpdateEmail(newEmailAddress)
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> db.collection("users")
                    .document(firebaseUser.getUid())
                    .update(User.EMAIL_ADDRESS, newEmailAddress)
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(unused1 -> redirectToLogin()));
        });

        // UPDATE PASSWORD SECTION
        btn_updatePassword.setOnClickListener(view -> {
            if (firebaseUser == null) {
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

        btn_logout.setOnClickListener(view -> {
            mAuth.signOut();

            Toast.makeText(this, "Başarıyla çıkış yapıldı!", Toast.LENGTH_SHORT).show();

            Intent loginIntent = new Intent(this, LoginPage.class);
            startActivity(loginIntent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CameraUtils.CAMERA_PERM_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.camera_permission_required, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedDegreeIndex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedDegreeIndex = 0;
    }

    private void redirectToLogin() {
        Toast.makeText(this, "Lütfen tekrar giriş yapınız.", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(this, LoginPage.class);
        startActivity(loginIntent);
    }

    private byte[] getBitmapData(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}