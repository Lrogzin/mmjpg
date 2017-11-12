package com.lrogzin.mmjpg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by 1U02UN on 2017/10/24.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titleList;
    private ArrayList<BaseFragment> fragmentList;

    public FragmentAdapter(FragmentManager fm, ArrayList<String> titleList, ArrayList<BaseFragment> fragmentList) {
        super(fm);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {}

    @Override
    public void destroyItem(View container, int position, Object object) {}
}

