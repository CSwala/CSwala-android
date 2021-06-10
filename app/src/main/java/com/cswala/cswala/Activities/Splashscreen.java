package com.cswala.cswala.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;

import com.pixplicity.easyprefs.library.Prefs;


public class Splashscreen extends AppCompatActivity {

    private static int SPLASH= 3000;
    Animation topanim, bottomanim;
    ImageView imageView;
    TextView tv, tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView = (ImageView)findViewById(R.id.gifImageView);

        tv1 = (TextView)findViewById(R.id.textView2);

        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView.setAnimation(topanim);
        tv1.setAnimation(bottomanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Don't use Intent, use this helper class
                IntentHelper intentHelper=new IntentHelper(Splashscreen.this);
                //Checking if our user is for first time or not...
                if (Prefs.getBoolean("isFirstTime",true)) intentHelper.GoToIntro();
                else intentHelper.GoToLogin();
            }
        }, SPLASH);
    }
}