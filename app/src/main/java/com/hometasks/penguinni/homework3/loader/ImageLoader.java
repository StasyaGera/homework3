package com.hometasks.penguinni.homework3.loader;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by penguinni on 29.11.16.
 */

class ImageLoader extends AsyncTask<Pair<URL, File>, Integer, Void> {

    @Override
    protected Void doInBackground(Pair<URL, File>... pairs) {
        HttpURLConnection connection;

        try {
            Log.d(TAG, "Loading image");

            connection = (HttpURLConnection) (pairs[0].first).openConnection();
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private final String TAG = "ImageLoader";
}
