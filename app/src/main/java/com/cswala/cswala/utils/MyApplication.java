package com.cswala.cswala.utils;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PrefInitz();
    }

    private void PrefInitz() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
