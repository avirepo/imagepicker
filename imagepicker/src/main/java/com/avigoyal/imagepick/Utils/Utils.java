package com.avigoyal.imagepick.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Class Utils created on 03/05/17 - 9:50 AM.
 * All copyrights reserved to the Quovantis.
 * Class behaviour is
 */

public class Utils {

    /**
     * Method will check if all the permission are granted to app or not
     * if any of the permission is not granted from provided one the method will return false else return true
     * @param context Context
     * @param permissions Permission array
     * @return true if all permission are granted else return false
     */
    public static boolean hasPermission(Context context, @Nullable String... permissions) {
        if (null != context && null != permissions) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
