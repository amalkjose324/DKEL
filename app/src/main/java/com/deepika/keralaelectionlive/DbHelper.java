package com.deepika.keralaelectionlive;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Amal K Jose on 04-03-2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dkel_db";
    HashMap<String,HashMap<String,String>> tbl_domains;

    // Contacts table name
    private static final String TABLE_CONFIG = "dkel_config";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        tbl_domains=new HashMap<>();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_config(config_id INTEGER PRIMARY KEY AUTOINCREMENT, config_key VARCHAR, config_value VARCHAR);");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('sync_date','1111-11-11 11:11:11')");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('pref_language',null)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domain_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_domain_id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidate_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_candidate_name VARCHAR)");

//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidates(candidate_id INTEGER PRIMARY KEY AUTOINCREMENT,candidate_name VARCHAR,candidate_domain SMALLINT,candidate_image VARCHAR,candidate_party SMALLINT)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domains(id INTEGER PRIMARY KEY AUTOINCREMENT,domain_name_eng VARCHAR,domain_name_mal VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_labels(id INTEGER PRIMARY KEY AUTOINCREMENT,label_name VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_parties(id INTEGER PRIMARY KEY AUTOINCREMENT,party_name VARCHAR,party_image VARCHAR,party_label TINYINT)");
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

    //To insert data to specified table
    public void insertData(String table,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert(table, null, values);
        }catch (Exception e){
            db.update(table,values,"id="+values.get("id"),null);
            System.out.print(e.toString());
        }

    }
    //To insert data to Firebase sync  table
    public void firebaseInsert(String table,ContentValues values,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            Cursor cur = db.rawQuery("SELECT * FROM "+table+" WHERE `id`="+id, null);
            if (cur.getCount() > 0) {
                db.update(table,values,"id="+id,null);
            } else {
                db.insert(table, null, values);
            }
        }catch (Exception e){
            System.out.print(e.toString());
        }
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

    //To get Domain names
//    ArrayList<String> domain_names=new ArrayList<>();
    LinkedHashMap<Integer,String> domain_names=new LinkedHashMap<Integer, String>();
    public LinkedHashMap<Integer,String> getDomainNames() {
        final SQLiteDatabase db = this.getReadableDatabase();
        final LinkedHashMap<Integer,String> domain_nonfav=new LinkedHashMap<Integer, String>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("dkel_domains");
        database.keepSynced(true);
        database.orderByChild("domain_name");
        database.orderByChild("domain_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                domain_names.clear();
                final ArrayList<Integer> fav_list=new ArrayList<>();
                Cursor cur = db.rawQuery("SELECT * FROM `dkel_domain_favourites`", null);
                while (cur.moveToNext()){
                    fav_list.add(cur.getInt(cur.getColumnIndex("fav_domain_id")));
                }
                for(DataSnapshot value:dataSnapshot.getChildren()){
                    Integer domains_id=Integer.parseInt(value.child("domain_id").getValue().toString());
                    String domain_name=value.child("domain_name").getValue().toString();
                    if(fav_list.contains(domains_id)){
                        domain_names.put(domains_id,domain_name);
                    }else{
                        domain_nonfav.put(domains_id,domain_name);
                    }
                }
                domain_names.putAll(domain_nonfav);
                TabDomains tabDomains=new TabDomains();
                tabDomains.setListValues(domain_names);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return domain_names;
    }

    //To get Candidate details names
//    HashMap<String,HashMap<String,String>> candidate_details=new HashMap<>();
//    public ArrayList<String> getCandidateDetails() {
//        final SQLiteDatabase db = this.getReadableDatabase();
//        final ArrayList<String> domain_temp=new ArrayList<>();
//        Cursor cur2 = db.rawQuery("SELECT * FROM `dkel_candidate_favourites` ORDER BY `fav_candidate_name`", null);
//        while (cur2.moveToNext()){
//            domain_temp.add(cur2.getString(cur2.getColumnIndex("fav_domain_name")));
//        }
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("dkel_domains");
//        database.keepSynced(true);
//        database.orderByChild("dkel_domains");
//        database.orderByChild("domain_name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                domain_names.clear();
//                ArrayList<String> domain_fav=new ArrayList<>();
//
//                for(DataSnapshot value:dataSnapshot.getChildren()){
//                    String domains=value.child("domain_name").getValue().toString();
//                    if(!domain_temp.contains(domains)){
//                        domain_names.add(domains);
//                    }else{
//                        domain_fav.add(domains);
//                    }
//                }
//                TabDomains tabDomains=new TabDomains();
//                domain_fav.addAll(domain_names);
//                tabDomains.setListValues(domain_fav);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Toast.makeText(context, error.toException().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        return domain_names;
//    }
    //To get Favourite Domain names
    public ArrayList<String> getFavDomainNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> domain_names=new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domain_favourites` ", null);
        while (cur.moveToNext()){
            domain_names.add(cur.getString(cur.getColumnIndex("fav_domain_name")));
        }
        return domain_names;
    }
    //To get Favourite Candidate names
    public ArrayList<String> getFavCandidateNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> candidate_names=new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidate_favourites` ", null);
        while (cur.moveToNext()){
            candidate_names.add(cur.getString(cur.getColumnIndex("fav_candidate_name")));
        }
        return candidate_names;
    }

    //To check favorite status - domains
    public boolean getDomainFavStatus(Integer domain_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domain_favourites` WHERE `fav_domain_id`="+domain_id, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    //To check favorite status - candidates
    public boolean getCandidateFavStatus(String candidate_name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidate_favourites` WHERE `fav_candidate_name`='"+candidate_name+"'", null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Adding domain to favorite
    public void addDomainToFavourite(Integer domain_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_domain_favourites` (`fav_domain_id`) VALUES("+domain_id+")");
    }
    //Adding candidate to favorite
    public void addCandidateToFavourite(String candidate_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_candidate_favourites` (`fav_candidate_name`) VALUES('"+candidate_name+"')");
    }

    //Remove domain from favorite
    public void removeDomainFromFavourite(Integer domain_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_domain_favourites` WHERE `fav_domain_id`="+domain_id);
    }
    //Remove candidate from favorite
    public void removeCandidateFromFavourite(String candidate_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_candidate_favourites` WHERE `fav_candidate_name`='"+candidate_name+"'");
    }
}
