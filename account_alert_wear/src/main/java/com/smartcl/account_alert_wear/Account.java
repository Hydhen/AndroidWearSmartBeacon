package com.smartcl.account_alert_wear;

import java.io.Serializable;

/**
 * Represent the bank account of a user.
 */
public class Account implements Serializable {
    private Long _money;
    private eState _state;
    private String _date;
    private String _name;

    public Account(Long money, String state, String date, String name) {
        _money = money;
        _date = date;
        _name = name;
        switch (state) {
            case "red":
                _state = eState.RED;
                break;
            case "yellow":
                _state = eState.YELLOW;
                break;
            case "green":
                _state = eState.GREEN;
                break;
            default:
                _state = eState.RED;
                break;
        }
    }

    public Long getMoney() {
        return _money;
    }

    public eState getState() {
        return _state;
    }

    public String getDate() {
        return _date;
    }

    public String getName() {
        return _name;
    }

    public int getDetailedStateImageResource() {
        switch (getState()) {
            case RED:
                return R.drawable.red_account;
            case YELLOW:
                return R.drawable.yellow_account;
            case GREEN:
                return R.drawable.green_account;
        }
        return 0;
    }

    public int getOverviewStateImageResource() {
        switch (getState()) {
            case RED:
                return R.drawable.red_circle;
            case YELLOW:
                return R.drawable.yellow_circle;
            case GREEN:
                return R.drawable.green_circle;
        }
        return 0;
    }

    enum eState {
        RED,
        YELLOW,
        GREEN
    }
}
