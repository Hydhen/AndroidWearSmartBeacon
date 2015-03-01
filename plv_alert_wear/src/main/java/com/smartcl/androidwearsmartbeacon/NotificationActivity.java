package com.smartcl.androidwearsmartbeacon;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smartcl.communicationlibrary.LCLPreferences;
import com.smartcl.communicationlibrary.MessageSender;

import org.json.simple.JSONObject;

/**
 * Activity displayed in a notification when we detect a smartbeacon.
 * This activity displays a question and sends the answer to the handheld device.
 */
public class NotificationActivity extends Activity {

    public static final String QUESTION_ANSWER_PATH = "/question/answer/";
    private MessageSender _messageSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets(stub);
            }
        });
    }

    private void disableWidgets(final WatchViewStub stub) {
        ImageButton btYes = (ImageButton) stub.findViewById(R.id.button_yes);
        ImageButton btNo = (ImageButton) stub.findViewById(R.id.button_no);
        TextView setText = (TextView) stub.findViewById(R.id.thanks);

        btYes.setEnabled(false);
        btNo.setEnabled(false);

        btNo.setVisibility(View.INVISIBLE);
        btYes.setVisibility(View.INVISIBLE);

        setText.setVisibility(View.VISIBLE);
    }

    private void setupWidgets(final WatchViewStub stub) {
        final Bundle bundle = getIntent().getExtras();
        final String question = bundle.getString("question");
        TextView questiontext = (TextView) stub.findViewById(R.id.question);

        questiontext.setText(question);
        _messageSender = new MessageSender(NotificationActivity.this);

        final ImageButton btYes = (ImageButton) stub.findViewById(R.id.button_yes);
        final ImageButton btNo = (ImageButton) stub.findViewById(R.id.button_no);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer("Yes", stub);
                disableWidgets(stub);
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer("No", stub);
                disableWidgets(stub);
            }
        });
    }

    private void sendAnswer(String answer, WatchViewStub stub) {
        final Bundle bundle = getIntent().getExtras();
        final String question = bundle.getString("question");

        JSONObject json = new JSONObject();
        json.put("answer", answer);
        json.put("question", question);
        json.put("name", LCLPreferences.GetNameUser(this));
        json.put("server_url", LCLPreferences.GetServerUrl(this));
        _messageSender.sendMessage(QUESTION_ANSWER_PATH, json);


    }

}
