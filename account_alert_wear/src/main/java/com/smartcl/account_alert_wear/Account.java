package com.smartcl.account_alert_wear;

import java.io.Serializable;

/**
 * Created by bourdi_b on 25/02/2015.
 */
public class Account implements Serializable {
    private Long _money;
    private eState _state;
    private String _date;

    public Account(Long money, String state, String date) {
        _money = money;
        _date = date;
        switch (state) {
            case "red":
                _state = eState.RED;
                break;
            case "orange":
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

    public int getStateImageResource() {
        switch (getState()) {
            case RED:
                return R.drawable.red;
            case YELLOW:
                return R.drawable.yellow;
            case GREEN:
                return R.drawable.green;
        }
        return 0;
    }

    enum eState {
        RED,
        YELLOW,
        GREEN
    }
}
