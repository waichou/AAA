package com.framworks.base.example.tab_viewpager.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.framworks.base.example.tab_viewpager.fragment.TabFragment;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class TabPagerFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public TabPagerFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
