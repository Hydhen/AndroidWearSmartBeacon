package com.smartcl.account_alert_wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CurrentAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_account);
        final Bundle bundle = getIntent().getExtras();
        final String strJson = bundle.getString("account_data");
        final JSONObject accountData = (JSONObject) JSONValue.parse(strJson);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets(stub, accountData);
            }
        });
    }

    private void setupWidgets(WatchViewStub stub, JSONObject accountInformation) {
        JSONObject currentAccountJson = (JSONObject) accountInformation.get("current_account");
        Account currentAccount = createAccountFromJson(currentAccountJson);

        TextView moneyLabel = (TextView) stub.findViewById(R.id.money_label);
        moneyLabel.setText(String.valueOf(currentAccount.getMoney()));
        TextView dateLabel = (TextView) stub.findViewById(R.id.date);
        dateLabel.setText(currentAccount.getDate());
        ImageView img = (ImageView) stub.findViewById(R.id.img);
        img.setImageResource(currentAccount.getStateImageResource());
    }

    private Account createAccountFromJson(JSONObject accountData) {
        final Long money = (Long) accountData.get("money");
        final String state = (String) accountData.get("state");
        final String date = (String) accountData.get("date");
        return new Account(money, state, date);
    }
}
