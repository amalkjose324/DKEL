package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SyncDB {
    private Context context;
    DbHelper dbHelper=new DbHelper(context);
    FirebaseDatabase database;
    public SyncDB(Context c) {
        this.context=c;
        database = FirebaseDatabase.getInstance();
         readData("dkel_domains");
            goHome();
    }
    public void goHome(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }, 6000);
    }
    public void readData(final String table){
            DatabaseReference myRef = database.getReference(table);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ContentValues values = new ContentValues();
                        String value = snapshot.getValue().toString();
                        value = value.substring(1, value.length() - 1);
                        String[] arrayString = value.split(",");
                        for (String s : arrayString) {
                            values.put(s.split("=")[0], s.split("=")[1]);
                        }
                        dbHelper.insertData(table, values);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(context,error.toException().toString(),Toast.LENGTH_SHORT).show();
                }
            });
    }
}
