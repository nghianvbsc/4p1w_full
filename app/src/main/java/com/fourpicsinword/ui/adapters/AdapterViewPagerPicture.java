package com.fourpicsinword.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fourpicsinword.ui.fragments.FragmentPicture;

/**
 * Created by NGHIA_IT on 12/21/2015.
 */
public class AdapterViewPagerPicture extends FragmentPagerAdapter {

    private int[] listUrlPicture;
    public AdapterViewPagerPicture(FragmentManager fm, int[] listUrlPicture) {
        super(fm);
        this.listUrlPicture = listUrlPicture;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentPicture.newInstance(listUrlPicture[position]);
    }

    @Override
    public int getCount() {
        return listUrlPicture == null? 0: listUrlPicture.length;
    }
}
