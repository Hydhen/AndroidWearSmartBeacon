package com.smartcl.androidwearsmartbeacon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.smartcl.communicationlibrary.LCLPreferences;
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
    public void queueBeacon(Context context, MessageSender messageSender, SBBeacon beacon) {
        final boolean isPassed = LCLPreferences.GetStatusUser(context);
        // We send the notification only the first time we detect the beacon.
        if (isPassed) {
            // TODO: uncomment this once in production
//            return;
        }

        JSONObject json = buildJsonObjectFromBeacon(context, beacon);

        // If the thread associated to this beacon is already running,
        // we interrupt it and re-start it right away.
        for (Iterator<Map.Entry<JSONObject, TimerThread>> it = _timers.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<JSONObject, TimerThread> entry = it.next();
            final JSONObject jsonObj = entry.getKey();
            if (jsonObj.equals(json)) {
                entry.getValue().interrupt();
                entry.setValue(new TimerThread(context, messageSender, json));
                entry.getValue().start();
                return;
            }
        }

        // Else we add the beacon in the timer list.
        TimerThread thread = new TimerThread(context, messageSender, json);
        _timers.put(json, thread);
        thread.start();
    }

    /**
     * Unqueue the beacon in the timer list. This should be called when
     * we exit the beacons' detection range.
     *
     * @param beacon The beacon that got undetected.
     */
    public void unqueueBeacon(Context context, SBBeacon beacon) {
        JSONObject json = buildJsonObjectFromBeacon(context, beacon);
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

    private JSONObject buildJsonObjectFromBeacon(Context context, SBBeacon beacon) {
        JSONObject json = new JSONObject();
        json.put("major", beacon.getMajor());
        json.put("minor", beacon.getMinor());
        json.put("username", LCLPreferences.GetNameUser(context));
        return json;
    }

    class TimerThread extends Thread {

        private JSONObject _json;
        private MessageSender _messageSender;
        private Context _context;

        public TimerThread(Context context, MessageSender messageSender, JSONObject json) {
            _messageSender = messageSender;
            _json = json;
            _context = context;
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
            // We set the passed boolean to true, so that we can know if the user has already passed next to the beacon.
            LCLPreferences.SetNameUser(_context, LCLPreferences.GetNameUser(_context), true);
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

            final boolean isPassed = LCLPreferences.GetStatusUser(this);
            // We send the notification only the first time we detect the beacon.
            if (isPassed) {
                Toast.makeText(this, "We already passed next to this PLV advertising",
                               Toast.LENGTH_SHORT).show();
                // TODO: uncomment this once in production
                //return;
            }

            _messageSender = new MessageSender(getApplicationContext());
            // disable logging message
            SBLogger.setSilentMode(true);

            SBLocationManager sbManager = SBLocationManager.getInstance(this);
            sbManager.addEntireSBRegion();
            sbManager.addBeaconLocationListener(this);
            sbManager.startMonitoringAllBeaconRegions();

            LCLPreferences.SetNameUser(this, "Olivier");
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
            _timerThreads.queueBeacon(this, _messageSender, beacon);
            Toast.makeText(this, "Beacon enter", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onExitedBeacons(List<SBBeacon> sbBeacons) {
        for (SBBeacon beacon : sbBeacons) {
            _timerThreads.unqueueBeacon(this, beacon);
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
