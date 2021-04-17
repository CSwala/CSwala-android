package com.cswala.cswala.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cswala.cswala.Fragments.LatestNewsFragment;
import com.cswala.cswala.Fragments.NewsEventsFragment;

public class LatestNewsAndEventsPageAdapter extends FragmentPagerAdapter {


    public LatestNewsAndEventsPageAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0)
            fragment = new LatestNewsFragment();
        else if (position == 1)
            fragment = new NewsEventsFragment();

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = null;

        if (position == 0)
            pageTitle = "Latest News";
        else if (position == 1)
            pageTitle = "Events";

        return pageTitle;
    }
}

