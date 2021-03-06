package com.deepika.keralaelectionlive.DatabaseResources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deepika.keralaelectionlive.CandidateInfoActivity;
import com.deepika.keralaelectionlive.TabActivities.TabCandidates;
import com.deepika.keralaelectionlive.TabActivities.TabDomainWiseAnalysis;
import com.deepika.keralaelectionlive.TabActivities.TabDomainWiseResult;
import com.deepika.keralaelectionlive.TabActivities.TabDomains;
import com.deepika.keralaelectionlive.TabActivities.TabHome;
import com.deepika.keralaelectionlive.TabActivities.TabLeadingCandidatesAll;
import com.deepika.keralaelectionlive.TabActivities.TabLeadingCandidatesLDF;
import com.deepika.keralaelectionlive.TabActivities.TabLeadingCandidatesNDA;
import com.deepika.keralaelectionlive.TabActivities.TabLeadingCandidatesOTH;
import com.deepika.keralaelectionlive.TabActivities.TabLeadingCandidatesUDF;
import com.deepika.keralaelectionlive.TabActivities.TabWinnerCandidatesAll;
import com.deepika.keralaelectionlive.TabActivities.TabWinnerCandidatesLDF;
import com.deepika.keralaelectionlive.TabActivities.TabWinnerCandidatesNDA;
import com.deepika.keralaelectionlive.TabActivities.TabWinnerCandidatesOTH;
import com.deepika.keralaelectionlive.TabActivities.TabWinnerCandidatesUDF;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS dkel_config(config_id INTEGER PRIMARY KEY AUTOINCREMENT, config_key VARCHAR UNIQUE, config_value VARCHAR);");
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushDomainWiseAnalysis();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataCandidates candidate = dataSnapshot.getValue(DataCandidates.class);
                String condition = "candidate_id= " + candidate.candidate_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataDomains domains = dataSnapshot.getValue(DataDomains.class);
                String condition = "domain_id= " + domains.domain_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainList();
                pushDomainWiseCandidateList();
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushDomainWiseAnalysis();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
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
                pushandidateDetails();
                pushDomainWiseCandidateList();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataPanels panels = dataSnapshot.getValue(DataPanels.class);
                String condition = "panel_id= " + panels.panel_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushDomainWiseCandidateList();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushDomainWiseCandidateList();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushDomainWiseAnalysis();
                pushWinnerCandidateOTHList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataParties parties = dataSnapshot.getValue(DataParties.class);
                String condition = "party_id= " + parties.party_id;
                deleteData(table, condition);
                pushCandidateList();
                pushDomainWiseCandidateList();
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushDomainWiseCandidateList();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
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
                pushandidateDetails();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushWinnerCandidateAllList();
                pushWinnerCandidateUDFList();
                pushWinnerCandidateLDFList();
                pushWinnerCandidateNDAList();
                pushWinnerCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataVotes votes = dataSnapshot.getValue(DataVotes.class);
                String condition = "vote_id= " + votes.vote_candidate_id;
                deleteData(table, condition);
                pushCandidateList();
                pushandidateDetails();
                pushDomainWiseCandidateList();
                pushLeadingCandidateAllList();
                pushLeadingCandidateUDFList();
                pushLeadingCandidateLDFList();
                pushLeadingCandidateNDAList();
                pushLeadingCandidateOTHList();
                pushVoteSummery();
                pushDomainWiseAnalysis();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //TO GET VOTE DETAILS TABLE
    public void getDataConfig() {
        final String table = "dkel_config";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(table);
        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataConfig config=dataSnapshot.getValue(DataConfig.class);
                values.put("config_key", config.config_key);
                values.put("config_value", config.config_value);
                insertData(table, values);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                ContentValues values = new ContentValues();
                DataConfig config=dataSnapshot.getValue(DataConfig.class);
                values.put("config_key", config.config_key);
                values.put("config_value", config.config_value);
                String condition = "config_key= '" + config.config_key+"'";
                updateData(table, values, condition);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                DataConfig config=dataSnapshot.getValue(DataConfig.class);
                String condition = "config_key= '" + config.config_key+"'";
                deleteData(table, condition);
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
    //To push to candidate details view
    public void pushandidateDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (session.getSelectedCandidate() != null) {
            int candidate_id = session.getSelectedCandidate();
            int domain_id=0;
            int rank=0;
            int max_vote=0,second_vote=0;
            int max_cand_id=0;
            Cursor cur;
            ArrayList<HashMap<String, String>> selected_candidate = new ArrayList<>();
            ArrayList<HashMap<String, String>> other_candidates = new ArrayList<>();
            selected_candidate.clear();
            cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `candidate_id`=" + candidate_id, null);
            while (cur.moveToNext()) {
                domain_id=cur.getInt(cur.getColumnIndex("candidate_domain"));
            }
            cur.close();
            cur = db.rawQuery("SELECT MAX(`vote_votes`)AS `vote_votes`,`candidate_id` FROM `dkel_candidates`,`dkel_votes` WHERE `candidate_id`=`vote_id` AND `candidate_domain`= "+domain_id, null);
            while (cur.moveToNext()) {
                max_vote=cur.getInt(cur.getColumnIndex("vote_votes"));
                max_cand_id=cur.getInt(cur.getColumnIndex("candidate_id"));
            }
            cur.close();
            cur = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes` FROM `dkel_candidates`,`dkel_votes` WHERE `candidate_id`=`vote_id` AND `candidate_domain`= "+domain_id+" AND `candidate_id`<>"+max_cand_id, null);
            while (cur.moveToNext()) {
                second_vote=cur.getInt(cur.getColumnIndex("vote_votes"));
            }
            cur.close();
            cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `candidate_domain`= "+domain_id+" ORDER BY `vote_votes` DESC", null);
            while (cur.moveToNext()) {
                rank++;
                if(candidate_id==cur.getInt(cur.getColumnIndex("candidate_id"))){
                    HashMap<String, String> details = new HashMap<>();
                    details.put("id", cur.getString(cur.getColumnIndex("candidate_id")));
                    details.put("rank", String.valueOf(rank));
                    details.put("second_vote", String.valueOf(second_vote));
                    details.put("max_vote", String.valueOf(max_vote));
                    details.put("name", cur.getString(cur.getColumnIndex("candidate_name")));
                    details.put("domain", cur.getString(cur.getColumnIndex("domain_name")));
                    details.put("party", cur.getString(cur.getColumnIndex("party_name")));
                    details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                    details.put("image", cur.getString(cur.getColumnIndex("candidate_image")));
                    details.put("votes", cur.getString(cur.getColumnIndex("vote_votes")));
                    details.put("party_image", cur.getString(cur.getColumnIndex("party_image")));
                    details.put("domain_status", cur.getString(cur.getColumnIndex("domain_status")));

                    selected_candidate.add(details);
                }

                HashMap<String, String> details = new HashMap<>();
                details.put("id", cur.getString(cur.getColumnIndex("candidate_id")));
                details.put("rank", String.valueOf(rank));
                details.put("max_vote", String.valueOf(max_vote));
                details.put("second_vote", String.valueOf(second_vote));
                details.put("name", cur.getString(cur.getColumnIndex("candidate_name")));
                details.put("domain", cur.getString(cur.getColumnIndex("domain_name")));
                details.put("party", cur.getString(cur.getColumnIndex("party_name")));
                details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                details.put("image", cur.getString(cur.getColumnIndex("candidate_image")));
                details.put("votes", cur.getString(cur.getColumnIndex("vote_votes")));
                details.put("domain_status", cur.getString(cur.getColumnIndex("domain_status")));
                other_candidates.add(details);

            }
            cur.close();
            CandidateInfoActivity candidateInfoActivity=new CandidateInfoActivity();
            candidateInfoActivity.setCandidateValues(selected_candidate,other_candidates);
        }
    }

    //To push to all leading candidate list view
    public void pushLeadingCandidateAllList() {
        SQLiteDatabase db = this.getReadableDatabase();
            ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
            candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
            TabLeadingCandidatesAll tabLeadingCandidatesAll=new TabLeadingCandidatesAll();
            tabLeadingCandidatesAll.setListValues(candidate_names);
    }

    //To push to udf leading candidate list view
    public void pushLeadingCandidateUDFList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `party_panel`=1 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabLeadingCandidatesUDF tabLeadingCandidatesUDF=new TabLeadingCandidatesUDF();
        tabLeadingCandidatesUDF.setListValues(candidate_names);
    }
    //To push to ldf leading candidate list view
    public void pushLeadingCandidateLDFList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `party_panel`=2 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabLeadingCandidatesLDF tabLeadingCandidatesLDF=new TabLeadingCandidatesLDF();
        tabLeadingCandidatesLDF.setListValues(candidate_names);
    }

    //To push to NDA leading candidate list view
    public void pushLeadingCandidateNDAList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `party_panel`=3 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabLeadingCandidatesNDA tabLeadingCandidatesNDA=new TabLeadingCandidatesNDA();
        tabLeadingCandidatesNDA.setListValues(candidate_names);
    }

    //To push to OTH leading candidate list view
    public void pushLeadingCandidateOTHList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `party_panel`=4 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabLeadingCandidatesOTH tabLeadingCandidatesOTH=new TabLeadingCandidatesOTH();
        tabLeadingCandidatesOTH.setListValues(candidate_names);
    }


    //To push to all Winner candidate list view
    public void pushWinnerCandidateAllList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `domain_status`=0 GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `domain_status`=0 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabWinnerCandidatesAll tabWinnerCandidatesAll=new TabWinnerCandidatesAll();
        tabWinnerCandidatesAll.setListValues(candidate_names);
    }

    //To push to udf Winner candidate list view
    public void pushWinnerCandidateUDFList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `domain_status`=0 GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `panel_id`=1 AND `domain_status`=0 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabWinnerCandidatesUDF tabWinnerCandidatesUDF=new TabWinnerCandidatesUDF();
        tabWinnerCandidatesUDF.setListValues(candidate_names);
    }
    //To push to ldf Winner candidate list view
    public void pushWinnerCandidateLDFList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `domain_status`=0 GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `panel_id`=2 AND `domain_status`=0 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabWinnerCandidatesLDF tabWinnerCandidatesLDF=new TabWinnerCandidatesLDF();
        tabWinnerCandidatesLDF.setListValues(candidate_names);
    }

    //To push to NDA Winner candidate list view
    public void pushWinnerCandidateNDAList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `domain_status`=0 GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `panel_id`=3 AND `domain_status`=0 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabWinnerCandidatesNDA tabWinnerCandidatesNDA=new TabWinnerCandidatesNDA();
        tabWinnerCandidatesNDA.setListValues(candidate_names);
    }

    //To push to OTH Winner candidate list view
    public void pushWinnerCandidateOTHList() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> candidate_names = new ArrayList<>();
        candidate_names.clear();
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `domain_status`=0 GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid=cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes=cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `panel_id`=4 AND `domain_status`=0 AND `domain_id`="+domainid+" AND `vote_votes`="+votes,null);
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
        }
        cursor.close();
        TabWinnerCandidatesOTH tabWinnerCandidatesOTH=new TabWinnerCandidatesOTH();
        tabWinnerCandidatesOTH.setListValues(candidate_names);
    }

    //To push to analysis summery
    public void pushDomainWiseAnalysis() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (session.getSelectedDomain() != null) {
            int domain_id = session.getSelectedDomain();
            ArrayList<HashMap<String, String>> vote_details = new ArrayList<>();
            vote_details.clear();
            Cursor cur = db.rawQuery("SELECT `panel_name`,SUM(vote_votes) as `sum_votes` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `candidate_domain`=" + domain_id+" GROUP BY `panel_id`", null);
            while (cur.moveToNext()) {
                HashMap<String, String> details = new HashMap<>();
                details.put("panel", cur.getString(cur.getColumnIndex("panel_name")));
                details.put("votes", cur.getString(cur.getColumnIndex("sum_votes")));
                vote_details.add(details);
            }
            cur.close();
            TabDomainWiseAnalysis tabDomainWiseAnalysis=new TabDomainWiseAnalysis();
            tabDomainWiseAnalysis.setListValues(vote_details);
        }
    }

    //To push Vote summery to home tab
    public void pushVoteSummery() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Integer> details = new HashMap<>();
        details.put("Lead_UDF",0);
        details.put("Lead_LDF",0);
        details.put("Lead_NDA",0);
        details.put("Lead_OTH",0);
        details.put("Won_UDF",0);
        details.put("Won_LDF",0);
        details.put("Won_NDA",0);
        details.put("Won_OTH",0);
        details.put("Total_Leads",0);
        details.put("Total_Wons",0);
        Cursor cursor = db.rawQuery("SELECT MAX(`vote_votes`) AS `vote_votes`,`domain_id` FROM `dkel_candidates`,`dkel_domains`,`dkel_votes` WHERE `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` GROUP BY `domain_id` ORDER BY `domain_name`;",null);
        while (cursor.moveToNext()) {
            int domainid = cursor.getInt(cursor.getColumnIndex("domain_id"));
            int votes = cursor.getInt(cursor.getColumnIndex("vote_votes"));
            Cursor cur = db.rawQuery("SELECT * FROM `dkel_candidates`,`dkel_domains`,`dkel_votes`,`dkel_parties`,`dkel_panels` WHERE `candidate_party`=`party_id` AND `candidate_domain`=`domain_id` AND `candidate_id`=`vote_id` AND `party_panel`=`panel_id` AND `domain_id`=" + domainid + " AND `vote_votes`=" + votes, null);
            while (cur.moveToNext()) {
                details.put("Total_Leads", details.get("Total_Leads") + 1);
                if (cur.getInt(cur.getColumnIndex("domain_status")) == 0) {
                    details.put("Total_Wons", details.get("Total_Wons") + 1);
                    if (cur.getInt(cur.getColumnIndex("party_panel")) == 1) {
                        details.put("Won_UDF", details.get("Won_UDF") + 1);
                    } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 2) {
                        details.put("Won_LDF", details.get("Won_LDF") + 1);
                    } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 3) {
                        details.put("Won_NDA", details.get("Won_NDA") + 1);
                    } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 4) {
                        details.put("Won_OTH", details.get("Won_OTH") + 1);
                    }
                }
                if (cur.getInt(cur.getColumnIndex("party_panel")) == 1) {
                    details.put("Lead_UDF", details.get("Lead_UDF") + 1);
                } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 2) {
                    details.put("Lead_LDF", details.get("Lead_LDF") + 1);
                } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 3) {
                    details.put("Lead_NDA", details.get("Lead_NDA") + 1);
                } else if (cur.getInt(cur.getColumnIndex("party_panel")) == 4) {
                    details.put("Lead_OTH", details.get("Lead_OTH") + 1);
                }
            }
            cur.close();
        }
        cursor.close();
        TabHome tabHome=new TabHome();
        tabHome.setVoteSummery(details);
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
        cur.close();
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
        cur.close();
        return candidate_name;
    }
    public String getShareText(){
        String text="Hey, I'm using Kerala Election Live App by Deepika to get realtime election results. Download it : https://play.google.com/store/apps/details?id=com.deepika.keralaelectionlive";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM `dkel_config` WHERE `config_key`='config_share_text'", null);
        if(cur.moveToNext()){
            text=cur.getString(cur.getColumnIndex("config_value"));
        }
        cur.close();
        return text;
    }
}
