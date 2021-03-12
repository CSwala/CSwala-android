package com.cswala.cswala.utils;

import android.content.Context;
import android.content.Intent;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cswala.cswala.IntroClass;
import com.cswala.cswala.LoginActivity;
import com.cswala.cswala.MainActivity;

public class IntentHelper {
    Context context;

    public IntentHelper(Context context) {
        this.context = context;
    }

    public void GoToHome() {
        context.startActivity(new Intent(context, MainActivity.class));
        Animatoo.animateSlideLeft(context);
    }
    public void GoToLogin() {
        context.startActivity(new Intent(context, LoginActivity.class));
        Animatoo.animateSlideLeft(context);
    }
    public void GoToIntro() {
        context.startActivity(new Intent(context, IntroClass.class));
        Animatoo.animateSlideLeft(context);
    }
}