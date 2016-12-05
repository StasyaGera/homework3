package com.hometasks.penguinni.homework3.api;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by penguinni on 29.11.16.
 */

public class VKApi {
    private static Uri BASE_URI = Uri.parse("https://api.vk.com/method");

    private static final String OWNER_ID = "-24125843";
    private static final String ALBUM_ID = "wall";

    public static final int COUNT = 20;

    public static HttpURLConnection getPhotosRequest(int offset) throws IOException {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("photos.get")
                .appendQueryParameter("owner_id", OWNER_ID)
                .appendQueryParameter("album_id", ALBUM_ID)
                .appendQueryParameter("offset", String.valueOf(offset * COUNT))
                .appendQueryParameter("count", String.valueOf(COUNT))
                .appendQueryParameter("rev", "1")
                .build();

        Log.d("VKApi", "built an uri: " + uri.toString());
        return (HttpURLConnection) new URL(uri.toString()).openConnection();
    }
}