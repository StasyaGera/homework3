package com.hometasks.penguinni.homework3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class HW3Activity extends AppCompatActivity {
    private Intent service = null;
    public static final String imageFilename = "image.jpg";
    public static final String recoveryFilename = "imageID.txt";

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

        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(BitmapFactory.decodeFile(new File(getFilesDir(), imageFilename).toString()));
    }
}
