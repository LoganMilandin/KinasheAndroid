package com.kinashe.kinasheandroid.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kinashe.kinasheandroid.MainActivity;

import java.util.Random;

/**
 * handles Firebase push notifications (though not necessary if sending a
 * message to all clients) as well as ad initialization and loading from
 * AdMob
 */
public class NotificationAdHelper {

    private static final String TAG = "NotificationHelper";

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";

    //this would be if we wanted to test a push notification on a single device
    public static void getAppId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                    }
                });
    }

    /**
     * initializes the ad with the correct unit ID from admob. Currently using a test ad
     * @param context should be the instance of MainActivity
     */
    public static void initializeAd(final MainActivity context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                context.ad = new InterstitialAd(context);
                context.ad.setAdUnitId(AD_UNIT_ID);
            }
        });
    }

    /**
     * displays the ad, but only one in three times this method is called
     * due to randomization (easily changed)
     * @param context should be the instance of MainActivity
     */
    public static void showAd(final MainActivity context) {
        if (new Random().nextInt(3) == 0) {
            try {
                context.ad.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        Log.d(TAG, "loaded ad");
                        context.ad.show();
                    }
                });
                Log.d(TAG, "loading ad");
                context.ad.loadAd(new AdRequest.Builder().build());
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }
}
