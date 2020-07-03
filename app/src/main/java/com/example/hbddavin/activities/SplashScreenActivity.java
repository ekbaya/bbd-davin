package com.example.hbddavin.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.hbddavin.R;
import com.example.hbddavin.services.SoundService;
import com.example.hbddavin.slider.MainSliderActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start service and play music
        startService(new Intent(SplashScreenActivity.this, SoundService.class));
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT >= 19){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent = new Intent(MainActivity.this, PermissionsActivity.class);
                Intent intent = new Intent(SplashScreenActivity.this, MainSliderActivity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        //start service and play music
        startService(new Intent(SplashScreenActivity.this, SoundService.class));
        super.onResume();
    }
}