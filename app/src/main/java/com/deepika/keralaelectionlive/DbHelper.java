package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Amal K Jose on 04-03-2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dkel_db";

    // Contacts table name
    private static final String TABLE_CONFIG = "dkel_config";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_config(config_id INTEGER PRIMARY KEY AUTOINCREMENT, config_key VARCHAR, config_value VARCHAR);");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('sync_date','1111-11-11 11:11:11')");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('pref_language',null)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_domain_name VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);

        onCreate(db);
    }

    //To update sync date in android database
    public void updateSyncDate(String date) {
        if(date !=null){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE dkel_config SET config_value='" + date + "' WHERE config_key='sync_date'");
        }
    }

    //To get last sync date
    public String getLastSyncDate() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("SELECT `config_value` FROM `dkel_config` WHERE `config_key`='sync_date'", null);
            cur.moveToFirst();
            return cur.getString(0);
    }

    //To test langusge selected
    public String getLanguageSelected() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT `config_value` FROM `dkel_config` WHERE `config_key`='pref_language'", null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            return cur.getString(0);
        } else {
            return null;
        }
    }

    //To set langusge
    public void setLanguageSelected(String language){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE dkel_config SET config_value='" + language + "' WHERE config_key='pref_language'");
    }

    //To create tables by sync (Automatic)
    public void defineSchema(ArrayList<String> keys_list, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        String schema_def = "";
        for (int x = 0; x < keys_list.size(); x++) {
            if (keys_list.get(x).contains("id")) {
                if (x == keys_list.size() - 1) {
                    schema_def += keys_list.get(x) + " INTEGER ";
                } else {
                    schema_def += keys_list.get(x) + " INTEGER, ";
                }
            } else {
                if (x == keys_list.size() - 1) {
                    schema_def += keys_list.get(x) + " VARCHAR ";
                } else {
                    schema_def += keys_list.get(x) + " VARCHAR, ";
                }
            }
        }
        db.execSQL("CREATE TABLE IF NOT EXISTS " + table + "(" + schema_def + ");");
    }

    //To insert data to specified table
    public void insertData(String table,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, values);
    }

    //To Update data to specified table
    public void updateData(String table,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, values, "id=" + values.get("id"), null);
    }

    //To Delete data to specified table
    public void deleteData(String table,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, "id=" + values.get("id"), null);
    }


    //To get constituencie names Malayalam/English
    public ArrayList<String> getConstNames(String language) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> const_names=new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domains`", null);
        while (cur.moveToNext()){
            const_names.add(cur.getString(language.equals("eng") ? 1 : 2));
        }
        return const_names;
    }

    //To check favorite status
    public boolean getFavStatus(String domain_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_favourites` WHERE `fav_domain_name`='"+domain_name+"'", null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Adding domain to favorite
    public void addToFavourite(String domain_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_favourites` (`fav_domain_name`) VALUES('"+domain_name+"')");
    }

    //Remove domain from favorite
    public void removeFromFavourite(String domain_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_favourites` WHERE `fav_domain_name`='"+domain_name+"'");
    }
}
