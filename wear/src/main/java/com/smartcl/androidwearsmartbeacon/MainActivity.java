package com.smartcl.androidwearsmartbeacon;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartcl.communicationlibrary.MessageSender;


/**
 * Main activity of the wearable application.
 * It starts the smartbeacons scanner service
 * and provides a button to stop this service.
 * <p/>
 * This activity is necessary because the RECEIVE_BOOT_COMPLETED property
 * does not work if we did not run an application's activity at least once.
 */
public class MainActivity extends Activity {

    private static final String MESSAGE = "This is from the Wear";
    private MessageSender _messageSender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _messageSender = new MessageSender(getApplicationContext());
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets();
            }
        });
    }

    /**
     * Set up the button for handling click events.
     */
    private void setupWidgets() {
        Button bt_stop_start_service = (Button) findViewById(R.id.start_stop_service);
        if (isMyServiceRunning(DiscoverBeaconsService.class) == false) {
            startBeaconService();
        }
        bt_stop_start_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, DiscoverBeaconsService.class));
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToPhone();
            }
        });
    }

    private void sendMessageToPhone() {
        _messageSender.sendMessage(MESSAGE);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Toast.makeText(this, "is running", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void startBeaconService() {
        startService(new Intent(this, DiscoverBeaconsService.class));
    }

}
