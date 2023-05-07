package com.example.graduatesystem.entities;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.LoginPage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import javax.annotation.Nullable;

public class User {
    public static final String UID = "uid";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String REGISTRATION_YEAR = "registrationYear";
    public static final String GRADUATION_YEAR = "graduationYear";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String CURRENT_COMPANY = "currentCompany";
    public static final String GRADUATION_DEGREE = "graduationDegree";

    private String uid;
    private String fullName;
    private int registrationYear;
    private int graduationYear;
    @Nullable
    private String phoneNumber;
    @Nullable
    private String currentCompany;
    @Nullable
    private String graduationDegree;

    public User() {
    }

    public User(String uid, String fullName, int registrationYear, int graduationYear, @Nullable String phoneNumber, @Nullable String currentCompany, @Nullable String degree) {
        this.uid = uid;
        this.fullName = fullName;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
        this.phoneNumber = phoneNumber;
        this.currentCompany = currentCompany;

        if (degree == null || TextUtils.isEmpty(degree)) {
            this.graduationDegree = "Lisans";
        } else {
            this.graduationDegree = degree;
        }
    }

    public User(String fullName, int registrationYear, int graduationYear, @Nullable String phoneNumber, @Nullable String currentCompany, @Nullable String degree) {
        this.fullName = fullName;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
        this.phoneNumber = phoneNumber;
        this.currentCompany = currentCompany;

        if (degree == null || TextUtils.isEmpty(degree)) {
            this.graduationDegree = "Lisans";
        } else {
            this.graduationDegree = degree;
        }
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(int registrationYear) {
        this.registrationYear = registrationYear;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Nullable
    public String getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(@Nullable String currentCompany) {
        this.currentCompany = currentCompany;
    }

    @Nullable
    public String getGraduationDegree() {
        return graduationDegree;
    }

    public void setGraduationDegree(@Nullable String graduationDegree) {
        if (graduationDegree == null || TextUtils.isEmpty(graduationDegree)) {
            this.graduationDegree = "Lisans";
        } else {
            this.graduationDegree = graduationDegree;
        }
    }

    public boolean validateFullName() {
        return !TextUtils.isEmpty(fullName);
    }

    public static boolean validateEmailAddress(String emailAddress) {
        if (TextUtils.isEmpty(emailAddress)) {
            return false;
        }

        return emailAddress.split("@").length == 2;
    }

    public boolean validateRegisterAndGraduationYears() {
        return graduationYear > registrationYear;
    }

    public static void assertAuthentication(AppCompatActivity activity) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(activity, "Lütfen tekrar giriş yapınız.", Toast.LENGTH_SHORT).show();

            Intent loginIntent = new Intent(activity, LoginPage.class);
            activity.startActivity(loginIntent);
        }
    }
}
