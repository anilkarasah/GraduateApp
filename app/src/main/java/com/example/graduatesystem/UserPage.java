package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.Map;

public class UserPage extends AppCompatActivity {
    private User user;

    private ImageView image_profilePicture;
    private TextView text_fullName;
    private TextView text_registrationYear;
    private TextView text_graduationYear;
    private TextView text_degree;
    private TextView text_company;

    private Button btn_email;
    private Button btn_message;

    private String emailAddress;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        User.assertAuthentication(this);

        Bundle bundle = getIntent().getExtras();
        user = bundle.getParcelable("user");

        text_fullName = findViewById(R.id.textUserPageFullName);
        text_registrationYear = findViewById(R.id.textUserPageRegistrationYear);
        text_graduationYear = findViewById(R.id.textUserPageGraduationYear);
        text_degree = findViewById(R.id.textUserPageGraduationDegree);
        text_company = findViewById(R.id.textUserPageCompany);
        image_profilePicture = findViewById(R.id.imageUserPageAvatar);

        btn_email = findViewById(R.id.buttonSendEmail);
        btn_message = findViewById(R.id.buttonSendMessage);

        btn_email.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Size Sormam Gereken Bir Şey Var!");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Bana müsait olduğunuzda dönebilirseniz sevinirim!");
            startActivity(Intent.createChooser(emailIntent, "Email uygulamasını seçin"));
        });
        btn_message.setOnClickListener(view -> {
            Intent messageIntent = new Intent(Intent.ACTION_SENDTO);
            messageIntent.setData(Uri.parse("smsto:" + phoneNumber));
            messageIntent.putExtra("sms_body", "Merhaba! Bana müsait olduğunuzda dönebilirseniz sevinirim!");
            startActivity(Intent.createChooser(messageIntent, "SMS uygulamasını seçin"));
        });

        String uid = user.getUid();

        if (uid == null) {
            Intent mainPageIntent = new Intent(this, MainPage.class);
            Toast.makeText(this, "Bir sorun oluştu.", Toast.LENGTH_SHORT).show();
            startActivity(mainPageIntent);
            return;
        }

        String fullName = user.getFullName();
        int registrationYear = user.getRegistrationYear();
        int graduationYear = user.getGraduationYear();
        String degree = user.getGraduationDegree();
        String company = user.getCurrentCompany();
        emailAddress = user.getEmailAddress();
        phoneNumber = user.getPhoneNumber();

        text_fullName.setText(fullName);
        text_registrationYear.setText(String.valueOf(registrationYear));
        text_graduationYear.setText(String.valueOf(graduationYear));
        text_degree.setText(degree);
        text_company.setText(company);

        File file = CacheUtils.getCacheChildDir(this, uid);
        Bitmap bitmap = CacheUtils.getBitmap(file);
        image_profilePicture.setImageBitmap(bitmap);
    }
}