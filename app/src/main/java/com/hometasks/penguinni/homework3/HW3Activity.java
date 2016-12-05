package com.hometasks.penguinni.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class HW3Activity extends AppCompatActivity {
    public static final String imageFile = "/image.jpg";
    public static final String recoveryFile = "/imageID.txt";

    private Intent service = null;
    private ImageView imageView;
    private TextView noInternet, error;
    private ProgressBar loading;
    BroadcastReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw3);

        loading = (ProgressBar) findViewById(R.id.loading);
        error = (TextView) findViewById(R.id.error_view);
        noInternet = (TextView) findViewById(R.id.no_internet);
        imageView = (ImageView) findViewById(R.id.image);

        if (service == null) {
            service = new Intent(getApplicationContext(), PicService.class);
        }

        IntentFilter statusChanged = new IntentFilter("internal.image_downloaded");
        statusChanged.addAction("internal.no_internet");
        statusChanged.addAction("internal.error_occurred");
        statusChanged.addAction("internal.loading_started");

        localReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ("internal.image_downloaded"):
                        Log.d("LocalReceiver", "Image loaded");
                        displayImage();
                        break;
                    case ("internal.no_internet"):
                        Log.d("LocalReceiver", "No connection");
                        setVisible(noInternet);
                        break;
                    case ("internal.error_occurred"):
                        Log.d("LocalReceiver", "Error");
                        setVisible(error);
                        break;
                    case ("internal.loading_started"):
                        Log.d("LocalReceiver", "Image in process");
                        setVisible(loading);
                        break;
                    default:
                        break;
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, statusChanged);


        Button start = (Button) findViewById(R.id.start_btn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(service);
            }
        });

        Button stop = (Button) findViewById(R.id.stop_btn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(service);
            }
        });

        Button restart = (Button) findViewById(R.id.restart_btn);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("service.restart"));
            }
        });

        displayImage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
    }

    void displayImage() {
        File image = new File(getFilesDir(), imageFile);
        if (image.exists()) {
            setVisible(imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(image.toString()));
        }
    }

    void setVisible(View v) {
        error.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);

        v.setVisibility(View.VISIBLE);
    }

    private final String TAG = "Main";
}
