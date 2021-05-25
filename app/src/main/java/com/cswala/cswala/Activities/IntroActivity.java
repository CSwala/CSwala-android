package com.cswala.cswala.Activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.Nullable;

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //It's for app intro
        //This will maximize everything in app intro
        setImmersive(true);
        //For parallax effect
        setTransformer(new AppIntroPageTransformerType.Parallax(1, -1, 2));
        //Explore slide
        addSlide(AppIntroFragment.newInstance("Explore", "Discover everything in CS in the form of a Dicitionary", R.drawable.explore, getColor(R.color.appintro_background_color)));
        //Job Portal & Opportunities slide
        addSlide(AppIntroFragment.newInstance("JobHunt", "Get latest information about upcoming job opportunities. Here you can also explore Job Portals of top MNCs", R.drawable.job, getColor(R.color.appintro_background_color)));
        //News slide
        addSlide(AppIntroFragment.newInstance("Hackfeed", "Get Latest News related to different Tech. ", R.drawable.news, getColor(R.color.appintro_background_color)));
        // Events slide
        addSlide(AppIntroFragment.newInstance("Hackevents", "Get Information about the upcoming Tech events like Hackathon, Coding Contests and many more., ", R.drawable.events, getColor(R.color.appintro_background_color)));
        // Community slide
        addSlide(AppIntroFragment.newInstance("Community", "Connect with Community", R.drawable.community, getColor(R.color.appintro_background_color)));
        //Supported properties
        /*
         title = "The title of your slide",
    description = "A description that will be shown on the bottom",
    imageDrawable = R.drawable.the_central_icon,
    backgroundDrawable = R.drawable.the_background_image,
    titleColor = Color.YELLOW,
    descriptionColor = Color.RED,
    backgroundColor = Color.BLUE,
    titleTypefaceFontRes = R.font.opensans_regular,
    descriptionTypefaceFontRes = R.font.opensans_regular,
         */
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        IntentHelper intentHelper = new IntentHelper(this);
        intentHelper.GoToLogin();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        //Making Intro-class flag boolean to false
        IntentHelper intentHelper = new IntentHelper(this);
        intentHelper.GoToLogin();
        Prefs.putBoolean("isFirstTime", false);
    }
}
