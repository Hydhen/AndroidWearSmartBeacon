package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomFragment extends Fragment {

    /**
     * Avoid the issue with the default constructor needed.
     *
     * @param account The account to display.
     * @return The instance of CustomFragment.
     */
    public static CustomFragment newInstance(Account account) {
        CustomFragment fragment = new CustomFragment();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putSerializable("account", account);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Account account = (Account) getArguments().getSerializable("account");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_current_account, container, false);
        final WatchViewStub stub = (WatchViewStub) view.findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets(stub, account);
            }
        });
        return view;
    }

    private void setupWidgets(WatchViewStub stub, Account account) {
        TextView moneyLabel = (TextView) stub.findViewById(R.id.money_label);
        moneyLabel.setText(account.getState().toString());
        ImageView img = (ImageView) stub.findViewById(R.id.imageView);
        switch (account.getState()) {
            case RED:
                img.setImageResource(R.drawable.red);
                break;
            case YELLOW:
                img.setImageResource(R.drawable.yellow);
                break;
            case GREEN:
                img.setImageResource(R.drawable.green);
                break;
        }
    }

}
