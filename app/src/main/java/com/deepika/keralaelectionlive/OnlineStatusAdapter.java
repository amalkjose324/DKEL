package com.deepika.keralaelectionlive;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class OnlineStatusAdapter {
    Handler handler;
    private MainActivity mainActivity;
    private CandidateInfoActivity candidateInfoActivity;
    private DomainWiseActivity domainWiseActivity;
    private LeadingListActivity leadingListActivity;
    private WonListActivity wonListActivity;
    private Runnable myRunnable;

    public Boolean isOnline(){
        return checkOnline();
    }

    public void Start() {
        mainActivity=new MainActivity();
        candidateInfoActivity=new CandidateInfoActivity();
        domainWiseActivity=new DomainWiseActivity();
        leadingListActivity=new LeadingListActivity();
        wonListActivity=new WonListActivity();

        handler = new Handler();
        final int delay = 10000;
        myRunnable = new Runnable() {
            public void run() {
                try {
                    if (checkOnline()) {
                        pushOnlineStatus(true);
                    } else {
                        pushOnlineStatus(false);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(myRunnable, delay);
    }

    public void Stop(){
        handler.removeCallbacks(myRunnable);
    }


    public boolean checkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void pushOnlineStatus(Boolean status){
        Log.d("Status:",status.toString());
        mainActivity.setOnlineStatus(status);
        candidateInfoActivity.setOnlineStatus(status);
        domainWiseActivity.setOnlineStatus(status);
        leadingListActivity.setOnlineStatus(status);
        wonListActivity.setOnlineStatus(status);
    }
}
