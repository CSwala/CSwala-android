package com.cswala.cswala.utils;

import com.google.firebase.database.DatabaseError;

public interface MessageListener {
    void onMessageReceived();

    void onError(DatabaseError error);
}
