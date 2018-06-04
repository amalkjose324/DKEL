package com.deepika.keralaelectionlive;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dkel_db";
    HashMap<String, HashMap<String, String>> tbl_domains;
    Session session;
    // Contacts table name
    private static final String TABLE_CONFIG = "dkel_config";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        tbl_domains = new HashMap<>();
        session = new Session(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_config(config_id INTEGER PRIMARY KEY AUTOINCREMENT, config_key VARCHAR, config_value VARCHAR);");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('sync_date','1111-11-11 11:11:11')");
        db.execSQL("INSERT INTO dkel_config(config_key,config_value) VALUES('pref_language',null)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domain_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_domain_id INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidate_favourites(fav_id INTEGER PRIMARY KEY AUTOINCREMENT,fav_candidate_id INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_candidates(candidate_id INTEGER PRIMARY KEY ,candidate_name VARCHAR,candidate_domain SMALLINT,candidate_image VARCHAR,candidate_party SMALLINT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_domains(domain_id INTEGER PRIMARY KEY ,domain_name VARCHAR,domain_status TINYINT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_panels(panel_id INTEGER PRIMARY KEY,panel_name VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_parties(party_id INTEGER PRIMARY KEY,party_name VARCHAR,party_image VARCHAR,party_panel TINYINT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_votes(vote_id INTEGER PRIMARY KEY ,vote_votes INTEGER)");

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
        db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    //To truncate table
    public void truncateTable(String table, SQLiteDatabase db) {
        db.delete(table, null, null);
    }

    //To Update data to specified table
    public void updateData(String table, ContentValues values, String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, values, condition, null);
    }

    //To Delete data to specified table
    public void deleteData(String table, String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, condition, null);
    }

    //====================================== READ DATA FROM FIREBASE DATABSE STARTS HERE =========================================

    //TO GET CANDIDATE DETAILS TABLE
    public void getDataCandidates() {
        final String table = "dkel_candidates";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataCandidates candidate = dataSnapshot.getValue(DataCandidates.class);
                values.put("candidate_id", candidate.candidate_id);
                values.put("candidate_name", candidate.candidate_name);
                values.put("candidate_domain", candidate.candidate_domain);
                values.put("candidate_party", candidate.candidate_party);
                values.put("candidate_image", candidate.candidate_image);
                insertData(table, values);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataCandidates candidate = dataSnapshot.getValue(DataCandidates.class);
                values.put("candidate_id", candidate.candidate_id);
                values.put("candidate_name", candidate.candidate_name);
                values.put("candidate_domain", candidate.candidate_domain);
                values.put("candidate_party", candidate.candidate_party);
                values.put("candidate_image", candidate.candidate_image);
                String condition = "candidate_id= " + candidate.candidate_id;
                updateData(table, values, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataCandidates candidate = dataSnapshot.getValue(DataCandidates.class);
                String condition = "candidate_id= " + candidate.candidate_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //TO GET DOMAIN DETAILS TABLE
    public void getDataDomains() {
        final String table = "dkel_domains";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataDomains domains = dataSnapshot.getValue(DataDomains.class);
                values.put("domain_id", domains.domain_id);
                values.put("domain_name", domains.domain_name);
                values.put("domain_status", domains.domain_status);
                insertData(table, values);
                pushCandidateList();
                pushDomainList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataDomains domains = dataSnapshot.getValue(DataDomains.class);
                values.put("domain_id", domains.domain_id);
                values.put("domain_name", domains.domain_name);
                values.put("domain_status", domains.domain_status);
                String condition = "domain_id= " + domains.domain_id;
                updateData(table, values, condition);
                pushCandidateList();
                pushDomainList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataDomains domains = dataSnapshot.getValue(DataDomains.class);
                String condition = "domain_id= " + domains.domain_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //TO GET PANEL DETAILS TABLE
    public void getDataPanels() {
        final String table = "dkel_panels";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                ContentValues values = new ContentValues();
                DataPanels panels = dataSnapshot.getValue(DataPanels.class);
                values.put("panel_id", panels.panel_id);
                values.put("panel_name", panels.panel_name);
                insertData(table, values);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataPanels panels = dataSnapshot.getValue(DataPanels.class);
                values.put("panel_id", panels.panel_id);
                values.put("panel_name", panels.panel_name);
                String condition = "panel_id= " + panels.panel_id;
                updateData(table, values, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataPanels panels = dataSnapshot.getValue(DataPanels.class);
                String condition = "panel_id= " + panels.panel_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //TO GET PARTIES DETAILS TABLE
    public void getDataParties() {
        final String table = "dkel_parties";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                ContentValues values = new ContentValues();
                DataParties parties = dataSnapshot.getValue(DataParties.class);
                values.put("party_id", parties.party_id);
                values.put("party_name", parties.party_name);
                values.put("party_image", parties.party_image);
                values.put("party_panel", parties.party_panel);
                insertData(table, values);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataParties parties = dataSnapshot.getValue(DataParties.class);
                values.put("party_id", parties.party_id);
                values.put("party_name", parties.party_name);
                values.put("party_image", parties.party_image);
                values.put("party_panel", parties.party_panel);
                String condition = "party_id= " + parties.party_id;
                updateData(table, values, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataParties parties = dataSnapshot.getValue(DataParties.class);
                String condition = "party_id= " + parties.party_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //TO GET VOTE DETAILS TABLE
    public void getDataVotes() {
        final String table = "dkel_votes";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                ContentValues values = new ContentValues();
                DataVotes votes = dataSnapshot.getValue(DataVotes.class);
                values.put("vote_id", votes.vote_candidate_id);
                values.put("vote_votes", votes.vote_candidate_vote);
                insertData(table, values);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataVotes votes = dataSnapshot.getValue(DataVotes.class);
                values.put("vote_id", votes.vote_candidate_id);
                values.put("vote_votes", votes.vote_candidate_vote);
                String condition = "vote_id= " + votes.vote_candidate_id;
                updateData(table, values, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataVotes votes = dataSnapshot.getValue(DataVotes.class);
                String condition = "vote_id= " + votes.vote_candidate_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //====================================== READ DATA FROM FIREBASE DATABSE ENDS HERE =========================================

    //To push to domain list view
    public void pushDomainList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> domain_names = new ArrayList<>();
        final ArrayList<HashMap<String, String>> domain_nonfav = new ArrayList<>();
        ArrayList<Integer> fav_list = getDomainFavList();
        domain_names.clear();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domains` ORDER BY `domain_name`", null);
        while (cur.moveToNext()) {
            if (fav_list.contains(cur.getInt(0))) {
                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("domain_id")));
                details.put("name", cur.getString(cur.getColumnIndex("domain_name")));
                domain_names.add(details);
            } else {
                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("domain_id")));
                details.put("name", cur.getString(cur.getColumnIndex("domain_name")));
                domain_nonfav.add(details);
            }
        }
        cur.close();
        domain_names.addAll(domain_nonfav);
        TabDomains tabDomains = new TabDomains();
        tabDomains.setListValues(domain_names);
    }

    //To push to candidate list view
    public void pushCandidateList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        ArrayList<HashMap<String, String>> candidate_nonfav = new ArrayList<>();
        ArrayList<Integer> fav_list = getCandidateFavList();
        candidate_names.clear();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id`", null);
        while (cur.moveToNext()) {
            if (fav_list.contains(cur.getInt(0))) {
                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("candidate_id")));
                details.put("name", cur.getString(cur.getColumnIndex("candidate_name")));
                details.put("domain", cur.getString(cur.getColumnIndex("domain_name")));
                details.put("party", cur.getString(cur.getColumnIndex("party_name")));
                details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                details.put("image", cur.getString(cur.getColumnIndex("candidate_image")));
                details.put("votes", cur.getString(cur.getColumnIndex("vote_votes")));
                candidate_names.add(details);
            } else {
                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("candidate_id")));
                details.put("name", cur.getString(cur.getColumnIndex("candidate_name")));
                details.put("domain", cur.getString(cur.getColumnIndex("domain_name")));
                details.put("party", cur.getString(cur.getColumnIndex("party_name")));
                details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                details.put("image", cur.getString(cur.getColumnIndex("candidate_image")));
                details.put("votes", cur.getString(cur.getColumnIndex("vote_votes")));
                candidate_nonfav.add(details);
            }
        }
        cur.close();
        candidate_names.addAll(candidate_nonfav);
        TabCandidates tabCandidates = new TabCandidates();
        tabCandidates.setListValues(candidate_names);
    }

    //To push to domain based candidate list view
    public void pushDomainWiseCandidateList() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (session.getSelectedDomain() != null) {
            int domain_id = session.getSelectedDomain();
            ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
            candidate_names.clear();
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `candidate_domain`=" + domain_id+" ORDER BY `vote_votes` DESC", null);
            while (cur.moveToNext()) {
                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("candidate_id")));
                details.put("name", cur.getString(cur.getColumnIndex("candidate_name")));
                details.put("domain", cur.getString(cur.getColumnIndex("domain_name")));
                details.put("party", cur.getString(cur.getColumnIndex("party_name")));
                details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                details.put("image", cur.getString(cur.getColumnIndex("candidate_image")));
                details.put("votes", cur.getString(cur.getColumnIndex("vote_votes")));
                candidate_names.add(details);

            }
            cur.close();
            TabDomainWiseResult tabDomainWiseResult=new TabDomainWiseResult();
            tabDomainWiseResult.setListValues(candidate_names);
        }
    }

    //To check favorite status - domains
    public ArrayList<Integer> getDomainFavList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> fav_domains = new ArrayList<Integer>();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domain_favourites`", null);
        while (cur.moveToNext()) {
            fav_domains.add(cur.getInt(1));
        }
        cur.close();
        return fav_domains;
    }

    //To check favorite status - candidates
    public ArrayList<Integer> getCandidateFavList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> fav_candidates = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidate_favourites`", null);
        while (cur.moveToNext()) {
            fav_candidates.add(cur.getInt(1));
        }
        cur.close();
        return fav_candidates;
    }

    //Adding domain to favorite
    public void addDomainToFavourite(int domain_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_domain_favourites` (`fav_domain_id`) VALUES(" + domain_id + ")");
    }

    //Adding candidate to favorite
    public void addCandidateToFavourite(Integer fav_candidate_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO `dkel_candidate_favourites` (`fav_candidate_id`) VALUES(" + fav_candidate_id + ")");
    }

    //Remove domain from favorite
    public void removeDomainFromFavourite(int domain_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_domain_favourites` WHERE `fav_domain_id`=" + domain_id);
    }

    //Remove candidate from favorite
    public void removeCandidateFromFavourite(Integer fav_candidate_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `dkel_candidate_favourites` WHERE `fav_candidate_id`=" + fav_candidate_id);
    }

    public void setSessionDomainId(int domainId) {
        session.setSelectedDomain(domainId);
    }
    public String getSelectedDomainName(){
        int domain_id=session.getSelectedDomain();
        String domain_name="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_domains` WHERE `domain_id`="+domain_id, null);
        if(cur.moveToNext()){
            domain_name=cur.getString(cur.getColumnIndex("domain_name"));
        }
        return domain_name;
    }
    public void setSessionCandidateId(int candidateId) {
        session.setSelectedCandidate(candidateId);
    }
    public String getSelectedCandidateName(){
        int candidate_id=session.getSelectedCandidate();
        String candidate_name="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates` WHERE `candidate_id`="+candidate_id, null);
        if(cur.moveToNext()){
            candidate_name=cur.getString(cur.getColumnIndex("candidate_name"));
        }
        return candidate_name;
    }
}
