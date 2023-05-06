package com.example.graduatesystem.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraUtils {
    public static final int CAMERA_PERM_CODE = 101;

    public static void askCameraPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
    }

    public static Bitmap cropBitmapToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width == height) return bitmap;

        int crop;
        Bitmap croppedBitmap;

        if (width > height) {
            crop = (width - height) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, crop, 0, height, height);
        } else {
            crop = (height - width) / 2;
            croppedBitmap = Bitmap.createBitmap(bitmap, 0, crop, width, width);
        }

        return croppedBitmap;
    }
}
