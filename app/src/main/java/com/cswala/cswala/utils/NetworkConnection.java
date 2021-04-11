package com.cswala.cswala.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.cswala.cswala.R;
import com.google.android.material.snackbar.Snackbar;

public class NetworkConnection {

    private View parentLayout;

    public NetworkConnection(View view) {
        parentLayout = view;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public boolean isConnected(Context context) {
        NetworkInfo info = NetworkConnection.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public void ShowNoConnection() {

        Snackbar.make(parentLayout, R.string.no_connection, Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show();
    }
}
