package com.hometasks.penguinni.homework3.loader;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by penguinni on 29.11.16.
 */

public class ImagesPullParser {
    static List<String> parseImages(InputStream in) throws IOException {
        return parseImages(new JsonReader(new InputStreamReader(in, "UTF-8")));
    }

    private static List<String> parseImages(JsonReader reader) throws IOException {
        reader.beginObject();
        switch (reader.nextName()) {
            case "response":
                break;
            case "error":
                return null;
            default:
                break;
        }
        reader.beginArray();

        ArrayList<String> images = new ArrayList<>();
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

        return images;
    }
}
