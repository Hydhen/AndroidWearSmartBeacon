package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

/**
 * Implementation of the 2D picker pattern.
 */
public class AccountPagerAdapter extends FragmentGridPagerAdapter {

    private final List<List<Account>> _accounts;
    private Context _context;

    public AccountPagerAdapter(Context context, FragmentManager fm, List<List<Account>> accounts) {
        super(fm);
        _context = context;
        _accounts = accounts;
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int column) {
        Account accountToDisplay = _accounts.get(row).get(column);
        CustomAccountFragment card = CustomAccountFragment.newInstance(accountToDisplay);
        return card;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        Account accountToDisplay = _accounts.get(row).get(column);
        switch (accountToDisplay.getState()) {
            case RED:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                                            new int[]{0xFFFF0000, 0xFFFFFFFF});
            case YELLOW:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                                            new int[]{0xFFFFFF00, 0xFFFFFFFF});
            case GREEN:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                                            new int[]{0xFF00FF00, 0xFFFFFFFF});
        }
        return new ColorDrawable();
    }

    @Override
    public int getRowCount() {
        return _accounts.size();
    }

    @Override
    public int getColumnCount(int rowNum) {
        if (getRowCount() <= 0) {
            return 0;
        }
        return _accounts.get(0).size();
    }
}
