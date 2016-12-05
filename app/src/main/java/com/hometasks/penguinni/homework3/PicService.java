package com.hometasks.penguinni.homework3;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hometasks.penguinni.homework3.loader.ImageLoaderManager;

public class PicService extends Service {
    public PicService() {}

    private ImageLoaderManager loaderManager;
    private Receiver receiver;
    private BroadcastReceiver localReceiver;

    public void reset() {
        Log.d(TAG, "reset");
        loaderManager.clean();
    }

    void loadNext() {
        Log.d(TAG, "load next image");
        loaderManager.nextImage();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on create");

        receiver = new Receiver(this);
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));

        localReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                reset();
            }
        };
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(localReceiver, new IntentFilter("service.restart"));

        loaderManager = new ImageLoaderManager(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "on start command");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on destroy");

        loaderManager.save();
        unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(localReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private final String TAG = "PicService";
}
