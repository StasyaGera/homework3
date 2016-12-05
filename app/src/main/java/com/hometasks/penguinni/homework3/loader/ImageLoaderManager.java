package com.hometasks.penguinni.homework3.loader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.util.Log;

import com.hometasks.penguinni.homework3.HW3Activity;
import com.hometasks.penguinni.homework3.Util;
import com.hometasks.penguinni.homework3.api.VKApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Created by penguinni on 29.11.16.
 */

public class ImageLoaderManager {
    public ImageLoaderManager(Context context) {
        recoveryFile = new File(context.getFilesDir(), HW3Activity.recoveryFile);
        recover();
        this.context = context;
    }

    private Context context;
    private int imageID = 0;
    private List<String> urls;
    private File recoveryFile;

    public void clean() {
        this.imageID = 0;
        this.urls = null;
        recoveryFile.delete();
    }

    private void recover() {
        try {
            if (recoveryFile.exists()) {
                Scanner scanner = new Scanner(recoveryFile);
                if (scanner.hasNextInt()) {
                    imageID = scanner.nextInt();
                    Log.d(TAG, "Read id " + imageID);
                }
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Recovery file not found");
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Log.d(TAG, "Saving id");

            recoveryFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(recoveryFile);
            printWriter.print(imageID);
            printWriter.close();
        } catch (IOException e) {
            Log.d(TAG, "Recovery file not found");
            e.printStackTrace();
        }
    }

    public void nextImage() {
        Log.d(TAG, "Initiating next image loader");

        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.loading_started"));

        if (urls == null || imageID % VKApi.COUNT == 0) {
            Log.d(TAG, "Obtaining next urls pack");
            getNextImagesPack();
        }

        imageID++;

        File image = new File(context.getFilesDir(), HW3Activity.imageFile);
        try {
            image.delete();
            if (urls != null) {
                image.createNewFile();
                new ImageLoader(context).execute((Pair<URL, File>) new Pair(new URL(urls.get(imageID % VKApi.COUNT)), image));
            }
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

            if (!Util.isConnectionAvailable(context, false)) {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.no_internet"));
                return null;
            }

            try {
                connection = VKApi.getPhotosRequest(ids[0] / VKApi.COUNT);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = connection.getInputStream();
                    List<String> result = ImagesPullParser.parseImages(in);
                    if (result != null) {
                        return result;
                    } else {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.error_occurred"));
                    }
                } else {
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("internal.error_occurred"));
                }
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
