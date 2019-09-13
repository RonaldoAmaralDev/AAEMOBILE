package br.com.araujoabreu.timg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
               // showSettingAlert();

            }
            /* 1 segundo = 1000 */
        }, 3000);
    }

    //public void showSettingAlert()
   // {
     //   AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
      //  alertDialog.setTitle("GPS setting!");
      //  alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
     //   alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
      //      @Override
      //      public void onClick(DialogInterface dialog, int which) {
       //         Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
       //         startActivity(intent);
       //     }
       // });
       // alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
       //     @Override
         //   public void onClick(DialogInterface dialog, int which) {
        //        dialog.cancel();
        //    }
       // });
      //  alertDialog.show();
  //  }

}
