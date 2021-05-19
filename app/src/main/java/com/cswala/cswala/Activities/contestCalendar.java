package com.cswala.cswala.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cswala.cswala.Adapters.Api;
import com.cswala.cswala.Adapters.ContestAdapter;
import com.cswala.cswala.Models.Results;
import com.cswala.cswala.Models.contest_Results;
import com.cswala.cswala.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class contestCalendar extends AppCompatActivity {

    List<contest_Results> contest_results_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_calendar);

        final ProgressBar progressbar = findViewById(R.id.progressBar);

        progressbar.setVisibility(View.VISIBLE);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView_contest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kontests.net/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Intent data = getIntent();
        String site = data.getStringExtra("site");

        Call<List<Results>> call = null;
        if (site.equals("codeforces"))
            call = api.getcodeforces();

        if (site.equals("codechef"))
            call = api.getcode_chef();

        if (site.equals("at_coder"))
            call = api.getat_coder();

        if (site.equals("top_coder"))
            call = api.gettop_coder();

        if (site.equals("hacker_earth"))
            call = api.gethacker_earth();

        if (site.equals("hacker_rank"))
            call = api.gethacker_rank();

        if (site.equals("leetcode"))
            call = api.getleet_code();

        if (site.equals("google_kickstart"))
            call = api.getkick_start();


        call.enqueue(new Callback<List<Results>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {
                if (!response.isSuccessful()) {

                    Log.e("contestCalendar", "Code :" + response.code());
                    Toast.makeText(contestCalendar.this, "Error while fetching data!! Try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Results> result = response.body();

                Collections.sort(result, new Comparator<Results>() {
                    public int compare(Results o1, Results o2) {
                        if (o1.getStart_time().equals("-") || o2.getStart_time().equals("-"))
                            return 0;
                        Date d1 = null, d2=null, e1=null, e2=null;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        if(o1.getStart_time().equals(o2.getStart_time()))
                        {
                            try {
                                e1 = format.parse(o1.getEnd_time());
                                e2= format.parse(o2.getEnd_time());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return e1.compareTo(e2);
                        }
                        else
                        {
                            try {
                                d1 = format.parse(o1.getStart_time());
                                d2= format.parse(o2.getStart_time());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return d1.compareTo(d2);
                        }
                    }
                });

                if (result != null) {
                    for (int iter = 0; iter < result.size(); iter++) {
                        Results dataob = result.get(iter);
                        contest_Results contest_results_obj = new contest_Results();
                        String content = "";

                        contest_results_obj.setName(dataob.getName());

                        contest_results_obj.setUrl(dataob.getUrl());
                        String starttime = dataob.getStart_time();
                        if (!starttime.equals("-")) {
                            String formattedStartDate = changeTimeZone(starttime);
                            String finalStartDate = parseDateTo(formattedStartDate, "dd-MMM-yyyy");
                            String finalStartTime = parseDateTo(formattedStartDate, "HH:mm:ss");

                            contest_results_obj.setStart_time(finalStartDate + " " + finalStartTime);

                            String endtime = dataob.getEnd_time();
                            if (!endtime.equals("-")) {
                                String formattedEndDate = changeTimeZone(endtime);
                                String finalEndDate = parseDateTo(formattedEndDate, "dd-MMM-yyyy");
                                String finalEndTime = parseDateTo(formattedEndDate, "HH:mm:ss");

                                contest_results_obj.setEnd_time(finalEndDate + " " + finalEndTime);
                            } else {
                                contest_results_obj.setEnd_time("-");
                            }
                        } else {
                            contest_results_obj.setStart_time("-");
                        }
                        String durationstr = dataob.getDuration();
                        String duration = "";
                        for (int i = 0; i < durationstr.length(); i++) {

                            char a = durationstr.charAt(i);
                            if (a == '.') break;
                            duration += a;

                        }
                        if (!duration.equals("-")) {
                            long durationtime = Long.parseLong(duration);
                            long hours = durationtime / 3600;
                            long minutes = (durationtime % 3600) / 60;
                            long seconds = durationtime % 60;
                            String timeString;
                            if(hours<=24)
                            timeString = String.format("%02d hours,%02d minutes", hours, minutes);
                            else
                            {
                                long days= hours/24;
                                hours = hours - (days*24);
                                timeString = String.format("%02d days,%02d hours,%02d minutes",days, hours, minutes);
                            }

                            contest_results_obj.setDuration(timeString);
                        } else {
                            contest_results_obj.setDuration("-");
                        }

                        String statusval = dataob.getStatus();
                        if (statusval.equals("CODING")) {
                            statusval = "Contest Running";

                            contest_results_obj.setStatus(statusval);
                        } else if (statusval.equals("BEFORE")) {
                            statusval = "Contest not started";

                            contest_results_obj.setStatus(statusval);
                        }

                        contest_results_list.add(contest_results_obj);
                        if (iter + 1 == result.size()) {
                            if (contest_results_list != null) {

                                contest_Results[] contestdata = new contest_Results[contest_results_list.size()];
                                for (int i = 0; i < contest_results_list.size(); i++)
                                    contestdata[i] = contest_results_list.get(i);
                                progressbar.setVisibility(View.GONE);
                                ContestAdapter contestAdapter = new ContestAdapter(contestdata, contestCalendar.this);
                                recyclerView.setAdapter(contestAdapter);
                                recyclerView.scheduleLayoutAnimation();
                            }
                        }
                    }
                } else {
                    Toast.makeText(contestCalendar.this, "No Data to display!!", Toast.LENGTH_SHORT).show();
                    return;
                }


            }

            @Override
            public void onFailure(Call<List<Results>> call, Throwable t) {

                Log.e("CalendarOnFailure ", t.getMessage());
                Toast.makeText(contestCalendar.this, "Failed to fetch data!! Try again.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    String changeTimeZone(String input) {
        String dateStr = input;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);

        return formattedDate;
    }

    public String parseDateTo(String time, String outpat) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String outputPattern = outpat;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}