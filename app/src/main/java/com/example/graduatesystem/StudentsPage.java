package com.example.graduatesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.CacheUtils;
import com.example.graduatesystem.utils.CameraUtils;
import com.example.graduatesystem.utils.UserAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
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

        UserAdapter userAdapter = new UserAdapter(users, this, user -> {
            Intent userPageIntent = new Intent(this, UserPage.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            userPageIntent.putExtras(bundle);
//            userPageIntent.putExtra("uid", user.getUid());
            this.startActivity(userPageIntent);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && e.getAction() == MotionEvent.ACTION_UP) {
                    int position = rv.getChildAdapterPosition(childView);
                    User clickedUser = users.get(position);

                    Intent intent = new Intent(StudentsPage.this, UserPage.class);
                    intent.putExtra("uid", clickedUser.getUid());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

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
                    String phoneNumber = map.get(User.PHONE_NUMBER).toString();
                    String currentCompany = map.get(User.CURRENT_COMPANY).toString();
                    String degree = map.get(User.GRADUATION_DEGREE).toString();
                    String emailAddress = map.get(User.EMAIL_ADDRESS).toString();

                    User user = new User(uid, fullName, registrationYear, graduationYear, phoneNumber, currentCompany, degree, emailAddress);

                    final File profilePictureFile = CacheUtils.getCacheChildDir(this, uid);
                    Bitmap cachedProfilePicture = CacheUtils.getBitmap(profilePictureFile);

                    if (cachedProfilePicture != null) {
                        user.profilePicture = cachedProfilePicture;
                        userAdapter.notifyItemChanged(users.indexOf(user));
                        users.add(user);
                    } else {
                        storage.getReference().child("profiles/" + uid + ".jpg").getBytes(CameraUtils.TWO_MEGABYTES).addOnFailureListener(e -> Log.i("StudentsPage/Storage", e.getMessage())).addOnSuccessListener(bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            CacheUtils.setBitmap(profilePictureFile, bitmap);

                            user.profilePicture = bitmap;
                            users.add(user);
                            userAdapter.notifyItemChanged(users.indexOf(user));
                        });
                    }
                }
            });
    }
}