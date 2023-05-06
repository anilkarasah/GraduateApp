package com.example.graduatesystem.entities;

public class Model {
    public static final int TYPE_POST = 0;
    public static final int TYPE_ANNOUNCEMENT = 1;
    public static final int TYPE_USER = 2;

    public int type;
    public int data;
    public String text;

    public Model(int type, int data, String text) {
        this.type = type;
        this.data = data;
        this.text = text;
    }
}
