package com.smartcl.androidwearsmartbeacon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.smartcl.communicationlibrary.MessageSender;

import java.util.List;

import eu.smartbeacon.sdk.core.SBBeacon;
import eu.smartbeacon.sdk.core.SBLocationManager;
import eu.smartbeacon.sdk.core.SBLocationManagerListener;
import eu.smartbeacon.sdk.utils.SBLogger;

public class DiscoverBeaconsService extends Service implements SBLocationManagerListener {

    private boolean _isInitialized = false;
    private MessageSender _messageSender = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeBeacons();
        Toast.makeText(this, "On create service", Toast.LENGTH_SHORT).show();
    }

    private void initializeBeacons() {
        if (_isInitialized == false) {
            _isInitialized = true;

            _messageSender = new MessageSender(getApplicationContext());
            // disable logging message
            SBLogger.setSilentMode(true);

            SBLocationManager sbManager = SBLocationManager.getInstance(this);
            sbManager.addEntireSBRegion();
            sbManager.addBeaconLocationListener(this);
            sbManager.startMonitoringAllBeaconRegions();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializeBeacons();

        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        SBLocationManager.getInstance(this)
                .stopMonitoringAllBeaconRegions();
        super.onDestroy();
    }

    @Override
    public void onEnteredBeacons(List<SBBeacon> sbBeacons) {
        _messageSender.sendMessage("Beacon has entered");
       // Toast.makeText(this, "Beacon enter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onExitedBeacons(List<SBBeacon> sbBeacons) {
        _messageSender.sendMessage("Beacon has exited");
        //Toast.makeText(this, "Beacon exit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDiscoveredBeacons(List<SBBeacon> sbBeacons) {
        _messageSender.sendMessage("Beacon has been discovered");
      //  Toast.makeText(this, "Beacon disco", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdatedProximity(SBBeacon sbBeacon, SBBeacon.Proximity proximity,
                                   SBBeacon.Proximity proximity2) {
        _messageSender.sendMessage("Beacon proximity updated");
     //   Toast.makeText(this, "Proxi", Toast.LENGTH_SHORT).show();
    }
}
