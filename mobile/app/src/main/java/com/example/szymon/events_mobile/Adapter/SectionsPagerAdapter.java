package com.example.szymon.events_mobile.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 17.05.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentsList = new ArrayList<>();
    private final List<String> titleFragmentList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentsList.add(fragment);
        titleFragmentList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }
}
