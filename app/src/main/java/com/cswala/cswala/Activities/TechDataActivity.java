package com.cswala.cswala.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cswala.cswala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class TechDataActivity extends AppCompatActivity {
    TextView documentationView,titleView,youtubeView,websiteView,githubView,coursesView,tipsView,tagView;
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
        documentationView=(TextView) findViewById(R.id.documentation);
        titleView=(TextView) findViewById(R.id.title);
        youtubeView=(TextView) findViewById(R.id.yotube_links);
        websiteView=(TextView) findViewById(R.id.website_links);
        githubView=(TextView) findViewById(R.id.github_links);
        coursesView=(TextView) findViewById(R.id.best_courses);
        tipsView=(TextView) findViewById(R.id.tips);
        tagView=(TextView) findViewById(R.id.tag);
        getData();
    }

    private void getData() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull @NotNull DocumentSnapshot documentSnapshot) {
                String title=documentSnapshot.getString("Title");
                String data=documentSnapshot.getString("Documentation");
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
                youtubeView.setText(youtube);
                websiteView.setText(website);
                tipsView.setText(tips);
                githubView.setText(github);
                coursesView.setText(courseLinks);
                documentationView.setText(data);
                titleView.setText(title);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(TechDataActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}