package com.deepika.keralaelectionlive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Amal K Jose on 04-03-2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    LinkedHashMap<Integer, HashMap<String, String>> candidate_details = new LinkedHashMap<Integer, HashMap<String, String>>();
    LinkedHashMap<Integer, String> candidate_votes = new LinkedHashMap<Integer, String>();

    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dkel_db";
    HashMap<String, HashMap<String, String>> tbl_domains;

    // Contacts table name
    private static final String TABLE_CONFIG = "dkel_config";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        tbl_domains = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_config(config_id INTEGER PRIMARY KEY AUTOINCREMENT, config_key VARCHAR, config_value VARCHAR);");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('sync_date','1111-11-11 11:11:11')");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('pref_language',null)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domain_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_domain_name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidate_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_candidate_id INTEGER)");

//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidates(candidate_id INTEGER PRIMARY KEY AUTOINCREMENT,candidate_name VARCHAR,candidate_domain SMALLINT,candidate_image VARCHAR,candidate_party SMALLINT)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domains(id INTEGER PRIMARY KEY AUTOINCREMENT,domain_name_eng VARCHAR,domain_name_mal VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_panels(id INTEGER PRIMARY KEY AUTOINCREMENT,panel_name VARCHAR)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_parties(id INTEGER PRIMARY KEY AUTOINCREMENT,party_name VARCHAR,party_image VARCHAR,party_panel TINYINT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG);
        onCreate(db);
    }

    //To update sync date in android database
    public void updateSyncDate(String date) {
        if (date != null) {
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
    public void setLanguageSelected(String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE dkel_config SET config_value='" + language + "' WHERE config_key='pref_language'");
    }

    //To insert data to specified table
    public void insertData(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.insert(table, null, values);
        } catch (Exception e) {
            db.update(table, values, "id=" + values.get("id"), null);
            System.out.print(e.toString());
        }

    }

    //To Update data to specified table
    public void updateData(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, values, "id=" + values.get("id"), null);
    }

    //To Delete data to specified table
    public void deleteData(String table, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, "id=" + values.get("id"), null);
    }

    public void getFirebaseVotes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dkel_votes");
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap<String, String> details = new HashMap<String, String>();
                CandidateVotes votes = dataSnapshot.getValue(CandidateVotes.class);
                candidate_votes.put(votes.vote_candidate_id, String.valueOf(votes.vote_candidate_vote));
                details = candidate_details.get(votes.vote_candidate_id);
                details.put("votes", String.valueOf(votes.vote_candidate_vote));
                candidate_details.put(votes.vote_candidate_id, details);
                pushCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap<String, String> details = new HashMap<String, String>();
                CandidateVotes votes = dataSnapshot.getValue(CandidateVotes.class);
                candidate_votes.put(votes.vote_candidate_id, String.valueOf(votes.vote_candidate_vote));
                details = candidate_details.get(votes.vote_candidate_id);
                details.put("votes", String.valueOf(votes.vote_candidate_vote));
                candidate_details.put(votes.vote_candidate_id, details);
                pushCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                HashMap<String, String> details = new HashMap<String, String>();
                CandidateVotes votes = dataSnapshot.getValue(CandidateVotes.class);
                candidate_votes.put(votes.vote_candidate_id, String.valueOf(votes.vote_candidate_vote));
                details = candidate_details.get(votes.vote_candidate_id);
                details.put("votes", "0");
                candidate_details.put(votes.vote_candidate_id, details);
                pushCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getFirebaseDetails() {
        getFirebaseVotes();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("dkel_candidates");
        ref.keepSynced(true);
        ref.orderByChild("candidate_name");
        ref.orderByChild("candidate_name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap<String, String> details = new HashMap<String, String>();
                CandidateDetails candidate = dataSnapshot.getValue(CandidateDetails.class);
                details.put("id", String.valueOf(candidate.candidate_id));
                details.put("name", candidate.candidate_name);
                details.put("domain", candidate.candidate_domain);
                details.put("party", candidate.candidate_party);
                details.put("panel", candidate.candidate_panel);
                details.put("image", candidate.candidate_image);
                details.put("votes", candidate_votes.get(candidate.candidate_id) != null ? candidate_votes.get(candidate.candidate_id) : "0");
                candidate_details.put(candidate.candidate_id, details);
                pushDomainList();
                pushCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap<String, String> details = new HashMap<String, String>();
                CandidateDetails candidate = dataSnapshot.getValue(CandidateDetails.class);
                details.put("id", String.valueOf(candidate.candidate_id));
                details.put("name", candidate.candidate_name);
                details.put("domain", candidate.candidate_domain);
                details.put("party", candidate.candidate_party);
                details.put("panel", candidate.candidate_panel);
                details.put("image", candidate.candidate_image);
                details.put("votes", candidate_votes.get(candidate.candidate_id) != null ? candidate_votes.get(candidate.candidate_id) : "0");
                candidate_details.put(candidate.candidate_id, details);
                pushDomainList();
                pushCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CandidateDetails candidate = dataSnapshot.getValue(CandidateDetails.class);
                candidate_details.remove(candidate.candidate_id);
                pushDomainList();
                pushCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //To push to domain list view
    public void pushDomainList() {
        ArrayList<String> domain_names = new ArrayList<>();
        final ArrayList<String> domain_nonfav = new ArrayList<>();
        domain_names.clear();
        for (Integer candidate : candidate_details.keySet()) {
            String domain = candidate_details.get(candidate).get("domain");
            if (getDomainFavStatus(domain) && !domain_names.contains(domain)) {
                domain_names.add(domain);
            } else if (!(domain_nonfav.contains(domain) || domain_names.contains(domain))) {
                domain_nonfav.add(domain);
            }
        }
        Collections.sort(domain_names,String.CASE_INSENSITIVE_ORDER);
        Collections.sort(domain_nonfav,String.CASE_INSENSITIVE_ORDER);
        domain_names.addAll(domain_nonfav);
        TabDomains tabDomains = new TabDomains();
        tabDomains.setListValues(domain_names);
    }

    //To push to candidate list view
    public void pushCandidateList() {
        ArrayList<HashMap<String,String>> candidate_names=new ArrayList<>();
        ArrayList<HashMap<String,String>> candidate_nonfav=new ArrayList<>();
        candidate_names.clear();
        for (Integer candidate : candidate_details.keySet()) {
            if (getCandidateFavStatus(candidate)) {
                candidate_names.add(candidate_details.get(candidate));
            }else{
                candidate_nonfav.add(candidate_details.get(candidate));
            }
        }
        candidate_names.addAll(candidate_nonfav);
        TabCandidates tabCandidates = new TabCandidates();
        tabCandidates.setListValues(candidate_names);
    }

    //To check favorite status - domains
    public boolean getDomainFavStatus(String domain_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domain_favourites` WHERE `fav_domain_name`='" + domain_name + "'", null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //To check favorite status - candidates
    public boolean getCandidateFavStatus(Integer fav_candidate_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidate_favourites` WHERE `fav_candidate_id`='" + fav_candidate_id+"'", null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //Adding domain to favorite
    public void addDomainToFavourite(String domain_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_domain_favourites` (`fav_domain_name`) VALUES('" + domain_name + "')");
    }

    //Adding candidate to favorite
    public void addCandidateToFavourite(Integer fav_candidate_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_candidate_favourites` (`fav_candidate_id`) VALUES(" + fav_candidate_id + ")");
    }

    //Remove domain from favorite
    public void removeDomainFromFavourite(String domain_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_domain_favourites` WHERE `fav_domain_name`='" + domain_name + "'");
    }

    //Remove candidate from favorite
    public void removeCandidateFromFavourite(Integer fav_candidate_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_candidate_favourites` WHERE `fav_candidate_id`=" + fav_candidate_id);
    }
}
