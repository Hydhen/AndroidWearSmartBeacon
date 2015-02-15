package com.smartcl.androidwearsmartbeacon;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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
