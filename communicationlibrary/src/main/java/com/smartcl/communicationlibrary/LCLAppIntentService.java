package com.smartcl.communicationlibrary;

import android.app.IntentService;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import org.json.simple.JSONObject;

public class LCLAppIntentService extends IntentService {

    private static final String WEBSITE_OPEN_PATH = "/app/open/lcl/";
    private MessageSender _messageSender = null;

    public LCLAppIntentService() {
        super("LCLAppIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        _messageSender = new MessageSender(getApplicationContext());

        Intent openOnPhoneIntent = new Intent(this, ConfirmationActivity.class);
        openOnPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openOnPhoneIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                   ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
        startActivity(openOnPhoneIntent);

        _messageSender = new MessageSender(getApplicationContext());
        JSONObject json = new JSONObject();
        json.put("details", "fr.lcl.android.customerarea");
        _messageSender.sendMessage(WEBSITE_OPEN_PATH, json);
    }

}
