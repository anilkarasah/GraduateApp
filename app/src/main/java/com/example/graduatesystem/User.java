package com.example.graduatesystem;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private static ArrayList<User> users = new ArrayList<User>() {
        {
            add(new User("admin", "admin", null, null));
        }
    };

    private String username;
    private String password;
    private Date birthDate;
    private Date graduationDate;

    public User(String username, String password, Date birthDate, Date graduationDate) {
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.graduationDate = graduationDate;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static boolean addUser(User user) {
        if (getUser(user.username) != null) {
            return false;
        }

        return users.add(user);
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }
}
