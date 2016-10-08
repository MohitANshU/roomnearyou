package com.adcoretechnologies.rny;

import android.content.Intent;
import android.os.Bundle;

import com.adcoretechnologies.rny.core.base.BaseActivity;
import com.adcoretechnologies.rny.intro.IntroActivity;
import com.adcoretechnologies.rny.util.Const;
import com.adcoretechnologies.rny.util.Pref;


/**
 * Created by Irfan on 23/05/16.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, IntroActivity.class);

        boolean isIntroDone = Pref.ReadBoolean(getApplicationContext(), Const.PREF_IS_INTRO_DONE, false);
        if (isIntroDone) {
            intent = new Intent(this, LauncherActivity.class);
        }

//        String authToken= Pref.Read(getApplicationContext(), Const.KEY_AUTH_TOKEN);
//
//        if(authToken.isEmpty())
//            intent=new Intent(this,LoginActivity.class);
//        else intent=new Intent(this,MainActivity.class);

        final Intent finalIntent = intent;
        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3000 * 1);
                    startActivity(finalIntent);
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
