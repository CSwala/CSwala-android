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
//                String data=documentSnapshot.getString("Documentation");
                getDocumentationList(documentSnapshot);
                getYoutubeList(documentSnapshot);
                getWebsiteList(documentSnapshot);
                getGithubList(documentSnapshot);
                getCourseList(documentSnapshot);
                getTipsList(documentSnapshot);
                String courseLinks=documentSnapshot.getString("Course1");
                String github=documentSnapshot.getString("Dedicated GH page1");
                String tips=documentSnapshot.getString("Tips1");
                String website=documentSnapshot.getString("Website1");
                String youtube=documentSnapshot.getString("YT1 ");
                String tag=documentSnapshot.getString("Tag");
                if(youtube==null)
                {
                    youtube=documentSnapshot.getString("YT1");
                }
                for(int i=2;;i++)
                {
                    String cr=documentSnapshot.getString("Course"+i);
                    String gh=documentSnapshot.getString("Dedicated GH page"+i);
                    if(cr!=null)
                    {
                        courseLinks=courseLinks+"\n\n"+cr;
                    }
                    else
                    {
                        break;
                    }
                }
                for(int i=2;;i++)
                {
                    String gh=documentSnapshot.getString("Dedicated GH page"+i);
                    if(gh!=null)
                    {
                        github=github+"\n\n"+gh;
                    }
                    else
                    {
                        break;
                    }
                }
                for(int i=2;;i++)
                {
                    String tip=documentSnapshot.getString("Tips"+i);
                    if(tip!=null)
                    {
                        tips=tips+"\n\n"+tip;
                    }
                    else
                    {
                        break;
                    }
                }
                for(int i=2;;i++)
                {
                    String web=documentSnapshot.getString("Website"+i);
                    if(web!=null)
                    {
                        website=website+"\n\n"+web;
                    }
                    else
                    {
                        break;
                    }
                }
                for(int i=2;;i++)
                {
                    String yt=documentSnapshot.getString("YT"+i+" ");
                    if(yt==null)
                    {
                        yt=documentSnapshot.getString("YT"+i);
                    }
                    if(yt!=null)
                    {
                        youtube=youtube+"\n\n"+yt;
                    }
                    else
                    {
                        break;
                    }
                }
                tagView.setText(tag);
//                youtubeView.setText(youtube);
//                websiteView.setText(website);
//                tipsView.setText(tips);
//                githubView.setText(github);
//                coursesView.setText(courseLinks);
//                documentationView.setText(data);
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
                webModel.setWebUrl(documentSnapshot.getString("YT"+i+" "));
                youtubeList.add(webModel);
//                youtube=youtube+"\n\n"+yt;
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
            if(yt==null)
            {
                yt=documentSnapshot.getString("YT"+i);
            }
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Website"+i));
                websiteList.add(webModel);
//                youtube=youtube+"\n\n"+yt;
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
            if(yt==null)
            {
                yt=documentSnapshot.getString("YT"+i);
            }
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Dedicated GH page"+i));
                githubList.add(webModel);
//                youtube=youtube+"\n\n"+yt;
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
            if(yt==null)
            {
                yt=documentSnapshot.getString("YT"+i);
            }
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Course"+i));
                courseList.add(webModel);
//                youtube=youtube+"\n\n"+yt;
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
            if(yt==null)
            {
                yt=documentSnapshot.getString("YT"+i);
            }
            if(yt!=null)
            {
                WebModel webModel=new WebModel();
                webModel.setWebUrl(documentSnapshot.getString("Tips"+i));
                tipList.add(webModel);
//                youtube=youtube+"\n\n"+yt;
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