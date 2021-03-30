package com.cswala.cswala.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cswala.cswala.Adapters.JobHuntViewPagerAdapter;
import com.cswala.cswala.R;
import com.google.android.material.tabs.TabLayout;

public class JobHunt extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    JobHuntViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_job_hunt, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        viewPagerAdapter = new JobHuntViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}