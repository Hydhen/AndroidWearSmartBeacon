package com.smartcl.account_alert_wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;

import com.smartcl.communicationlibrary.MessageSender;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity displayed in a notification when we detect a smartbeacon.
 * This activity displays a question and sends the answer to the handheld device.
 */
public class AccountHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_history);

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
        List<Account> accounts = new ArrayList();
        JSONObject currentAccountJson = (JSONObject) accountInformation.get("current_account");
        Account currentAccount = createAccountFromJson(currentAccountJson);
        accounts.add(currentAccount);

        JSONArray historyJsonArray = (JSONArray) accountInformation.get("previsions");
        for (Object accountHistory : historyJsonArray) {
            Account account = createAccountFromJson((JSONObject) accountHistory);
            accounts.add(account);
        }

        final GridViewPager pager = (GridViewPager) stub.findViewById(R.id.pager);
        pager.setAdapter(
                new AccountPagerAdapter(AccountHistoryActivity.this, getFragmentManager(), accounts));
    }

    private Account createAccountFromJson(JSONObject accountData) {
        final Long money = (Long) accountData.get("money");
        final String state = (String) accountData.get("state");
        final String date = (String) accountData.get("date");
        return new Account(money, state, date);
    }

}
