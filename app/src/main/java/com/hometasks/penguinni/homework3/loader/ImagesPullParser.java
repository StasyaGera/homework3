package com.hometasks.penguinni.homework3.loader;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by penguinni on 29.11.16.
 */

public class ImagesPullParser {
    static List<String> parseImages(InputStream in) {
        try {
            return parseImages(new JsonReader(new InputStreamReader(in, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<String> parseImages(JsonReader reader) {
        ArrayList<String> images = null;
        try {
            reader.beginObject();
            switch (reader.nextName()) {
                case "response":
                    break;
                case "error":
                    reader.beginObject();
                    while (!reader.nextName().equals("error_msg")) {
                        reader.skipValue();
                    }
                    Log.e(TAG, reader.nextString());
                    return null;
                default:
                    break;
            }
            reader.beginArray();

            images = new ArrayList<>();
            while (reader.hasNext()) {
                reader.beginObject();

                String token;
                while (reader.hasNext()) {
                    token = reader.nextName();
                    if (token != null && token.equals("src_big")) {
                        images.add(reader.nextString());
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
            }

            reader.endArray();
            reader.endObject();

        } catch (IOException e) {
            Log.d(TAG, "Cannot parse json");
            e.printStackTrace();
        }

        return images;
    }

    private static final String TAG = "ImagesPullParser";
}
