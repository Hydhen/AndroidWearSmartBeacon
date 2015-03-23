package com.smartcl.account_alert_wear;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class AccountStatesOverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_overview);
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

    private void setupWidgets(WatchViewStub stub, JSONObject accountsInformation) {
        List<List<Account>> accounts = createAccountsFromJson(accountsInformation);

        RelativeLayout mainLayout = (RelativeLayout) stub
                .findViewById(R.id.main_layout_accounts_detailed);
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        int id_below = R.id.space_top_account;

        for (List<Account> accountStates : accounts) {
            View lineAccount = inflater.inflate(R.layout.account_states, null);
            TextView nameView = (TextView) lineAccount.findViewById(R.id.name);

            int id = View.generateViewId();
            lineAccount.setId(id);

            if (accountStates.size() >= 1) {
                Account firstState = accountStates.get(0);
                nameView.setText(firstState.getName());

                ImageView currentState = (ImageView) lineAccount.findViewById(R.id.current_state);
                currentState.setImageResource(accountStates.get(0).getOverviewStateImageResource());

                ImageView midState = (ImageView) lineAccount.findViewById(R.id.mid_state);
                midState.setImageResource(accountStates.get(1).getOverviewStateImageResource());

                ImageView lastState = (ImageView) lineAccount.findViewById(R.id.last_state);
                lastState.setImageResource(accountStates.get(2).getOverviewStateImageResource());
            }

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, id_below);
            p.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lineAccount.setLayoutParams(p);

            mainLayout.addView(lineAccount);
            id_below = lineAccount.getId();
        }
    }

    private List<List<Account>> createAccountsFromJson(JSONObject accountsData) {
        List<List<Account>> accounts = new ArrayList<List<Account>>();
        final JSONArray jsonAccounts = (JSONArray) accountsData.get("accounts");
        for (final Object accountEntity : jsonAccounts) {
            final JSONObject jsonAccount = (JSONObject) accountEntity;
            final String name = (String) jsonAccount.get("name");
            final JSONArray account = (JSONArray) jsonAccount.get("account");
            List<Account> accountsStates = new ArrayList<Account>();
            for (Object moneyEntity : account) {
                final JSONObject jsonMoney = (JSONObject) moneyEntity;
                final String date = (String) jsonMoney.get("date");
                final String state = (String) jsonMoney.get("state");
                final Double money = (Double) jsonMoney.get("money");
                accountsStates.add(new Account(money, state, date, name));
            }
            accounts.add(accountsStates);
        }
        return accounts;
    }
}
