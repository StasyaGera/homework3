package com.hometasks.penguinni.homework3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class HW3Activity extends AppCompatActivity {
    private Intent service = null;
    private LocalBroadcastManager localBCManager = null;

    public static final String imageFile = "/image.jpg";
    public static final String recoveryFile = "/imageID.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hw3);

        if (service == null) {
            service = new Intent(getApplicationContext(), PicService.class);
        }

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

        TextView errorText = (TextView) findViewById(R.id.error);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        File image = new File(getFilesDir(), imageFile);
        if (image.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(image.toString()));
        } else {
            imageView.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        File image = new File(getFilesDir(), imageFile);
        if (image.exists()) {
            image.delete();
        }
    }
}
