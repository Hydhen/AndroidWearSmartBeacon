package com.smartcl.androidwearsmartbeacon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.smartcl.communicationlibrary.MessageSender;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.smartbeacon.sdk.core.SBBeacon;
import eu.smartbeacon.sdk.core.SBLocationManager;
import eu.smartbeacon.sdk.core.SBLocationManagerListener;
import eu.smartbeacon.sdk.utils.SBLogger;

class TimerThreads {
    public static final String BEACON_ENTERED_PATH = "/beacon/entered/";
    private HashMap<JSONObject, TimerThread> _timers = new HashMap<>();

    /**
     * Queue a beacon in the timer list. Basically we wait for few seconds and then run
     * an action if nothing else said the opposite (exit the detection range for instance).
     *
     * @param messageSender The MessageSender instance to send a message to the handheld.
     * @param beacon        The beacon entered.
     */
    public void queueBeacon(MessageSender messageSender, SBBeacon beacon) {
        JSONObject json = buildJsonObjectFromBeacon(beacon);

        // If the thread associated to this beacon is already running,
        // we interrupt it and re-start it right away.
        for (Iterator<Map.Entry<JSONObject, TimerThread>> it = _timers.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<JSONObject, TimerThread> entry = it.next();
            final JSONObject jsonObj = entry.getKey();
            if (jsonObj.equals(json)) {
                entry.getValue().interrupt();
                entry.setValue(new TimerThread(messageSender, json));
                entry.getValue().start();
                return;
            }
        }

        // Else we add the beacon in the timer list.
        TimerThread thread = new TimerThread(messageSender, json);
        _timers.put(json, thread);
        thread.start();
    }

    /**
     * Unqueue the beacon in the timer list. This should be called when
     * we exit the beacons' detection range.
     *
     * @param beacon The beacon that got undetected.
     */
    public void unqueueBeacon(SBBeacon beacon) {
        JSONObject json = buildJsonObjectFromBeacon(beacon);
        for (Iterator<Map.Entry<JSONObject, TimerThread>> it = _timers.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<JSONObject, TimerThread> entry = it.next();
            final JSONObject jsonObj = entry.getKey();
            if (jsonObj.equals(json)) {
                entry.getValue().interrupt();
                it.remove();
                return;
            }
        }
    }

    private JSONObject buildJsonObjectFromBeacon(SBBeacon beacon) {
        JSONObject json = new JSONObject();
        json.put("major", beacon.getMajor());
        json.put("minor", beacon.getMinor());
        return json;
    }

    class TimerThread extends Thread {

        private JSONObject _json;
        private MessageSender _messageSender;

        public TimerThread(MessageSender messageSender, JSONObject json) {
            _messageSender = messageSender;
            _json = json;
        }

        @Override
        public void run() {
            try {
                synchronized (this) {
                    // Let's say we need 5 seconds to read the LCL's PLV.
                    wait(5000);
                }
            } catch (InterruptedException ex) {
            }
            _messageSender.sendMessage(BEACON_ENTERED_PATH, _json);
        }
    }
}

/**
 * Service which scans the smartbeacons around.
 * If a smartbecon is found, it sends a message to the handheld device
 * via the MessageSender class.
 */
public class DiscoverBeaconsService extends Service implements SBLocationManagerListener {

    private final TimerThreads _timerThreads = new TimerThreads();
    private boolean _isInitialized = false;
    private MessageSender _messageSender = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeBeacons();
    }

    private void initializeBeacons() {
        if (!_isInitialized) {
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
        for (SBBeacon beacon : sbBeacons) {
            _timerThreads.queueBeacon(_messageSender, beacon);
            Toast.makeText(this, "Beacon enter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExitedBeacons(List<SBBeacon> sbBeacons) {
        for (SBBeacon beacon : sbBeacons) {
            _timerThreads.unqueueBeacon(beacon);
            Toast.makeText(this, "Beacon exit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDiscoveredBeacons(List<SBBeacon> sbBeacons) {
    }

    @Override
    public void onUpdatedProximity(SBBeacon sbBeacon, SBBeacon.Proximity proximity,
                                   SBBeacon.Proximity proximity2) {
    }
}
