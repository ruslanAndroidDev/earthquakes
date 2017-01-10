package com.example.pk.test2012.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pk.test2012.R;

/**
 * Created by pk on 09.01.2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    int logoTimer = 0;
                    while (logoTimer < 2000) {
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}
