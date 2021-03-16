package com.cswala.cswala.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cswala.cswala.Fragments.CommunityFragment;
import com.cswala.cswala.Fragments.ExploreFragment;
import com.cswala.cswala.Fragments.JobHunt;
import com.cswala.cswala.Fragments.NewsFragment;
import com.cswala.cswala.Fragments.ProfileFragment;
import com.cswala.cswala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar bt;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bottom_navigation);
        bt.setItemSelected(R.id.explore, true);

        bt.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                Fragment fragment = null;

                switch(i) {

                    case R.id.explore:
                        fragment = new ExploreFragment();
                        break;

                    case R.id.jobhunt:
                        fragment = new JobHunt();
                        break;

                    case R.id.hackfeed:
                        fragment = new NewsFragment();
                        break;

                    case R.id.communtiy:
                        fragment = new CommunityFragment();
                        break;

                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }

                if(fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
            }
        });

        TextView tv = findViewById(R.id.logout);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}