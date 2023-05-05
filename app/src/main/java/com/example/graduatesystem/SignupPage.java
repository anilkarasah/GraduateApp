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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        text_fullName = (EditText) findViewById(R.id.textProfileFullName);
        text_emailAddress = (EditText) findViewById(R.id.textProfileEmailAddress);
        text_password = (EditText) findViewById(R.id.textProfileCurrentPassword);
        text_registrationYear = (EditText) findViewById(R.id.textProfileRegistrationYear);
        text_graduationYear = (EditText) findViewById(R.id.textProfileGraduationYear);
        text_login = (TextView) findViewById(R.id.textViewLoginButton);
        image_avatar = (ImageView) findViewById(R.id.imageViewAvatar);

        Intent loginActivity = new Intent(this, LoginPage.class);
        text_login.setOnClickListener(view -> startActivity(loginActivity));

        btn_signup = (Button) findViewById(R.id.buttonUpdateProfile);
        btn_signup.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();
            int registrationYear = Integer.parseInt(text_registrationYear.getText().toString());
            int graduationYear = Integer.parseInt(text_graduationYear.getText().toString());

            User user = new User(fullName, emailAddress, registrationYear, graduationYear, null, null, null);

            if (!validateUserData(user, emailAddress)) {
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnFailureListener(e -> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();

                    String uid = firebaseUser.getUid();
                    user.setUid(uid);
                    db.collection("users")
                        .document(uid)
                        .set(user)
                        .addOnFailureListener(e -> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show());

                    byte[] data = getBitmapData(image_avatar);
                    storage.getReference()
                        .child("profiles/" + uid + ".jpg")
                        .putBytes(data)
                        .addOnFailureListener(e -> Log.d("signup/storage", e.toString()));

                    firebaseUser.sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            Toast.makeText(this, "Başarıyla kayıt oldunuz!", Toast.LENGTH_SHORT).show();
                            startActivity(loginActivity);
                        });
                });
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

        btn_takePicture = (Button) findViewById(R.id.buttonProfileTakePicture);
        btn_takePicture.setOnClickListener(view -> {
            CameraUtils.askCameraPermissions(this);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                activityResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, R.string.no_app_supporting, Toast.LENGTH_SHORT).show();
            }
        });

        btn_uploadPicture = (Button) findViewById(R.id.buttonProfileUploadPicture);
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

    private byte[] getBitmapData(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private boolean validateUserData(User user, String emailAddress) {
        if (!user.validateFullName()) {
            Toast.makeText(getApplicationContext(), "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!User.validateEmailAddress(emailAddress)) {
            Toast.makeText(getApplicationContext(), "Lütfen geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!user.validateRegisterAndGraduationYears()) {
            Toast.makeText(this, "Mezuniyet tarihi, kayıt tarihinden sonra olmalıdır.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
