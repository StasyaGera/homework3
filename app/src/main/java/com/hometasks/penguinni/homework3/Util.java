package com.hometasks.penguinni.homework3;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * Created by penguinni on 29.11.16.
 */

public class Util {

    public static boolean isConnectionAvailable(@NonNull Context context, boolean defaultValue) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return defaultValue;
        }
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }
}
