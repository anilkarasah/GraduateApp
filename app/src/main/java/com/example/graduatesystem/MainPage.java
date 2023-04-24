package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {

    TextView text_id;
    TextView text_fullName;
    TextView text_username;
    TextView text_password;
    TextView text_registerYear;
    TextView text_graduationYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        text_id = (TextView) findViewById(R.id.textViewId);
        text_fullName = (TextView) findViewById(R.id.textViewFullName);
        text_username = (TextView) findViewById(R.id.textViewUsername);
        text_password = (TextView) findViewById(R.id.textViewPassword);
        text_registerYear = (TextView) findViewById(R.id.textViewRegisterYear);
        text_graduationYear = (TextView) findViewById(R.id.textViewGraduationYear);

//        if (user == null) {
//            Toast.makeText(getApplicationContext(), "Kullanıcı bulunamadı.", Toast.LENGTH_SHORT).show();
//        } else {
//            text_id.setText(String.valueOf(user.getId()));
//            text_fullName.setText(user.getFullName());
//            text_username.setText(user.getEmailAddress());
//            text_password.setText(user.getPassword());
//            text_registerYear.setText(String.valueOf(user.getRegisterYear()));
//            text_graduationYear.setText(String.valueOf(user.getGraduationYear()));
//        }
    }
}