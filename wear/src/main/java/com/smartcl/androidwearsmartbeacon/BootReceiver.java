package com.smartcl.androidwearsmartbeacon;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Called when the device boots.
 * It start automatically the service which scans the smartbeacons around.
 */
public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent().setComponent(new ComponentName(context.getPackageName(),
                                                                         DiscoverBeaconsService.class
                                                                                 .getName())));
    }
}
