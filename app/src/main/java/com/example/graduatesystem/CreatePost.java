package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduatesystem.entities.Post;
import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreatePost extends AppCompatActivity {
    private Button btn_menu;

    private EditText text_postText;
    private Button btn_create;

    private String selectedDate;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        User.assertAuthentication(this);

        btn_menu = findViewById(R.id.buttonCreatePostMenu);
        text_postText = findViewById(R.id.textCreatePost);
        btn_create = findViewById(R.id.buttonCreatePost);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_menu.setOnClickListener(view -> {
            Intent menuIntent = new Intent(this, Menu.class);
            startActivity(menuIntent);
        });

        btn_create.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);

            Toast.makeText(this, "Duyurunun bitiş tarihini seçiniz", Toast.LENGTH_LONG).show();

            DatePickerDialog dialog = new DatePickerDialog(
                CreatePost.this,
                (datePicker, year, month, day) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, day);

                    String text = text_postText.getText().toString();
                    String userId = mAuth.getCurrentUser().getUid();

                    Date currentDate = new Date();
                    Timestamp postedAtTimestamp = new Timestamp(currentDate.getTime());
                    Timestamp validUntilTimestamp = new Timestamp(selectedCalendar.getTimeInMillis());

                    Map<String, Object> post = new HashMap<>();
                    post.put(Post.TEXT, text);
                    post.put(Post.AUTHOR_ID, userId);
                    post.put(Post.POSTED_AT, postedAtTimestamp);
                    post.put(Post.VALID_UNTIL, validUntilTimestamp);

                    db.collection("posts")
                        .document()
                        .set(post)
                        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Duyuru başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainPage.class);
                            startActivity(intent);
                        });
                }, mYear, mMonth, mDay);

            dialog.show();
        });
    }
}