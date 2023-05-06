package com.example.graduatesystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduatesystem.entities.Model;
import com.example.graduatesystem.entities.User;
import com.example.graduatesystem.utils.RecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class MainPage extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private final ArrayList<Model> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerView);

        models.add(new Model(Model.TYPE_POST, 0, "Bu bir gönderidir!"));
        models.add(new Model(Model.TYPE_POST, 0, "Bu başka bir gönderi!"));
        models.add(new Model(Model.TYPE_ANNOUNCEMENT, 0, "Bu da görselli bir gönderi!"));
        models.add(new Model(Model.TYPE_POST, 0, "Lorem ipsum dolor sit amet!"));
        models.add(new Model(Model.TYPE_POST, 0, "Bir şeyler deniyorum!"));
        models.add(new Model(Model.TYPE_ANNOUNCEMENT, 0, "Bu da başka bir görselli gönderi!"));
        models.add(new Model(Model.TYPE_POST, 0, "Bunu da ekleyelim!"));


        db.collection("users")
            .get()
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
            .addOnSuccessListener(querySnapshot -> {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Map<String, Object> map = document.getData();
                    String fullName = map.get(User.FULL_NAME).toString();
                    int registrationYear = Integer.parseInt(map.get(User.REGISTRATION_YEAR).toString());
                    int graduationYear = Integer.parseInt(map.get(User.GRADUATION_YEAR).toString());

                    Model model = new Model(
                        Model.TYPE_USER,
                        0,
                        String.format("%s (%d-%d)", fullName, registrationYear, graduationYear)
                    );

                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(models, this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(recyclerAdapter);
                }
            });
    }
}