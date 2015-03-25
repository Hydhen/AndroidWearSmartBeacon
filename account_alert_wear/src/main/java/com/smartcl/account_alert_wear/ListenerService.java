package com.smartcl.account_alert_wear;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;
import com.smartcl.communicationlibrary.LCLPreferences;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    public static final String ACCOUNTS_PATH = "/accounts/";
    public static final String SEND_PREFERENCES_PATH = "/preferences/data/";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        final String path = messageEvent.getPath();
        switch (path) {
            case ACCOUNTS_PATH:
                accountsGotten(messageEvent);
                break;
            case SEND_PREFERENCES_PATH:
                preferencesGotten(messageEvent);
                break;
            default:
                //showToast("Unknown message:" + path);
                break;
        }
    }

    private void accountsGotten(MessageEvent messageEvent) {
       // JSONObject json = extractJsonFromMessage(messageEvent);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        final String currentDate = dateFormat.format(cal.getTime());

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 5);
        final String date5Days = dateFormat.format(cal.getTime());

        cal.setTime(new Date());
        cal.add(Calendar.DATE, 10);
        final String date10Days = dateFormat.format(cal.getTime());

        // TODO: get this message from json object gotten.
        final String mockMessage =
                "{ \"accounts\" : [" +
                        "{ \"name\" : \"Mon compte courant\", \"account\" : [" +
                        "{ \"money\" : 436.0, \"state\" : \"green\", \"date\" : \"" + currentDate + "\" }," +
                        "{ \"money\" : 36.0, \"state\" : \"yellow\", \"date\" : \"" + date5Days + "\" }," +
                        "{ \"money\" : 2036.0, \"state\" : \"green\", \"date\" : \"" + date10Days + "\" }" +
                        "]}," +
                        "{ \"name\" : \"Mon fils\", \"account\" : [" +
                        "{ \"money\" : 150.0, \"state\" : \"green\", \"date\" : \"" + currentDate + "\" }," +
                        "{ \"money\" : 50.0, \"state\" : \"yellow\", \"date\" : \"" + date5Days + "\" }," +
                        "{ \"money\" : -20.0, \"state\" : \"red\", \"date\" : \"" + date10Days + "\" }" +
                        "]}" +
                        "]}";
        JSONObject jsonMocked = (JSONObject) JSONValue.parse(mockMessage);

        NotificationsTrigger.TriggerNotification(this, jsonMocked);
    }

    private void preferencesGotten(MessageEvent messageEvent) {
        byte[] data = messageEvent.getData();
        Map<String, ?> map = LCLPreferences.GetDeserialized(data);
        LCLPreferences.WritePreferences(this, map);

        // TODO: this is a biiiig cheat.
        // The problem is when I get the preferences, the message for accounts is lost !
        // Since the server connection will not be done, we can cheat like this...
        // FIXME: Find a fix for message loses.
        accountsGotten(messageEvent);
    }
}
