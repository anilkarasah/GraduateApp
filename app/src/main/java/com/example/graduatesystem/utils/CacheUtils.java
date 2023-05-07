package com.example.graduatesystem.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class CacheUtils {
    public static File getCacheChildDir(AppCompatActivity activity, String child) {
        File cacheDir = activity.getCacheDir();

        return new File(cacheDir, child);
    }

    public static Bitmap getBitmap(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Bitmap authorProfilePicture = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            return authorProfilePicture;
        } catch (FileNotFoundException e) {
            Log.i("CacheUtils", "getBitmapFromCache: FileNotFound: " + e.getMessage());
        } catch (Exception e) {
            Log.i("CacheUtils", "getBitmapFromCache: Exception: " + e.getMessage());
        }

        return null;
    }

    public static void setBitmap(File file, Bitmap bitmap) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.i("CacheUtils", "setBitmapToCache: FileNotFound: " + e.getMessage());
        } catch (Exception e) {
            Log.i("CacheUtils", "setBitmapToCache: Exception: " + e.getMessage());
        }
    }
}
