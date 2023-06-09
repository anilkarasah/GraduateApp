package com.example.graduatesystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatesystem.entities.Post;
import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.example.graduatesystem.utils.PostAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MainPage extends AppCompatActivity {
    private static final int REQUEST_CODE = 10;
    private RecyclerView recyclerView;

    private Button btn_menu;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private final ArrayList<Post> posts = new ArrayList<>();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        User.assertAuthentication(this);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.mainPageRecyclerView);
        btn_menu = findViewById(R.id.buttonMainMenu);

        btn_menu.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, Menu.class);
            startActivity(menuIntent);
        });

        PostAdapter postAdapter = new PostAdapter(posts, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postAdapter);

        preferences = getSharedPreferences("userFullNames", MODE_PRIVATE);

        Date currentDate = new Date();
        db.collection("posts")
            .whereGreaterThan(Post.VALID_UNTIL, currentDate)
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(querySnapshot -> {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Map<String, Object> map = document.getData();

                    String text = map.get(Post.TEXT).toString();
                    String authorId = map.get(Post.AUTHOR_ID).toString();
                    Date postedAt = document.getTimestamp(Post.POSTED_AT).toDate();
                    Date validUntil = document.getTimestamp(Post.VALID_UNTIL).toDate();

                    Post post = new Post(Post.TEXT_TYPE, authorId, text, postedAt, validUntil);
                    posts.add(post);

                    final String userCacheKey = String.format("user-%s", authorId);

                    if (preferences.contains(userCacheKey)) {
                        String fullName = preferences.getString(userCacheKey, "");
                        post.setAuthorFullName(fullName);

                        postAdapter.notifyItemChanged(posts.indexOf(post));
                    } else {
                        // get post author's full name from Firestore
                        // because it does not exist in preferences
                        db.collection("users")
                            .document(authorId)
                            .get()
                            .addOnFailureListener(e -> Log.i("MainPage/GetAuthor", e.getMessage()))
                            .addOnSuccessListener(snapshot -> {
                                String fullName = snapshot.getData().get(User.FULL_NAME).toString();
                                post.setAuthorFullName(fullName);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(userCacheKey, fullName);
                                editor.apply();

                                postAdapter.notifyItemChanged(posts.indexOf(post));
                            });
                    }

                    final File profilePictureFile = CacheUtils.getCacheChildDir(this, authorId);
                    Bitmap cachedProfilePicture = CacheUtils.getBitmap(profilePictureFile);

                    if (cachedProfilePicture != null) {
                        post.setAuthorProfilePicture(cachedProfilePicture);
                        postAdapter.notifyItemChanged(posts.indexOf(post));
                    } else {
                        storage.getReference()
                            .child("profiles/" + authorId + ".jpg")
                            .getBytes(CameraUtils.TWO_MEGABYTES)
                            .addOnFailureListener(err -> Log.i("MainPage/Avatar", err.getMessage()))
                            .addOnSuccessListener(bytes -> {
                                Bitmap authorProfilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                CacheUtils.setBitmap(profilePictureFile, authorProfilePicture);

                                post.setAuthorProfilePicture(authorProfilePicture);
                                postAdapter.notifyItemChanged(posts.indexOf(post));
                            });
                    }
                }
            });
    }
}