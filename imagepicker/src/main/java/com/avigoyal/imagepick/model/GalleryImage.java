package com.avigoyal.imagepick.model;

import android.net.Uri;


public class GalleryImage {

    public Uri mUri;
    public int mOrientation;

    public GalleryImage(Uri uri, int orientation){
        mUri = uri;

        mOrientation = orientation;
    }
}
