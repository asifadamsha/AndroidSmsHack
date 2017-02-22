package com.projet.esgi.smshack.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by raphael on 30/01/2016.
 * This class receive the incoming mms messages.
 * Since we are not interested in MMS this received does nothing.
 */
public class mmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(getClass().getName(), "MmsReceived has been called");
    }
}
