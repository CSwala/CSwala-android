package com.cswala.cswala.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cswala.cswala.MainActivity;
import com.cswala.cswala.Models.PortalListElement;
import com.cswala.cswala.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "NOTIFICATION CHANNEL" ;
    FirebaseFirestore db;
    CollectionReference docRef;
    PendingIntent pendingIntent;
    private ListenerRegistration portallistener, dictListener;
    private SharedPreferences sp;
    private Set<String> myTitles;
    private static final String TAG = "SERVICE CS-WALA";

    public NotificationService() {


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("Portal");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        sp = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        myTitles = sp.getStringSet(getString(R.string.TITLES), new HashSet<String>());

        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                NotificationService.this.notify(snapshots,e);
            }
        };
        portallistener = docRef.addSnapshotListener(eventListener);
        dictListener = db.collection("Dictionary").addSnapshotListener(eventListener);
    }
    void notify(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e){
        if (e != null) {
            Log.w(TAG, "listen:error", e);
            return;
        }
        if (snapshots != null) {
            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        if(!myTitles.contains(Objects.requireNonNull(dc.getDocument().getData().get("Title")).toString())) {
                            showNotification(Objects.requireNonNull(dc.getDocument().getData().get("Title")).toString(),
                                    dc.getDocument().getData().containsKey("iscompany") ? "New portal added!" : "New Dictionary Added",
                                    dc.getDocument().getData().containsKey("iscompany") ? 0 : 1);

                            myTitles.add(Objects.requireNonNull(dc.getDocument().getData().get("Title")).toString());
                            sp.edit().putStringSet(getString(R.string.TITLES),myTitles).apply();
                        }
                        break;
                    case MODIFIED:
                        showNotification(Objects.requireNonNull(dc.getDocument().getData().get("Title")).toString(),
                                dc.getDocument().getData().containsKey("iscompany") ? "New data available" : "The dictionary was updated!",
                                dc.getDocument().getData().containsKey("iscompany") ? 0 : 1);

                        myTitles.add(Objects.requireNonNull(dc.getDocument().getData().get("Title")).toString());
                        break;
                    case REMOVED:
                        //System.out.println("Removed city: " + dc.getDocument().getData());
                        break;
                    default:
                        break;
                }
            }
        }
    }
    void showNotification(String title, String subTitle, int channelId){
        Log.d("MY SERVICE CSWALA", title);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(channelId, builder.build());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(portallistener != null){
            portallistener.remove();
            portallistener = null;
        }
        if(dictListener != null){
            dictListener.remove();
            dictListener = null;
        }
    }
}