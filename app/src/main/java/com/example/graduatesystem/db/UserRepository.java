package com.example.graduatesystem.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.graduatesystem.entities.User;

public class UserRepository {
    public static final String USER_TABLE_NAME = "user";
    public static final String ID_COL = "id";
    public static final String FULL_NAME_COL = "full_name";
    public static final String EMAIL_ADDRESS_COL = "username";
    public static final String PASSWORD_COL = "password";
    public static final String REGISTER_YEAR_COL = "register_year";
    public static final String GRADUATION_YEAR_COL = "graduation_year";

    public static final String USER_TABLE_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (" +
            ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FULL_NAME_COL + " TEXT, " +
            EMAIL_ADDRESS_COL + " TEXT, " +
            PASSWORD_COL + " TEXT, " +
            REGISTER_YEAR_COL + " INTEGER, " +
            GRADUATION_YEAR_COL + " INTEGER)";

    public static ContentValues addUserContentValues(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FULL_NAME_COL, user.getFullName());
        contentValues.put(EMAIL_ADDRESS_COL, user.getEmailAddress());
        contentValues.put(PASSWORD_COL, user.getPassword());
        contentValues.put(REGISTER_YEAR_COL, user.getRegisterYear());
        contentValues.put(GRADUATION_YEAR_COL, user.getGraduationYear());

        return contentValues;
    }

    public static User getUser(SQLiteDatabase database, int id) {
        Cursor cursor = database.rawQuery(
                "SELECT full_name, username, password, register_year, graduation_year " +
                        "FROM " + USER_TABLE_NAME + " WHERE id = ? LIMIT 1",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(0);
            String username = cursor.getString(1);
            String password = cursor.getString(2);
            int registerYear = cursor.getInt(3);
            int graduationYear = cursor.getInt(4);

            cursor.close();

            return new User(id, fullName, username, password, registerYear, graduationYear);
        }

        return null;
    }
}
