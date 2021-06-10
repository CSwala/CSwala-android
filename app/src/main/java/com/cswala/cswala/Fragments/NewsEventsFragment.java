package com.cswala.cswala.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cswala.cswala.Activities.contestCalendar;
import com.cswala.cswala.R;

public class NewsEventsFragment extends Fragment {

    public NewsEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_events, container, false);
        Button codeforces_btn = (Button) view.findViewById(R.id.codeforces);
        Button codechef_btn = (Button) view.findViewById(R.id.codechef);
        Button at_coder_btn = (Button) view.findViewById(R.id.at_coder);
        Button top_coder_btn = (Button) view.findViewById(R.id.top_coder);
        Button hacker_earth_btn = (Button) view.findViewById(R.id.hacker_earth);
        Button hacker_rank_btn = (Button) view.findViewById(R.id.hacker_rank);
        Button leetcode_btn = (Button) view.findViewById(R.id.leetcode);
        Button google_kickstart_btn = (Button) view.findViewById(R.id.faang);

        codeforces_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "codeforces");
                startActivity(i);
            }
        });

        codechef_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "codechef");
                startActivity(i);
            }
        });

        at_coder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "at_coder");
                startActivity(i);
            }
        });

        top_coder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "top_coder");
                startActivity(i);
            }
        });

        hacker_earth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "hacker_earth");
                startActivity(i);
            }
        });

        hacker_rank_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "hacker_rank");
                startActivity(i);
            }
        });

        leetcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "leetcode");
                startActivity(i);
            }
        });

        google_kickstart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), contestCalendar.class);
                i.putExtra("site", "google_kickstart");
                startActivity(i);
            }
        });
        return view;
    }
}