package com.smartcl.androidwearsmartbeacon;

import com.google.android.gms.wearable.MessageEvent;
import com.smartcl.communicationlibrary.BaseListenerService;


/**
 * Created by bourdi_bay on 31/01/2015.
 */
public class ListenerService extends BaseListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast(messageEvent.getPath());
    }

}
