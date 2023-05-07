package com.example.graduatesystem.entities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.graduatesystem.LoginPage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import javax.annotation.Nullable;

public class User implements Parcelable {
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
    @Nullable
    private String emailAddress;

    @Nullable
    public Bitmap profilePicture;

    public User() {
    }

    public User(String uid, String fullName, int registrationYear, int graduationYear, @Nullable String phoneNumber, @Nullable String currentCompany, @Nullable String degree, @Nullable String emailAddress) {
        this.uid = uid;
        this.fullName = fullName;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
        this.phoneNumber = phoneNumber;
        this.currentCompany = currentCompany;
        this.emailAddress = emailAddress;

        if (degree == null || TextUtils.isEmpty(degree)) {
            this.graduationDegree = "Lisans";
        } else {
            this.graduationDegree = degree;
        }
    }

    public User(String fullName, int registrationYear, int graduationYear, @Nullable String phoneNumber, @Nullable String currentCompany, @Nullable String degree, @Nullable String emailAddress) {
        this.fullName = fullName;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
        this.phoneNumber = phoneNumber;
        this.currentCompany = currentCompany;
        this.emailAddress = emailAddress;

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

    @Nullable
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(@Nullable String emailAddress) {
        this.emailAddress = emailAddress;
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

    protected User(Parcel in) {
        this.uid = in.readString();
        this.fullName = in.readString();
        this.registrationYear = in.readInt();
        this.graduationYear = in.readInt();
        this.graduationDegree = in.readString();
        this.phoneNumber = in.readString();
        this.currentCompany = in.readString();
        this.emailAddress = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(uid);
        parcel.writeString(fullName);
        parcel.writeInt(registrationYear);
        parcel.writeInt(graduationYear);
        parcel.writeString(graduationDegree);
        parcel.writeString(phoneNumber);
        parcel.writeString(currentCompany);
        parcel.writeString(emailAddress);
    }
}
