package com.example.graduatesystem;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MyPosts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btn_createPost;

    private final ArrayList<Post> posts = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        User.assertAuthentication(this);

        recyclerView = findViewById(R.id.myPostsRecyclerView);
        btn_createPost = findViewById(R.id.buttonAddPost);

        btn_createPost.setOnClickListener(view -> {
            Intent createPostIntent = new Intent(this, CreatePost.class);
            startActivity(createPostIntent);
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        final String uid = mAuth.getCurrentUser().getUid();

        PostAdapter postAdapter = new PostAdapter(posts, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postAdapter);
        db.collection("posts")
            .whereEqualTo(Post.AUTHOR_ID, uid)
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

                    String fullName = mAuth.getCurrentUser().getDisplayName();
                    post.setAuthorFullName(fullName);
                    postAdapter.notifyItemChanged(posts.indexOf(post));

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