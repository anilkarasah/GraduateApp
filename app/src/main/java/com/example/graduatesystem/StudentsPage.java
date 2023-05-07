package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.example.graduatesystem.utils.UserAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class StudentsPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final ArrayList<User> users = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_page);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.usersPageRecyclerView);

        UserAdapter userAdapter = new UserAdapter(users, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        db.collection("users")
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Map<String, Object> map = document.getData();

                    String uid = map.get(User.UID).toString();
                    String fullName = map.get(User.FULL_NAME).toString();
                    int registrationYear = Integer.parseInt(map.get(User.REGISTRATION_YEAR).toString());
                    int graduationYear = Integer.parseInt(map.get(User.GRADUATION_YEAR).toString());

                    User user = new User(uid, fullName, registrationYear, graduationYear, null, null, null);

                    final File profilePictureFile = CacheUtils.getCacheChildDir(this, uid);
                    Bitmap cachedProfilePicture = CacheUtils.getBitmap(profilePictureFile);

                    users.add(user);

                    if (cachedProfilePicture != null) {
                        user.profilePicture = cachedProfilePicture;
                        userAdapter.notifyItemChanged(users.indexOf(user));
                    } else {
                        storage.getReference()
                            .child("profiles/" + uid + ".jpg")
                            .getBytes(CameraUtils.TWO_MEGABYTES)
                            .addOnFailureListener(e -> Log.i("StudentsPage/Storage", e.getMessage()))
                            .addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                CacheUtils.setBitmap(profilePictureFile, bitmap);

                                user.profilePicture = bitmap;
                                userAdapter.notifyItemChanged(users.indexOf(user));
                            });
                    }
                }
            });
    }
}