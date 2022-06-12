package com.aktechzone.propertydeal.Authentications;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.SharedPrefManager;
import com.aktechzone.propertydeal.activities.DrawerActivity;
import com.aktechzone.propertydeal.activities.DrawerAgentActivity;

public class Splash extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_splash_screen);
        fullScreen();
        int seconddelay = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(getApplicationContext()).getUserType().equals("admin")) {
                    if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                        Log.wtf("splash", "true");
                        startActivity(new Intent(Splash.this, DrawerActivity.class));
                        finish();
                    } else {
                        Log.wtf("splash", "false");
                        startActivity(new Intent(Splash.this, SignIn.class));
                        finish();
                    }
                } else if (SharedPrefManager.getInstance(getApplicationContext()).getUserType().equals("agent")) {
                    if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                        startActivity(new Intent(Splash.this, DrawerAgentActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this, SignIn.class));
                        finish();
                    }
                } else {
                    if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                        startActivity(new Intent(Splash.this, DrawerActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(Splash.this, SignIn.class));
                        finish();
                    }
                }
            }
        }, seconddelay);
    }

    public void fullScreen() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
        } else {
        }
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }
}