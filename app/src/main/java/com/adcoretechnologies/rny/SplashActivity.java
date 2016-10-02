package com.adcoretechnologies.rny;

import android.content.Intent;
import android.os.Bundle;

import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.intro.IntroActivity;


/**
 * Created by Irfan on 23/05/16.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent intent = new Intent(this, IntroActivity.class);
//
//        String authToken= Pref.Read(getApplicationContext(), Const.KEY_AUTH_TOKEN);
//
//        if(authToken.isEmpty())
//            intent=new Intent(this,LoginActivity.class);
//        else intent=new Intent(this,MainActivity.class);

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3000 * 1);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {

                }
            }
        };
        background.start();
    }

    @Override
    public void init() {

    }

    @Override
    public void log(String message) {

    }
}
