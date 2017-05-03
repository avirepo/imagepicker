package com.avigoyal.imagepick.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.avigoyal.imagepick.model.GalleryImage;

import java.util.ArrayList;


/**
 * Author Vikas for the class GalleryUtils on 27/04/16 - 4:24 PM.
 * It is open source class any one can put their suggestions and input in it.
 * Class behaviour is to provide utility for gallery related functionality like fetch
 * all images from device.
 */
public class GalleryUtils {

    /**
     * Method to fetch all gallery images
     * @param context Context
     * @param galleyImageCol Image collection
     * @return List of gallery image
     */
    public static ArrayList<GalleryImage> fetchGalleryImages(Context context, ArrayList<GalleryImage> galleyImageCol) {
        if (null == galleyImageCol) {
            galleyImageCol = new ArrayList<>();
        }

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.ORIENTATION};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        ContentResolver cr = context.getContentResolver();
        Cursor imageCursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");
        if (null != imageCursor) {
            while (imageCursor.moveToNext()) {
                int columnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                if (columnIndex != -1 && columnIndex < imageCursor.getColumnCount()) {
                    String stringUri = imageCursor.getString(columnIndex);
                    if (!TextUtils.isEmpty(stringUri)) {
                        Uri uri = Uri.parse(stringUri);
                        int orientationIndex = imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION);
                        int orientation = ExifInterface.ORIENTATION_NORMAL;
                        if (orientationIndex != -1 && orientationIndex < imageCursor.getColumnCount()) {
                            orientation = imageCursor.getInt(orientationIndex);
                        }
                        galleyImageCol.add(new GalleryImage(uri, orientation));
                    }
                }
            }
            imageCursor.close();
        }
        return galleyImageCol;
    }
}
