package com.example.graduatesystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CameraUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SignupPage extends AppCompatActivity {
    private EditText text_fullName;
    private EditText text_emailAddress;
    private EditText text_password;
    private EditText text_registrationYear;
    private EditText text_graduationYear;
    private Button btn_signup;
    private TextView text_login;
    private ImageView image_avatar;
    private Button btn_takePicture;
    private Button btn_uploadPicture;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        text_fullName = (EditText) findViewById(R.id.textSignupFullName);
        text_emailAddress = (EditText) findViewById(R.id.textSignupEmailAddress);
        text_password = (EditText) findViewById(R.id.editTextPassword);
        text_registrationYear = (EditText) findViewById(R.id.editTextRegisterYear);
        text_graduationYear = (EditText) findViewById(R.id.editTextGraduationYear);
        text_login = (TextView) findViewById(R.id.textViewLoginButton);
        image_avatar = (ImageView) findViewById(R.id.imageViewAvatar);

        Intent loginActivity = new Intent(this, LoginPage.class);

        text_login.setOnClickListener(view -> startActivity(loginActivity));

        btn_signup = (Button) findViewById(R.id.buttonSignup);
        btn_signup.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();
            int registrationYear = Integer.parseInt(text_registrationYear.getText().toString());
            int graduationYear = Integer.parseInt(text_graduationYear.getText().toString());

            if (!validateUserData(fullName, emailAddress)) {
                return;
            }

            signupUserToAuthentication(emailAddress, password);

            User user = new User(fullName, emailAddress, registrationYear, graduationYear);
            createUserRecordToFirestore(user);

            uploadImageToStorage();

            startActivity(loginActivity);
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }

                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                image_avatar.setImageBitmap(bitmap);
            }
        );

        btn_takePicture = (Button) findViewById(R.id.buttonTakePicture);
        btn_takePicture.setOnClickListener(view -> {
            CameraUtils.askCameraPermissions(this);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                activityResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, R.string.no_app_supporting, Toast.LENGTH_SHORT).show();
            }
        });

        btn_uploadPicture = (Button) findViewById(R.id.buttonUploadPicture);
        btn_uploadPicture.setOnClickListener(view ->
            Toast.makeText(getApplicationContext(), "Resim yüklenecek!", Toast.LENGTH_SHORT).show());
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

    private void uploadImageToStorage() {
        Bitmap bitmap = ((BitmapDrawable) image_avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] data = baos.toByteArray();

        StorageReference reference = storage.getReference()
            .child("profiles/" + this.uid + ".jpg");

        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(e ->
            Toast.makeText(SignupPage.this, "Profil fotoğrafı yüklenirken hata oluştu.", Toast.LENGTH_SHORT).show());

        Log.d("uploadImage", "ulaştık");
    }

    private boolean validateUserData(String fullName, String emailAddress) {
        if (!User.validateFullName(fullName)) {
            Toast.makeText(getApplicationContext(), "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!User.validateEmailAddress(emailAddress)) {
            Toast.makeText(getApplicationContext(), "Lütfen geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signupUserToAuthentication(String emailAddress, String password) {
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    this.uid = task.getResult().getUser().getUid();
                }
            });
    }

    private void createUserRecordToFirestore(User user) {
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

                Log.d("createUser", "ulaştık");
            });
    }
}
