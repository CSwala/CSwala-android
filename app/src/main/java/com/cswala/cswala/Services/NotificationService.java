package com.cswala.cswala.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

import javax.annotation.Nullable;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "NOTIFICATION CHANNEL" ;
    FirebaseFirestore db;
    CollectionReference docRef;
    PendingIntent pendingIntent;
    private ListenerRegistration listener;

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

        // [END firestore_setup_client_create_with_project_id]
        // [END fs_initialize_project_id]
        listener = docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            private static final String TAG = "SERVICE CS-WALA";

            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                if (snapshots != null) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {

                        switch (dc.getType()) {
                            case ADDED:
                                Log.d("MY SERVICE CSWALA", dc.getDocument().getData().get("Title").toString());
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(dc.getDocument().getData().get("Title").toString())
                                        .setContentText("New portal added!")
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
                                notificationManager.notify(0, builder.build());

                                break;
                            case MODIFIED:
                                //System.out.println("Modified city: " + dc.getDocument().getData());
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
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener != null){
            listener.remove();
            listener = null;
        }
    }
}