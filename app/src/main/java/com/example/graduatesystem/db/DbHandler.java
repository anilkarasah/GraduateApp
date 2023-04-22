package com.example.graduatesystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.graduatesystem.entities.User;

public class DbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "graduateApp";
    private static final int DB_VERSION = 1;

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(UserRepository.USER_TABLE_CREATE_QUERY);
        sqLiteDatabase.execSQL(PostRepository.POST_TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserRepository.USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostRepository.POST_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
