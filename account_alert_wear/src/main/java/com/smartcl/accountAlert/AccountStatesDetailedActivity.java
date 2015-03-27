package com.smartcl.accountAlert;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity displayed in a notification when we detect a smartbeacon.
 * This activity displays the account states of the user and its family.
 */
public class AccountStatesDetailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_detailed);

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
        List<List<Account>> accounts = createAccountsFromJson(accountInformation);

        final GridViewPager pager = (GridViewPager) stub.findViewById(R.id.pager);
        pager.setAdapter(
                new AccountPagerAdapter(AccountStatesDetailedActivity.this, getFragmentManager(),
                                        accounts));
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
