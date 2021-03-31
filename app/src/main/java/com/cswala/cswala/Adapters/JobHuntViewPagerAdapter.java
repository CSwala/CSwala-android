package com.cswala.cswala.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cswala.cswala.Fragments.JobFragment;
import com.cswala.cswala.Fragments.PortalFragment;

public class JobHuntViewPagerAdapter extends FragmentPagerAdapter {


    public JobHuntViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new PortalFragment();
        } else if (position == 1) {
            fragment = new JobFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Job-Portals";
        } else if (position == 1) {
            title = "Opportunities";
        }
        return title;
    }
}
