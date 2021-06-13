package com.cswala.cswala;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Fragments.CommunityFragment;
import com.cswala.cswala.Fragments.ExploreFragment;
import com.cswala.cswala.Fragments.JobHunt;
import com.cswala.cswala.Fragments.NewsFragment;
import com.cswala.cswala.Fragments.ProfileFragment;
import com.cswala.cswala.Services.NotificationService;
import com.cswala.cswala.utils.NetworkConnection;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExploreFragment()).commit();
        }
        setContentView(R.layout.activity_main);

        View parentLayout = findViewById(android.R.id.content);
        NetworkConnection networkConnection = new NetworkConnection(parentLayout);

        if (!networkConnection.isConnected(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        }

        bt = findViewById(R.id.bottom_navigation);
        bt.setItemSelected(R.id.explore, true);

        startService(new Intent(this, NotificationService.class));
        bt.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {


                Fragment fragment;

                switch(i) {

                    case R.id.jobhunt:
                        fragment = new JobHunt();
                        break;

                    case R.id.hackfeed:
                        //HACK FEED Fragment
                        fragment = new NewsFragment();
                        break;

                    case R.id.community:
                        fragment = new CommunityFragment();
                        break;

                    case R.id.profile:
                        //PROFILE Fragment
                        fragment = new ProfileFragment();
                        break;

                    default:
                        fragment = new ExploreFragment();
                        break;
                }


                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.frag_fade_in, R.anim.frag_fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.fragment_container,fragment).commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    public void ExitDialog(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_exit,null);

        final Button loginBtn_git = mView.findViewById(R.id.login_git);
        final Button cancelBtn_git = mView.findViewById(R.id.cancel_git);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        cancelBtn_git.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink_anim);
                    cancelBtn_git.startAnimation(animation);
                    alertDialog.dismiss();
                }
            });
        loginBtn_git.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink_anim);
                    loginBtn_git.startAnimation(animation);
                    finishAffinity();
                }
            });

        alertDialog.show();
        }
    }