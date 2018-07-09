package com.deepika.keralaelectionlive;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;

public class OnlineStatusService extends JobService {
    BackgroundTask backgroundTask;
    static Handler handler = new Handler();
    static Context context;

    MainActivity mainActivity=new MainActivity();

    @Override
    public boolean onStartJob(final JobParameters job) {
        context = getApplicationContext();
        backgroundTask = new BackgroundTask();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(context, "stop request", Toast.LENGTH_SHORT).show();
        return false;
    }

    public class BackgroundTask {
        public BackgroundTask() {
            handler = new Handler();
            final int delay=5000;
            Runnable myRunnable = new Runnable() {
                public void run() {
                    try
                    {
                        if (isOnline()) {
                            mainActivity.setOnlineStatus(true);
                        } else {
                            mainActivity.setOnlineStatus(false);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    handler.postDelayed(this,delay);
                }
            };
            handler.postDelayed(myRunnable,delay);
        }
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
