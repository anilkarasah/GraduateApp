package com.example.graduatesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduatesystem.db.UserRepository;
import com.example.graduatesystem.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupPage extends AppCompatActivity {
    private EditText text_fullName;
    private EditText text_emailAddress;
    private EditText text_password;
    private EditText text_registerYear;
    private EditText text_graduationYear;
    private Button btn_signup;
    private TextView text_login;

    private FirebaseAuth mAuth;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        mAuth = FirebaseAuth.getInstance();

        userRepository = new UserRepository(getApplicationContext());

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

            User user = new User(
                    fullName,
                    emailAddress,
                    password,
                    registerYear,
                    graduationYear);

            mAuth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Kayıt başarısız!", Toast.LENGTH_LONG).show();
                                return;
                            }

//                            user.setUid(mAuth.getCurrentUser().getUid());

                            userRepository.createUser(user);

                            Toast.makeText(getApplicationContext(), "Başarıyla kayıt oldunuz!", Toast.LENGTH_LONG).show();
                            Intent loginActivity = new Intent(getApplicationContext(), LoginPage.class);
                            startActivity(loginActivity);
                        }
                    });
        });
    }
}