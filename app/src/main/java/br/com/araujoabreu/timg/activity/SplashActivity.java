package br.com.araujoabreu.timg.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import br.com.araujoabreu.timg.R;
import br.com.araujoabreu.timg.activity.tutorial.IntroActivity;

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
