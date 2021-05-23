package com.cswala.cswala.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cswala.cswala.Adapters.BegineerAdapter;
import com.cswala.cswala.MainActivity;
import com.cswala.cswala.R;

import java.util.ArrayList;
import java.util.List;

public class BegineerActivity extends AppCompatActivity {

//    private TextView tvHead;
    private RecyclerView rvBegineer;
    private List<Pair<String,String>> stringList;
    BegineerAdapter begineerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begineer);

//        tvHead=findViewById(R.id.tvHead);
        rvBegineer=findViewById(R.id.rvBegineer);
        String Heading=getIntent().getStringExtra("heading");
//        tvHead.setText(Heading);
        stringList=new ArrayList<Pair<String,String>>();
        begineerAdapter=new BegineerAdapter(BegineerActivity.this,stringList);
        rvBegineer.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        rvBegineer.setAdapter(begineerAdapter);
        stringList.add(new Pair<String,String> ("Open a Github Account","https://github.com"));
        stringList.add(new Pair<String,String> ("Build a network","https://www.linkedin.com"));
        stringList.add(new Pair<String,String> ("Start Programming","https://www.programiz.com"));
        stringList.add(new Pair<String,String> ("Participate in Hackathon","https://www.hackerearth.com/challenges/hackathon/"));
        begineerAdapter.notifyDataSetChanged();
    }
}