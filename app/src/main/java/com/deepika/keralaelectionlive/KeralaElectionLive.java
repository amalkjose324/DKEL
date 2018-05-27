package com.deepika.keralaelectionlive;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class KeralaElectionLive extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
