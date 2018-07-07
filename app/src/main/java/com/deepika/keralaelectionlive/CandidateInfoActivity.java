package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CandidateInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DbHelper dbHelper;
    public static TextView candi_name,candi_domain,candi_party,v_status,candi_total_vote,vote_diff,candi_panel,candi_pos;
    public static ImageView candidate_image,party_image;
    public static  LinearLayout candi_border;
    public static ListView listView;
public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context=getApplicationContext();
        dbHelper = new DbHelper(CandidateInfoActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        listView=(ListView)findViewById(R.id.list_results);
        candidate_image=(ImageView)findViewById(R.id.candidate_image);
        party_image=(ImageView)findViewById(R.id.party_image);

        candi_panel=(TextView)findViewById(R.id.candidate_panel);
        candi_name=(TextView) findViewById(R.id.card_candidate_name);
        candi_domain=(TextView)findViewById(R.id.candidate_domain);
        candi_party=(TextView)findViewById(R.id.candidate_party);
        v_status=(TextView)findViewById(R.id.vote_status);
        vote_diff=(TextView)findViewById(R.id.candidate_vote_diff);
        candi_total_vote=(TextView)findViewById(R.id.candidate_total_votes);
        candi_pos=(TextView)findViewById(R.id.candidate_position);

        candi_border=(LinearLayout)findViewById(R.id.candi_border);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        dbHelper.pushandidateDetails();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.candidate_id);
                dbHelper.setSessionCandidateId(Integer.parseInt(tv.getText().toString()));
                dbHelper.pushandidateDetails();
            }
        });
    }
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (!(back_pressed + 2000 > System.currentTimeMillis())) {
            super.onBackPressed();
        }
        else{
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        back_pressed = System.currentTimeMillis();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            String data = dbHelper.getShareText();
            final Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(i);
        } else if (id == R.id.action_about) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(CandidateInfoActivity.this);
            LayoutInflater inflater = (CandidateInfoActivity.this).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.about, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            ImageButton close = (ImageButton) dialogView.findViewById(R.id.close);
            final AlertDialog alertDialog = builder.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leading) {
            Intent intent=new Intent(CandidateInfoActivity.this,LeadingListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_won) {
            Intent intent=new Intent(CandidateInfoActivity.this,WonListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent=new Intent(CandidateInfoActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",1);
            startActivity(intent);
        } else if (id == R.id.nav_domains) {
            Intent intent=new Intent(CandidateInfoActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",0);
            startActivity(intent);
        } else if (id == R.id.nav_candidates) {
            Intent intent=new Intent(CandidateInfoActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",2);
            startActivity(intent);
        }else if (id == R.id.nav_share) {
            String data = dbHelper.getShareText();
            final Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(CandidateInfoActivity.this);
            LayoutInflater inflater = (CandidateInfoActivity.this).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.about, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            ImageButton close = (ImageButton) dialogView.findViewById(R.id.close);
            final AlertDialog alertDialog = builder.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setCandidateValues(ArrayList<HashMap<String, String>> selected_candidate,ArrayList<HashMap<String, String>> other_candidate){
        if(context!=null) {
            for(HashMap h:selected_candidate){
                int max_vote=Integer.parseInt(h.get("max_vote").toString());
                int second_vote=Integer.parseInt(h.get("second_vote").toString());
                String panel=h.get("panel").toString();
                int vote=Integer.parseInt(h.get("votes").toString());
                short domain_status=Short.parseShort(h.get("domain_status").toString());
                int candi_id=Integer.parseInt(h.get("id").toString());
                candi_name.setText(h.get("name").toString());
                candi_domain.setText(h.get("domain").toString());
                candi_party.setText(h.get("party").toString());
                candi_pos.setText(h.get("rank").toString());
                candi_total_vote.setText(String.valueOf(vote));
                if(panel.equals("UDF")){
                    candi_panel.setText("U\nD\nF");
                    candi_border.setBackgroundResource(R.drawable.bg_list_green);
                    candi_panel.setBackgroundResource(R.drawable.bg_rect_green);
                    candi_name.setTextColor(Color.rgb(15,130,63));
                    candi_party.setTextColor(Color.rgb(15,130,63));
                }else if(panel.equals("LDF")){
                    candi_panel.setText("L\nD\nF");
                    candi_border.setBackgroundResource(R.drawable.bg_list_red);
                    candi_panel.setBackgroundResource(R.drawable.bg_rect_red);
                    candi_name.setTextColor(Color.rgb(214,39,40));
                    candi_party.setTextColor(Color.rgb(214,39,40));
                }else if(panel.equals("NDA")){
                    candi_panel.setText("N\nD\nA");
                    candi_border.setBackgroundResource(R.drawable.bg_list_orange);
                    candi_panel.setBackgroundResource(R.drawable.bg_rect_orange);
                    candi_name.setTextColor(Color.rgb(243,112,34));
                    candi_party.setTextColor(Color.rgb(243,112,34));
                }else {
                    candi_panel.setText("O\nT\nH");
                    candi_border.setBackgroundResource(R.drawable.bg_list_blue);
                    candi_panel.setBackgroundResource(R.drawable.bg_rect_blue);
                    candi_name.setTextColor(Color.rgb(31,119,180));
                    candi_party.setTextColor(Color.rgb(31,119,180));
                }
                if(domain_status==1){
                    if(vote==max_vote){
                        v_status.setText("Leading By Votes");
                        v_status.setTextColor(Color.rgb(15,130,63));
                        int diff_vote=vote-second_vote;
                        vote_diff.setTextColor(Color.rgb(15,130,63));
                        if(diff_vote==0){
                            vote_diff.setText("N/A (Tie)");
                            candi_pos.setText("1");
                            candi_pos.setTextColor(Color.rgb(15,130,63));
                        }else{
                            vote_diff.setText(String.valueOf(diff_vote));
                            candi_pos.setTextColor(Color.rgb(15,130,63));
                        }
                    }else{
                        v_status.setText("Low By Votes");
                        v_status.setTextColor(Color.RED);
                        int diff_vote=max_vote-vote;
                        vote_diff.setText(String.valueOf(diff_vote));
                        vote_diff.setTextColor(Color.RED);
                        candi_pos.setTextColor(Color.RED);
                    }
                }else{
                    if(vote==max_vote){
                        v_status.setText("Won By Votes");
                        v_status.setTextColor(Color.rgb(15,130,63));
                        int diff_vote=vote-second_vote;
                        vote_diff.setTextColor(Color.rgb(15,130,63));
                        if(diff_vote==0){
                            vote_diff.setText("N/A (Tie)");
                            candi_pos.setText("1");
                            candi_pos.setTextColor(Color.rgb(15,130,63));
                        }else{
                            vote_diff.setText(String.valueOf(diff_vote));
                            candi_pos.setTextColor(Color.rgb(15,130,63));
                        }
                    }else{
                        v_status.setText("Lost By Votes");
                        v_status.setTextColor(Color.RED);
                        int diff_vote=max_vote-vote;
                        vote_diff.setText(String.valueOf(diff_vote));
                        vote_diff.setTextColor(Color.RED);
                        candi_pos.setTextColor(Color.RED);
                    }
                }
                Picasso.with(context).load(h.get("image").toString())
                        .transform(new RoundedTransformation(30, 0))
                        .into(candidate_image);
                Picasso.with(context).load(h.get("party_image").toString())
                        .transform(new RoundedTransformation(360, 0))
                        .into(party_image);

                listView.setAdapter(new CandidateInfoCustomAdapter(context, other_candidate,candi_id));
            }

        }
    }
}
