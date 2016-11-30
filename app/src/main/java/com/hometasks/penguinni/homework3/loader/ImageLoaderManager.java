package com.hometasks.penguinni.homework3.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.hometasks.penguinni.homework3.HW3Activity;
import com.hometasks.penguinni.homework3.api.VKApi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by penguinni on 30.11.16.
 */

public class ImageLoaderManager {
    public ImageLoaderManager(Context context) {
        this.context = context;
    }

    private Context context;
    private int imageID = 0;
    private List<String> urls;

    public void recover(int id) {
        imageID = id;
    }

    public void nextImage() {
        Log.d(TAG, "Initiating next image loader");

        if (imageID % VKApi.COUNT == 0) {
            Log.d(TAG, "obtaining next urls pack");
            getNextImagesPack();
        }

        imageID++;

        File image = new File(context.getFilesDir(), HW3Activity.imageFilename);
        try {
            image.delete();
            image.createNewFile();
            new ImageLoader().execute((Pair<URL, File>) new Pair(new URL(urls.get(imageID % VKApi.COUNT)), image));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    private void getNextImagesPack() {
        AsyncTask<Integer, Integer, List<String>> nextPackGetter = new AsyncTask<Integer, Integer, List<String>>() {
            @Override
            protected List<String> doInBackground(Integer... ids) {
                Log.d(TAG, "Performing an API request");

                HttpURLConnection connection;
                try {
                    connection = VKApi.getPhotosRequest(ids[0] / VKApi.COUNT);
                    InputStream in = connection.getInputStream();
                    return ImagesPullParser.parseImages(in);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }

                return null;
            }
        };

        try {
            this.urls = nextPackGetter.execute(imageID).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private final String TAG = "ImageLoaderManager";
}
