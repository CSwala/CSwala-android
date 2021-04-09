package com.cswala.cswala.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cswala.cswala.Adapters.WebAdapter;
import com.cswala.cswala.Models.WebModel;
import com.cswala.cswala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TechDataActivity extends AppCompatActivity {
    TextView titleView,tagView;
    RecyclerView documentationView,youtubeView,websiteView,githubView,coursesView,tipsView;
    List<WebModel> documentationList,youtubeList,websiteList,githubList,courseList,tipList;
    WebAdapter webAdapter;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_data);
        Intent data=getIntent();
        String tech=data.getStringExtra("tech");
        firestore=FirebaseFirestore.getInstance();
        documentReference=firestore.collection("Dictionary").document(tech);
        documentationView=(RecyclerView) findViewById(R.id.documentation);
        titleView=(TextView) findViewById(R.id.title);
        youtubeView=(RecyclerView) findViewById(R.id.yotube_links);
        websiteView=(RecyclerView) findViewById(R.id.website_links);
        githubView=(RecyclerView) findViewById(R.id.github_links);
        coursesView=(RecyclerView) findViewById(R.id.best_courses);
        tipsView=(RecyclerView) findViewById(R.id.tips);
        tagView=(TextView) findViewById(R.id.tag);

        documentationList=new ArrayList<>();
        youtubeList=new ArrayList<>();
        websiteList=new ArrayList<>();
        githubList=new ArrayList<>();
        courseList=new ArrayList<>();
        tipList=new ArrayList<>();
        getData();
    }

    private void getData() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull @NotNull DocumentSnapshot documentSnapshot) {
                String title=documentSnapshot.getString("Title");
                getDocumentationList(documentSnapshot);
                getYoutubeList(documentSnapshot);
                getWebsiteList(documentSnapshot);
                getGithubList(documentSnapshot);
                getCourseList(documentSnapshot);
                getTipsList(documentSnapshot);
                String tag=documentSnapshot.getString("Tag");
                tagView.setText(tag);
                titleView.setText(title);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(TechDataActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDocumentationList(DocumentSnapshot documentSnapshot)
    {
        WebModel webModel=new WebModel();
        webModel.setWebUrl(documentSnapshot.getString("Documentation"));
        documentationList.add(webModel);
        documentationView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(documentationList,TechDataActivity.this);
        documentationView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }

    private void getYoutubeList(DocumentSnapshot documentSnapshot)
    {
        for(int i=1;;i++)
        {
            String yt=documentSnapshot.getString("YT"+i+" ");
            if(yt==null)
            {
                yt=documentSnapshot.getString("YT"+i);
            }
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(yt);
                youtubeList.add(webModel);
            }
            else
            {
                break;
            }
        }
        youtubeView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(youtubeList,TechDataActivity.this);
        youtubeView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }

    private void getWebsiteList(DocumentSnapshot documentSnapshot)
    {
        for(int i=1;;i++)
        {
            String yt=documentSnapshot.getString("Website"+i);
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Website"+i));
                websiteList.add(webModel);
            }
            else
            {
                break;
            }
        }
        websiteView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(websiteList,TechDataActivity.this);
        websiteView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }

    private void getGithubList(DocumentSnapshot documentSnapshot)
    {
        for(int i=1;;i++)
        {
            String yt=documentSnapshot.getString("Dedicated GH page"+i);
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Dedicated GH page"+i));
                githubList.add(webModel);
            }
            else
            {
                break;
            }
        }
        githubView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(githubList,TechDataActivity.this);
        githubView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }

    private void getCourseList(DocumentSnapshot documentSnapshot)
    {
        for(int i=1;;i++)
        {
            String yt=documentSnapshot.getString("Course"+i);
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Course"+i));
                courseList.add(webModel);
            }
            else
            {
                break;
            }
        }
        coursesView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(courseList,TechDataActivity.this);
        coursesView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }

    private void getTipsList(DocumentSnapshot documentSnapshot)
    {
        for(int i=1;;i++)
        {
            String yt=documentSnapshot.getString("Tips"+i);
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Tips"+i));
                tipList.add(webModel);
            }
            else
            {
                break;
            }
        }
        tipsView.setLayoutManager(new LinearLayoutManager(TechDataActivity.this,RecyclerView.HORIZONTAL,false));
        webAdapter=new WebAdapter(tipList,TechDataActivity.this);
        tipsView.setAdapter(webAdapter);
        webAdapter.notifyDataSetChanged();
    }
}