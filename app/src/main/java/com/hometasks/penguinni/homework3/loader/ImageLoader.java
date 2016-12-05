package com.hometasks.penguinni.homework3.loader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.util.Log;

import com.hometasks.penguinni.homework3.HW3Activity;
import com.hometasks.penguinni.homework3.Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by penguinni on 29.11.16.
 */

class ImageLoader extends AsyncTask<Pair<URL, File>, Integer, Void> {

    private final Context context;

    ImageLoader(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Pair<URL, File>... pairs) {
        HttpURLConnection connection;

        if (!Util.isConnectionAvailable(context, false)) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.no_internet"));
            return null;
        }

        try {
            Log.d(TAG, "Loading image");

            connection = (HttpURLConnection) (pairs[0].first).openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                connection.setConnectTimeout((int) 15e3);
                connection.setReadTimeout((int) 6e4);

                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(pairs[0].second));

                byte[] buffer = new byte[8192];
                int size;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

                in.close();
                out.close();
            } else {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.error_occurred"));
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.image_downloaded"));

        return null;
    }

    private final String TAG = "ImageLoader";
}
