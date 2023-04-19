package com.example.graduatesystem.db;

public class PostRepository {
    public static final String POST_TABLE_NAME = "post";
    public static final String ID_COL = "id";
    public static final String TITLE_COL = "title";
    public static final String DESCRIPTION_COL = "description";

    public static final String POST_TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + POST_TABLE_NAME + " (" +
            ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE_COL + " TEXT, " +
            DESCRIPTION_COL + " TEXT)";
}
