package com.smartcl.account_alert_wear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bourdi_b on 25/02/2015.
 */
public class AccountPagerAdapter extends FragmentGridPagerAdapter {

    private final Context _context;
    private final List<AccountPage> _pages;

    public AccountPagerAdapter(Context ctx, FragmentManager fm, List<Account> accounts) {
        super(fm);
        _context = ctx;
        _pages = new ArrayList(accounts.size());
        for (Account account : accounts) {
            _pages.add(new AccountPage(account));
        }
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        AccountPage page = _pages.get(row);
        CustomFragment card = CustomFragment.newInstance(page.getAccount());
        return card;
    }

    // Obtain the background image for the page at the specified position
    //@Override
    //public ImageReference getBackground(int row, int column) {
    //   return ImageReference.forDrawable(BG_IMAGES[row % BG_IMAGES.length]);
    // }

    // Override methods in FragmentGridPagerAdapter
    // Obtain the number of pages (vertical)
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
