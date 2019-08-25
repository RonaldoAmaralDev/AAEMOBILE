package com.delaroystudios.uploadmedia.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystudios.uploadmedia.R;
import com.delaroystudios.uploadmedia.activity.tutorial.IntroActivity;
import com.delaroystudios.uploadmedia.principal.Permissões;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 480)
        {
            setContentView(R.layout.activity_splashtablet);

        } else if (config.smallestScreenWidthDp == 720)
        {
            setContentView(R.layout.activity_splashtablet);
        }
        else {
            setContentView(R.layout.activity_splash);
        }


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), IntroActivity.class));
                finish();
            }
            /* 1 segundo = 1000 */
        }, 3000);
    }

}
