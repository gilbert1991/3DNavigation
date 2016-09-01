package com.nyu.gilbert.a3dnavigation.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gilbert on 8/31/16.
 */

public class FileHandler {

    private static final String TAG = "FileHandler";

    public static File createPublicImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg",         // suffix
                storageDir     // directory
        );

        return image;
    }

    public static File createPrivateImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); // private to app only
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg",         // suffix
                storageDir     // directory
        );

        return image;
    }

    public static Intent createTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if(takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createPrivateImageFile(context);
            } catch (IOException io) {
                // Error while creating the File
                Log.e(TAG, "IOException while creating image file.");
            }

            // Continue only if the File was successfully created
            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                                            "com.nyu.gilbert.a3dnavigation.fileprovider",
                                            photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("PHOTO_PATH", "file:" + photoFile.getAbsolutePath());
            }
        }

        return takePictureIntent;
    }

    public static Intent createMediaScanIntent(String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(photoPath));
        mediaScanIntent.setData(contentUri);

        return mediaScanIntent;
    }
}
