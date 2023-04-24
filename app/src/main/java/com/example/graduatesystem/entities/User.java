package com.example.graduatesystem.entities;

import android.text.TextUtils;

public class User {
    private String uid;
    private String fullName;
    private String emailAddress;
    private String password;
    private int registerYear;
    private int graduationYear;

    public User(String uid, String fullName, String emailAddress, String password, int registerYear, int graduationYear) {
        this.uid = uid;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.registerYear = registerYear;
        this.graduationYear = graduationYear;
    }

    public User(String fullName, String emailAddress, String password, int registerYear, int graduationYear) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.registerYear = registerYear;
        this.graduationYear = graduationYear;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRegisterYear() {
        return registerYear;
    }

    public void setRegisterYear(int registerYear) {
        this.registerYear = registerYear;
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
