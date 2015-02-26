package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bourdi_b on 25/02/2015.
 */
public class AccountPagerAdapter extends FragmentGridPagerAdapter {

    private final List<AccountPage> _pages;
    private Context _context;

    public AccountPagerAdapter(Context context, FragmentManager fm, List<Account> accounts) {
        super(fm);
        _context = context;
        _pages = new ArrayList(accounts.size());
        for (Account account : accounts) {
            _pages.add(new AccountPage(account));
        }
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        AccountPage page = _pages.get(row);
        CustomAccountFragment card = CustomAccountFragment.newInstance(page.getAccount());
        return card;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        AccountPage page = _pages.get(row);
        switch (page.getAccount().getState()) {
            case RED:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {0xFFFF0000, 0xFFFFFFFF});
            case YELLOW:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {0xFFFFFF00, 0xFFFFFFFF});
            case GREEN:
                return new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {0xFF00FF00, 0xFFFFFFFF});
        }
        return new ColorDrawable();
    }


    @Override
    public int getRowCount() {
        return _pages.size();
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }
}
