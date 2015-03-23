package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CustomAccountFragment extends Fragment {

    /**
     * Avoid the issue with the default constructor needed.
     *
     * @param account The account to display.
     * @return The instance of CustomAccountFragment.
     */
    public static CustomAccountFragment newInstance(Account account, int row, int nbRows, int column, int nbColumns) {
        CustomAccountFragment fragment = new CustomAccountFragment();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putSerializable("account", account);
        bundle.putBoolean("hasBottomNeighboor", row < nbRows - 1);
        bundle.putBoolean("hasAboveNeighboor", row > 0);
        bundle.putBoolean("hasLeftNeighboor", column > 0);
        bundle.putBoolean("hasRightNeighboor", column < nbColumns - 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Account account = (Account) getArguments().getSerializable("account");
        final boolean hasBottomNeighboor = getArguments().getBoolean("hasBottomNeighboor");
        final boolean hasAboveNeighboor = getArguments().getBoolean("hasAboveNeighboor");
        final boolean hasLeftNeighboor = getArguments().getBoolean("hasLeftNeighboor");
        final boolean hasRightNeighboor = getArguments().getBoolean("hasRightNeighboor");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_account, container, false);
        final WatchViewStub stub = (WatchViewStub) view
                .findViewById(R.id.watch_view_stub_custom_fragment);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets(stub, account, hasBottomNeighboor, hasAboveNeighboor, hasLeftNeighboor, hasRightNeighboor);
            }
        });
        return view;
    }

    private void setupWidgets(WatchViewStub stub, Account account, boolean hasBottomNeighboor, boolean hasAboveNeighboor,
                              boolean hasLeftNeighboor, boolean hasRightNeighboor) {
        TextView name = (TextView) stub.findViewById(R.id.name_user);
        name.setText(account.getName());
        TextView moneyLabel = (TextView) stub.findViewById(R.id.money_label);

        final Double money = account.getMoney();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat format = new DecimalFormat("#,###.00", symbols);

        moneyLabel.setText(format.format(money) + " €");

        TextView dateLabel = (TextView) stub.findViewById(R.id.date);
        dateLabel.setText(account.getDate());
        ImageView img = (ImageView) stub.findViewById(R.id.state_account);
        img.setImageResource(account.getDetailedStateImageResource());

        View leftArrow = stub.findViewById(R.id.arrow_left);
        View rightArrow = stub.findViewById(R.id.arrow_right);

        boolean left = false;
        boolean right = false;

        if (hasLeftNeighboor == true) {
            leftArrow.setRotation(0);
            left = true;
        }
        if (hasRightNeighboor == true) {
            rightArrow.setRotation(180);
            right = true;
        }

        if (left == false) {
            if (hasAboveNeighboor == true) {
                leftArrow.setRotation(90);
            } else if (hasBottomNeighboor == true) {
                leftArrow.setRotation(-90);
            }
        }
        else if (right == false) {
            if (hasAboveNeighboor == true) {
                rightArrow.setRotation(90);
            } else if (hasBottomNeighboor == true) {
                rightArrow.setRotation(-90);
            }
        }
    }

}
