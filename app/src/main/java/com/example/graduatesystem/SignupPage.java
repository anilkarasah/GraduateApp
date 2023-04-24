package com.example.graduatesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupPage extends AppCompatActivity {
    private EditText text_fullName;
    private EditText text_emailAddress;
    private EditText text_password;
    private EditText text_registerYear;
    private EditText text_graduationYear;
    private Button btn_signup;
    private TextView text_login;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        text_fullName = (EditText) findViewById(R.id.textSignupFullName);
        text_emailAddress = (EditText) findViewById(R.id.textSignupEmailAddress);
        text_password = (EditText) findViewById(R.id.editTextPassword);
        text_registerYear = (EditText) findViewById(R.id.editTextRegisterYear);
        text_graduationYear = (EditText) findViewById(R.id.editTextGraduationYear);
        text_login = (TextView) findViewById(R.id.textViewLoginButton);

        Intent loginActivity = new Intent(getApplicationContext(), LoginPage.class);

        text_login.setOnClickListener(view -> startActivity(loginActivity));

        btn_signup = (Button) findViewById(R.id.buttonSignup);
        btn_signup.setOnClickListener(view -> {
            String fullName = text_fullName.getText().toString();
            String emailAddress = text_emailAddress.getText().toString();
            String password = text_password.getText().toString();
            int registerYear = Integer.parseInt(text_registerYear.getText().toString());
            int graduationYear = Integer.parseInt(text_graduationYear.getText().toString());

            if (!User.validateFullName(fullName)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir isim giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validateEmailAddress(emailAddress)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!User.validatePassword(password)) {
                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir parola giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Kayıt başarısız!", Toast.LENGTH_LONG).show();
                    return;
                }

                String uid = mAuth.getCurrentUser().getUid();
                User user = new User(
                    uid,
                    fullName,
                    emailAddress,
                    password,
                    registerYear,
                    graduationYear);

                db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .set(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Başarıyla kayıt oldunuz!", Toast.LENGTH_LONG).show();
                        Intent loginActivity1 = new Intent(getApplicationContext(), LoginPage.class);
                        startActivity(loginActivity1);
                    })
                    .addOnFailureListener(e ->
                        Toast.makeText(
                                getApplicationContext(),
                                "Kayıt işlemi başarısız oldu!",
                                Toast.LENGTH_LONG)
                            .show());
            });
        });
    }
}
