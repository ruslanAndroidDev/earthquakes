package com.example.pk.test2012.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pk.test2012.R;

import java.util.ArrayList;

import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

/**
 * Created by pk on 09.01.2017.
 */

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sPref;
    public final String FIRST_LAUNCH_KEY = "firstLaunch";

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
                    sPref = getPreferences(MODE_PRIVATE);
                    boolean firstLaunch = sPref.getBoolean(FIRST_LAUNCH_KEY, true);
                    if (firstLaunch == true) {
                        loadTutorial();
                    } else {
                        startMainActivity();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        logoTimer.start();
    }

    private void startMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems());
        startActivityForResult(mainAct, 2385);

    }

    private ArrayList<TutorialItem> getTutorialItems() {
        TutorialItem tutorialItem1 = new TutorialItem((" EarthQuakes"), ("В цьому додатку ви можете дізнатися місце,час і потужність землетрусів які сталися цього місяця"),
                R.color.magnitude3, R.mipmap.earthqukes);

        TutorialItem tutorialItem2 = new TutorialItem(("Район землетрусу на карті:"), ("Для перегляду району в якому стався землетрус клікніть на картку і утримуйте декілька секунд."),
                R.color.magnitude4, R.drawable.img_intro);
        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(FIRST_LAUNCH_KEY, false);
        ed.commit();
        startMainActivity();
    }
}
