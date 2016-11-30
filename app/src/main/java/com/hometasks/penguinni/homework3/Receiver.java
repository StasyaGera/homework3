package com.hometasks.penguinni.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by penguinni on 28.11.16.
 */

public class Receiver extends BroadcastReceiver {
    Receiver(PicService parent) {
        this.parent = parent;
    }

    PicService parent;

    @Override
    public void onReceive(Context context, Intent intent) {
        parent.loadNext();
    }
}
