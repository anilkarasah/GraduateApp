package com.example.graduatesystem.entities;

import android.text.TextUtils;

public class User {
    public static final String UID = "uid";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String REGISTRATION_YEAR = "registrationYear";
    public static final String GRADUATION_YEAR = "graduationYear";

    private String uid;
    private String fullName;
    private String emailAddress;
    private int registrationYear;
    private int graduationYear;

    public User() {
    }

    public User(String uid, String fullName, String emailAddress, int registrationYear, int graduationYear) {
        this.uid = uid;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
    }

    public User(String fullName, String emailAddress, int registrationYear, int graduationYear) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.registrationYear = registrationYear;
        this.graduationYear = graduationYear;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public static boolean validateFullName(String fullName) {
        return !TextUtils.isEmpty(fullName);
    }

    public static boolean validateEmailAddress(String emailAddress) {
        if (TextUtils.isEmpty(emailAddress)) {
            return false;
        }

        return emailAddress.split("@").length == 2;
    }

    public static boolean validatePassword(String password) {
        return !TextUtils.isEmpty(password);
    }

    public static boolean validateRegisterAndGraduationYears(int registerYear, int graduationYear) {
        return graduationYear > registerYear;
    }
}
