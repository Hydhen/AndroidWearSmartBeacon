package com.smartcl.androidwearsmartbeacon;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;


/**
 * Listener which gets messages from the handheld device.
 */
public class ListenerService extends BaseListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        // TODO: check format
        showToast(messageEvent.getPath());

        //TODO: if question, send if as parameter to the trigger notification (reuqire refactoring).
        NotificationsTrigger.TriggerNotification(this);


    }

}
