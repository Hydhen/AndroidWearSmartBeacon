package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAccountFragment extends Fragment {

    /**
     * Avoid the issue with the default constructor needed.
     *
     * @param account The account to display.
     * @return The instance of CustomAccountFragment.
     */
    public static CustomAccountFragment newInstance(Account account, boolean hasBottomNeighboor, boolean hasNextNeighboor) {
        CustomAccountFragment fragment = new CustomAccountFragment();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putSerializable("account", account);
        bundle.putBoolean("hasBottomNeighboor", hasBottomNeighboor);
        bundle.putBoolean("hasNextNeighboor", hasNextNeighboor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Account account = (Account) getArguments().getSerializable("account");
        final boolean hasBottomNeighboor = getArguments().getBoolean("hasBottomNeighboor");
        final boolean hasNextNeighboor = getArguments().getBoolean("hasNextNeighboor");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_account, container, false);
        final WatchViewStub stub = (WatchViewStub) view
                .findViewById(R.id.watch_view_stub_custom_fragment);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets(stub, account, hasBottomNeighboor, hasNextNeighboor);
            }
        });
        return view;
    }

    private void setupWidgets(WatchViewStub stub, Account account, boolean hasBottomNeighboor, boolean hasNextNeighboor) {
        TextView name = (TextView) stub.findViewById(R.id.nameUser);
        name.setText(account.getName());
        TextView moneyLabel = (TextView) stub.findViewById(R.id.money_label);
        moneyLabel.setText(String.valueOf(account.getMoney()));
        TextView dateLabel = (TextView) stub.findViewById(R.id.date);
        dateLabel.setText(account.getDate());
        ImageView img = (ImageView) stub.findViewById(R.id.img);
        img.setImageResource(account.getDetailedStateImageResource());

        View arrowHori = stub.findViewById(R.id.arrow_hori);
        if (hasNextNeighboor == false) {
            arrowHori.setVisibility(View.INVISIBLE);
        }
        View arrowVert = stub.findViewById(R.id.arrow_vert);
        if (hasBottomNeighboor == false) {
            arrowVert.setVisibility(View.INVISIBLE);
        }
    }

}
