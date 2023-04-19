package com.example.graduatesystem.entities;

public class User {
//    private static final ArrayList<User> users = new ArrayList<User>() {
//        {
//            add(new User("Yönetici", "admin", "admin", 2019, 2024));
//        }
//    };

    private int id;
    private String fullName;
    private String username;
    private String password;
    private int registerYear;
    private int graduationYear;

    public User(int id, String fullName, String username, String password, int registerYear, int graduationYear) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.registerYear = registerYear;
        this.graduationYear = graduationYear;
    }

    public User(String fullName, String username, String password, int registerYear, int graduationYear) {
        this.fullName = fullName;
        this.username = username;
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

//    public static ArrayList<User> getUsers() {
//        return users;
//    }
//
//    public static User getUser(String username) {
//        for (User user : users) {
//            if (user.username.equals(username)) {
//                return user;
//            }
//        }
//
//        return null;
//    }
//
//    public static boolean addUser(User user) {
//        if (getUser(user.username) != null) {
//            return false;
//        }
//
//        return users.add(user);
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public static boolean validateUsername(String username) {
        return username.length() > 0;
    }

    public static boolean validatePassword(String password) {
        return password.length() > 0;
    }

    public static boolean validateRegisterAndGraduationYears(int registerYear, int graduationYear) {
        return graduationYear > registerYear;
    }

    public static final String TABLE_CREATE_QUERY =
            "CREATE TABLE IF NOT EXISTS user" +
                    "";
}
