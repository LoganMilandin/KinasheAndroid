package com.kinashe.kinasheandroid.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.AddBusinessActivity;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.PlacesActivity;
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.TransportationActivity;

/**
 * contains helpers for the bottom navigation bar. Each tab on the bar
 * contains a separate activity.
 */
public class BottomBarHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        //to stop the icons from moving around
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        //to center the icons in the bar
        bottomNavigationViewEx.setIconsMarginTop(0);

    }

    //creates listener for next menu item tap
    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.main:
                        context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.places:
                        context.startActivity(new Intent(context, PlacesActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.transportation:
                        context.startActivity(new Intent(context, TransportationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;

                    case R.id.addbusiness:
                        context.startActivity(new Intent(context, AddBusinessActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                }


                return false;
            }
        });
    }
}
