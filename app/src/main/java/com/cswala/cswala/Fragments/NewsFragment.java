package com.cswala.cswala.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.cswala.cswala.Adapters.LatestNewsAndEventsPageAdapter;
import com.cswala.cswala.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class NewsFragment extends Fragment {
    TabLayout newsTabLayout;
    ViewPager newsViewPager;
    LatestNewsAndEventsPageAdapter newsViewPagerAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsViewPager = view.findViewById(R.id.newsViewPager);

        newsViewPagerAdapter = new LatestNewsAndEventsPageAdapter(getChildFragmentManager());
        newsViewPager.setAdapter(newsViewPagerAdapter);

        newsTabLayout = view.findViewById(R.id.newsTabs);
        newsTabLayout.setupWithViewPager(newsViewPager);
        return view;
    }
}