package com.cswala.cswala.Activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import com.cswala.cswala.R;
import com.cswala.cswala.utils.IntentHelper;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.pixplicity.easyprefs.library.Prefs;

import org.jetbrains.annotations.Nullable;

public class IntroClass extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //It's for app intro
        //This will maximize everything in app intro
        setImmersive(true);
        //For parallax effect
        setTransformer(new AppIntroPageTransformerType.Parallax(1, -1, 2));
        //For first slide
        addSlide(AppIntroFragment.newInstance("CS-Wala 1", "Description", R.drawable.ic_intro_one, getColor(R.color.appintro_background_color)));
        //For second slide
        addSlide(AppIntroFragment.newInstance("CS-Wala 2", "Description",R.drawable.ic_intro_one, getColor(R.color.appintro_background_color)));
        //For third slide
        addSlide(AppIntroFragment.newInstance("CS-Wala 3", "Description",R.drawable.ic_intro_one, getColor(R.color.appintro_background_color)));
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
        IntentHelper intentHelper=new IntentHelper(this);
        intentHelper.GoToLogin();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        //Making Intro-class flag boolean to false
        IntentHelper intentHelper=new IntentHelper(this);
        intentHelper.GoToLogin();
        Prefs.putBoolean("isFirstTime", false);
    }
}
