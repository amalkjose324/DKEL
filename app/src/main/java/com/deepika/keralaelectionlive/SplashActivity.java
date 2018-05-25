package com.deepika.keralaelectionlive;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    DbHelper dbHelper=new DbHelper(this);
    ImageView mContentView;
    private SparseIntArray mErrorString;
    private static final int REQUEST_PERMISSIONS = 20;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mErrorString = new SparseIntArray();
        mContentView = (ImageView) findViewById(R.id.fullscreen_content);
        requestAppPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt,
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Toast.makeText(this,"All permissions are required",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                ActivityCompat.requestPermissions(SplashActivity.this, requestedPermissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }
    public void onPermissionsGranted(int requestCode){
        String language =dbHelper.getLanguageSelected();
        if(language != null){
        }
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            LayoutInflater inflater = (SplashActivity.this).getLayoutInflater();
            final View dialogView=inflater.inflate(R.layout.language_selector, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            Button close=(Button)dialogView.findViewById(R.id.close);
            final RadioGroup radioGroup=(RadioGroup)dialogView.findViewById(R.id.language_radio_group);

            final AlertDialog alertDialog = builder.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) dialogView.findViewById(selectedId);
                    dbHelper.setLanguageSelected(radioButton.getText().toString().equals("English") ? "eng" : "mal");
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getData();                //
            }
        }, 3000);

        final Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim);
        mContentView.startAnimation(myFadeInAnimation); //Set animation to your ImageView
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Animation a = new AlphaAnimation(1.00f, 0.00f);
                a.setDuration(2000);
                a.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                        mContentView.setVisibility(View.GONE);
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                        mContentView.setVisibility(View.VISIBLE);
                    }

                    public void onAnimationEnd(Animation animation) {
                        //mContentView.setVisibility(View.VISIBLE);
                    }
                });
                mContentView.startAnimation(a);
            }
        }, 1500);
    }
    public void getData(){
        //dbHelper.getDomainNames();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        this.startActivity(intent);
        this.finish();

    }
}
