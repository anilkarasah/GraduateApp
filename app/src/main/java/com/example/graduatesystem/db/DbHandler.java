package com.example.graduatesystem.db;

import android.content.ContentValues;
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

    public void addNewUser(User user) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = UserRepository.addUserContentValues(user);

        database.insert(UserRepository.USER_TABLE_NAME, null, contentValues);
        database.close();
    }

    public User getUser(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        return UserRepository.getUser(database, id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserRepository.USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostRepository.POST_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
