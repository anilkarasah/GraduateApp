package com.example.graduatesystem.entities;

public class User {

    private int id;
    private String fullName;
    private String emailAddress;
    private String password;
    private int registerYear;
    private int graduationYear;

    public User(int id, String fullName, String emailAddress, String password, int registerYear, int graduationYear) {
        this.id = id;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
        return fullName.length() > 0;
    }

    public static boolean validateEmailAddress(String emailAddress) {
        if (emailAddress.split("@").length != 2) {
            return false;
        }

        return emailAddress.length() > 0;
    }

    public static boolean validatePassword(String password) {
        return password.length() > 0;
    }

    public static boolean validateRegisterAndGraduationYears(int registerYear, int graduationYear) {
        return graduationYear > registerYear;
    }
}
