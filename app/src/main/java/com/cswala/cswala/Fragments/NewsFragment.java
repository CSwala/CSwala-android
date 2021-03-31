package com.cswala.cswala.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cswala.cswala.R;
import com.google.android.material.tabs.TabLayout;

public class NewsFragment extends Fragment {
    public static final String TAG = "HackFeed_fragment_click";

    FragmentManager fragmentManager;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        final TabLayout tabLayout = view.findViewById(R.id.tab_hf);

        if(savedInstanceState==null) {
            // Set default fragment here to be set on first time app launch

            tabLayout.selectTab(tabLayout.getTabAt(0));
            fragmentManager = getParentFragmentManager();
            LatestNewsFragment defaultFragment = new LatestNewsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_hackfeed, defaultFragment)
                    .commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.select();
                Fragment fragment = null;
                if(tab.getPosition()==0){
                    fragment = new LatestNewsFragment();
                }else if(tab.getPosition()==1) {
                    fragment = new NewsEventsFragment();
                }

                if(fragment!=null){
                    fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_hackfeed, fragment)
                            .commit();
                }else{
                    Log.e(TAG, "Error in creating fragment");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}