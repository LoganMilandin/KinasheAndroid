package com.kinashe.kinasheandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

/**
 * handles the splash screen displayed when the app is initially
 * loaded
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }
}
