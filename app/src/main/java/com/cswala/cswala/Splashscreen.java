package com.cswala.cswala;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cswala.cswala.utils.IntentHelper;

import com.pixplicity.easyprefs.library.Prefs;


public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView tv = findViewById(R.id.tv);
        TextView tv1 = findViewById(R.id.textView2);
        int SPLASH = 3000;
        Animation animation = AnimationUtils.loadAnimation(Splashscreen.this, R.anim.text_anim);
        tv.startAnimation(animation);
        tv1.startAnimation(animation);
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
