package com.example.hbddavin.slider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hbddavin.R;
import com.example.hbddavin.activities.MainActivity;
import com.example.hbddavin.services.SoundService;
import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainSliderActivity extends AppCompatActivity
        implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private Button proceed_button;
    private Button button;
    private SliderPagerAdapter adapter;
    boolean proceed = false;
    int trigger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // making activity full screen
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main_slider);

        viewPager = findViewById(R.id.pagerIntroSlider);
        TabLayout tabLayout = findViewById(R.id.tabs);
        proceed_button = findViewById(R.id.proceed_button);
        button = findViewById(R.id.button);
        // init slider pager adapter
        adapter = new SliderPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // set adapter
        viewPager.setAdapter(adapter);
        // set dot indicators
        tabLayout.setupWithViewPager(viewPager);
        // make status bar transparent
        viewPager.setOnPageChangeListener(this);
        proceed_button.setOnClickListener(this);
        button.setOnClickListener(this);
        changeStatusBarColor();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(button)){
            if (viewPager.getCurrentItem() < adapter.getCount()) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

                if(viewPager.getCurrentItem() == 2){
                    trigger ++;
                    if(trigger > 1){
                        Intent i = new Intent(MainSliderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                }
            }
        }
        if (view.equals(proceed_button)){
            Intent intent = new Intent(MainSliderActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            button.setText(R.string.get_started);
            proceed = true;
        } else {
            button.setText(R.string.next);
            proceed = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainSliderActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < adapter.getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
    @Override
    protected void onResume() {
        //start service and play music
        startService(new Intent(MainSliderActivity.this, SoundService.class));
        super.onResume();
    }
}