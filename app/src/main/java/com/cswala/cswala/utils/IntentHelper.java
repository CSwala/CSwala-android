package com.cswala.cswala.utils;

import android.content.Context;
import android.content.Intent;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cswala.cswala.Activities.IntroClass;
import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Activities.TechDataActivity;
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

    public void GoToTechData(String item)
    {
        Intent transfer=new Intent(context, TechDataActivity.class);
        transfer.putExtra("tech",item);
        context.startActivity(transfer);
    }
}