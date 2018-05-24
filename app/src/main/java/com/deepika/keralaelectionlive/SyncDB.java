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
    int sync_count;
    String sync_date;
    View view;
    SQLiteDatabase db;
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String sourceSyncDate;
    DbHelper dbHelper;
    String defDate="1111-11-11 11:11:11";
    public SyncDB(Context c) {

        context = c;
        dbHelper=new DbHelper(context);
        sync_date = dbHelper.getLastSyncDate();
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String url = "https://keldeepika.ml/sync.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsresponse = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                            ArrayList<String> tables = getTableList(jsresponse);
                            for (int i = 0; i < tables.size(); i++) {
                                sync_count += getData(jsresponse, tables.get(i));
                            }
                            dbHelper.updateSyncDate(sourceSyncDate);
                            goHome();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error code 1 :\n\n"+e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "No Internet..!", Toast.LENGTH_SHORT).show();
                        goHome();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", sync_date);
                return params;
            }
        };
        queue.add(strRequest);
    }

    // to retrive data from net
    public int getData(JSONObject jsresponse, final String table) {
        Toast.makeText(context,table,Toast.LENGTH_SHORT).show();
        int sync_no = 0;
        try {
            Date on_create = dateTimeFormat.parse(defDate);
            Integer status;
            Integer cc = 0;
            JSONArray tableArray = jsresponse.optJSONArray(table);
            ArrayList<String> keys_list = new ArrayList<String>();
            for (int i = 0; i < tableArray.length(); i++) {
                JSONObject c = tableArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.clear();
                Iterator iterator = c.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if ((!key.trim().equals("on_create")) && (!key.trim().equals("on_update")) && (!key.trim().equals("status"))) {
                        values.put(key, c.getString(key));
                        keys_list.add(key);
                    }
                }
                on_create = dateTimeFormat.parse(c.getString("on_create"));
                status = c.getInt("status");

                if (on_create.compareTo(dateTimeFormat.parse(sync_date)) > 0) {
                    if (status == 1) {
                        // code for table creation and insertion
                        if (sync_date.equals(defDate) && cc == 0) {
                            cc++;
                            dbHelper.defineSchema(keys_list,table);
                        }
                        dbHelper.insertData(table,values);

                        sync_no++;
                    } else {
                        continue;
                    }
                } else {
                    if (status == 1) {
                        // Code for update
                        dbHelper.updateData(table,values);
                        sync_no++;
                    } else {
                        // Code for delete
                        dbHelper.deleteData(table,values);
                        sync_no++;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error code 3 :\n\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return sync_no;
    }

    public ArrayList<String> getTableList(JSONObject jsresponse) {
        ArrayList<String> table_list = new ArrayList<String>();
        try {
            JSONArray tableArray = jsresponse.optJSONArray("table_list");
            Date ssDate = dateTimeFormat.parse(defDate);
            for (int i = 0; i < tableArray.length(); i++) {
                JSONObject c = tableArray.getJSONObject(i);
                Iterator iterator = c.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (key.trim().equals("table_name")) {
                        table_list.add(c.getString(key));
                    }
                    if (key.trim().equals("on_update")) {
                        Date on_update = dateTimeFormat.parse(c.getString("on_update"));
                        if (on_update.compareTo(ssDate) > 0) {
                            sourceSyncDate = c.getString("on_update");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error code 4 :\n\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return table_list;
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
}
