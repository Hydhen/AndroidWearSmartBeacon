package com.smartcl.androidwearsmartbeacon;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private void setupWidgets(WatchViewStub stub) {
        final Bundle bundle = getIntent().getExtras();
        final String question = bundle.getString("question");
        TextView textView = (TextView) stub.findViewById(R.id.question);
        textView.setText(question);

        _messageSender = new MessageSender(NotificationActivity.this);

        Button btYes = (Button) stub.findViewById(R.id.button_yes);
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer("Yes");
            }
        });
        Button btNo = (Button) stub.findViewById(R.id.button_no);
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer("No");
            }
        });
    }

    private void sendAnswer(String answer) {
        final Bundle bundle = getIntent().getExtras();
        final String question = bundle.getString("question");

        JSONObject json = new JSONObject();
        json.put("answer", answer);
        json.put("question", question);
        _messageSender.sendMessage(QUESTION_ANSWER_PATH, json);
    }

}
