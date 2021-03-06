package com.deepika.keralaelectionlive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deepika.keralaelectionlive.Adapters.OnlineStatusAdapter;
import com.deepika.keralaelectionlive.DatabaseResources.DbHelper;
import com.deepika.keralaelectionlive.TabActivities.TabDomainWiseAnalysis;
import com.deepika.keralaelectionlive.TabActivities.TabDomainWiseResult;

import java.util.List;

public class DomainWiseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DbHelper dbHelper;
    public static Context context;
    public static TextView online_text;
    OnlineStatusAdapter onlineStatusAdapter = new OnlineStatusAdapter();
    private DomainWiseActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_wise);
        context = getApplicationContext();
        online_text = findViewById(R.id.online_status);
        onlineStatusAdapter.Start();
        dbHelper=new DbHelper(DomainWiseActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv= findViewById(R.id.domain_name);
        tv.setText(dbHelper.getSelectedDomainName());
        mSectionsPagerAdapter = new DomainWiseActivity.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    Boolean isPause=false;
    @Override
    protected void onPause() {
        super.onPause();
        onlineStatusAdapter.Stop();
        isPause=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPause) {
            onlineStatusAdapter.Start();
            isPause=false;
        }
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
                            onlineStatusAdapter.Stop();
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(DomainWiseActivity.this);
            LayoutInflater inflater = (DomainWiseActivity.this).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.about, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            ImageButton close = dialogView.findViewById(R.id.close);
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
            Intent intent=new Intent(DomainWiseActivity.this,LeadingListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_won) {
            Intent intent=new Intent(DomainWiseActivity.this,WonListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            Intent intent=new Intent(DomainWiseActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",1);
            startActivity(intent);
        } else if (id == R.id.nav_domains) {
            Intent intent=new Intent(DomainWiseActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",0);
            startActivity(intent);
        } else if (id == R.id.nav_candidates) {
            Intent intent=new Intent(DomainWiseActivity.this,MainActivity.class);
            intent.putExtra("FRAGMENT_ID",2);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            String data = dbHelper.getShareText();
            final Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(DomainWiseActivity.this);
            LayoutInflater inflater = (DomainWiseActivity.this).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.about, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            ImageButton close = dialogView.findViewById(R.id.close);
            final AlertDialog alertDialog = builder.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabDomainWiseResult tabDomainWiseResult = new TabDomainWiseResult();
                    return tabDomainWiseResult;
                case 1:
                    TabDomainWiseAnalysis tabDomainWiseAnalysis = new TabDomainWiseAnalysis();
                    return tabDomainWiseAnalysis;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = DomainWiseActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.getUserVisibleHint())
                return fragment;
        }
        return null;
    }
    public void setOnlineStatus(Boolean status) {
        if (context != null) {
            if (status) {
                online_text.setText("Online");
                online_text.setBackgroundResource(R.drawable.onlinebg);
            } else {
                online_text.setText("Offline");
                online_text.setBackgroundResource(R.drawable.offlinebg);
            }
        }
    }
}
