package com.example.graduatesystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatesystem.entities.Post;
import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CameraUtils;
import com.example.graduatesystem.utils.PostAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainPage extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private final ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        PostAdapter postAdapter = new PostAdapter(posts, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postAdapter);

        db.collection("posts")
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(querySnapshot -> {
                Log.i("mainPage", "onCreate: query başına geldik!");
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Map<String, Object> map = document.getData();

                    String text = map.get(Post.TEXT).toString();
                    String authorId = map.get(Post.AUTHOR_ID).toString();
                    Date postedAt = document.getTimestamp(Post.POSTED_AT).toDate();
                    Date validUntil = document.getTimestamp(Post.VALID_UNTIL).toDate();

                    Post post = new Post(Post.TEXT_TYPE, authorId, text, postedAt, validUntil);
                    posts.add(post);

                    db.collection("users")
                        .document(authorId)
                        .get()
                        .addOnFailureListener(e -> Log.i("MainPage/GetAuthor", e.getMessage()))
                        .addOnSuccessListener(snapshot -> {
                            String fullName = snapshot.getData().get(User.FULL_NAME).toString();
                            post.setAuthorFullName(fullName);

                            postAdapter.notifyItemChanged(posts.indexOf(post));
                        });

                    storage.getReference()
                        .child("profiles/" + authorId + ".jpg")
                        .getBytes(CameraUtils.TWO_MEGABYTES)
                        .addOnFailureListener(e -> Log.i("MainPage/Avatar", e.getMessage()))
                        .addOnSuccessListener(bytes -> {
                            Bitmap authorProfilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            post.setAuthorProfilePicture(authorProfilePicture);

                            postAdapter.notifyItemChanged(posts.indexOf(post));
                        });
                }
            });
    }
}