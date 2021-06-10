package com.cswala.cswala.utils;

import android.content.Context;
import android.content.Intent;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.cswala.cswala.Activities.BegineerActivity;
import com.cswala.cswala.Activities.IntroActivity;
import com.cswala.cswala.Activities.LoginActivity;
import com.cswala.cswala.Activities.LoginWithEmail;
import com.cswala.cswala.Activities.Register;
import com.cswala.cswala.Activities.ResetPassword;
import com.cswala.cswala.Activities.TechDataActivity;
import com.cswala.cswala.Activities.WebActivity;
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
        context.startActivity(new Intent(context, IntroActivity.class));
        Animatoo.animateSlideLeft(context);
    }

    public void GoToLoginWithEmail(){
        context.startActivity(new Intent(context, LoginWithEmail.class));
        Animatoo.animateSlideLeft(context);
    }

    public void GoToRegister(){
        context.startActivity(new Intent(context, Register.class));
        Animatoo.animateSlideLeft(context);
    }

    public void GoToForgetPassword(){
        context.startActivity(new Intent(context, ResetPassword.class));
        Animatoo.animateSlideLeft(context);
    }
    public void GoToTechData(String item)
    {
        Intent transfer=new Intent(context, TechDataActivity.class);
        transfer.putExtra("tech",item);
        context.startActivity(transfer);
    }

    public void GoToWeb(String WebUrl)
    {
        Intent intent=new Intent(context, WebActivity.class);
        intent.putExtra("URL",WebUrl);
        context.startActivity(intent);
    }
    public void GoToBegineer(String Heading)
    {
        Intent intent=new Intent(context, BegineerActivity.class);
        intent.putExtra("heading",Heading);
        context.startActivity(intent);
    }
}