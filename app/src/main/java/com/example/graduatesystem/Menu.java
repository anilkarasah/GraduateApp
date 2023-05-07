package com.example.graduatesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class Menu extends AppCompatActivity {
    LinearLayout layout_profile;
    ImageView image_profilePicture;
    TextView text_fullName;

    TextView text_mainPage;
    TextView text_myPosts;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        User.assertAuthentication(this);

        layout_profile = findViewById(R.id.menuProfileLayout);
        image_profilePicture = findViewById(R.id.imageMenuAvatar);
        text_fullName = findViewById(R.id.textMenuFullName);
        text_mainPage = findViewById(R.id.textMenuMainPage);
        text_myPosts = findViewById(R.id.textMenuMyPosts);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // NAVIGATIONS
        layout_profile.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, Profile.class);
            startActivity(profileIntent);
        });

        text_mainPage.setOnClickListener(view -> {
            Intent mainPageIntent = new Intent(this, MainPage.class);
            startActivity(mainPageIntent);
        });

        text_myPosts.setOnClickListener(view -> {
            Intent myPostsIntent = new Intent(this, MyPosts.class);
            startActivity(myPostsIntent);
        });

        // IMPORT DATA FOR PROFILE SECTION
        final FirebaseUser user = mAuth.getCurrentUser();
        final String uid = user.getUid();
        final String fullName = user.getDisplayName();

        final String userCacheKey = String.format("user-%s", uid);
        if (fullName != null) {
            text_fullName.setText(fullName);
        }

        final File profilePictureFile = CacheUtils.getCacheChildDir(this, uid);
        Bitmap bitmap = CacheUtils.getBitmap(profilePictureFile);

        if (bitmap != null) {
            image_profilePicture.setImageBitmap(bitmap);
        } else {
            storage.getReference()
                .child("profiles/" + uid + ".jpg")
                .getBytes(CameraUtils.TWO_MEGABYTES)
                .addOnFailureListener(e -> Log.i("Menu", e.getMessage()))
                .addOnSuccessListener(bytes -> {
                    Bitmap authorProfilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    CacheUtils.setBitmap(profilePictureFile, authorProfilePicture);
                    image_profilePicture.setImageBitmap(authorProfilePicture);
                });
        }
    }
}