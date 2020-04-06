package com.kinashe.kinasheandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.CustomFragment;
import com.kinashe.kinasheandroid.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * handles the page that allows the user to select Nearby or All
 * for a particular business type
 */
public class NearbyAllFragment extends CustomFragment {

    private static final String TAG = "AddBusinessPage";

    private MainActivity context;
    private int navbarIndex;
    public static final int MAX_DISTANCE_FOR_NEARBY = 20;
    public List<BusinessInfo> theseBusinesses;
    private View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_nearby_all, container, false);
        context = (MainActivity) getActivity();
        setupView();
        return myView;
    }



    /**
     * sets image display and click listeners
     */
    private void setupView() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "location permissions allowed");
            setupHelper(context.location);
        } else {
            Log.d(TAG, "requesting location permissions");
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.LOCATION_REQUEST_CODE);
        }
    }

    public void getLocation() {
        context.locationProvider.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        context.location = location;
                        setupHelper(location);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupHelper(final Location location) {
        String category = getArguments().getString("title");
        theseBusinesses = new ArrayList<>();
        for (BusinessInfo business: context.businesses) {
            if (business.getBusinessType().equalsIgnoreCase(category.substring(0, category.indexOf("|") - 1).
                    replace("/", "-"))) {
                theseBusinesses.add(business);
            }
        }
        TextView title = myView.findViewById(R.id.title);
        ImageView image = myView.findViewById(R.id.nearby_all_image);
        title.setText(category);
        image.setImageResource(getArguments().getInt("image"));
        ImageView backButton = myView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View back) {
                context.navigationManager.handleBackClicked(NearbyAllFragment.this);
            }
        });
        CardView nearbyButton = myView.findViewById(R.id.nearby_button);
        CardView allButton = myView.findViewById(R.id.all_button);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                CustomFragment newFragment = new NearbyAllListFragment();
                ((NearbyAllListFragment)newFragment).setNearby(false);
                newFragment.setArguments(getArguments());
                newFragment.setParent(NearbyAllFragment.this);
                NearbyAllFragment.this.setChild(newFragment);
                ((NearbyAllListFragment)newFragment).setupScrollableContent(theseBusinesses);
                context.navigationManager.handleNewFragmentCreated(newFragment);
            }
        });
        if (location != null) {
            nearbyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View button) {
                    CustomFragment newFragment = new NearbyAllListFragment();
                    ((NearbyAllListFragment)newFragment).setNearby(true);
                    newFragment.setArguments(getArguments());
                    newFragment.setParent(NearbyAllFragment.this);
                    NearbyAllFragment.this.setChild(newFragment);
                    PermissionUtils.setDistances(theseBusinesses, location);
                    List<BusinessInfo> nearbyBusinesses = new ArrayList<>();
                    for (BusinessInfo business: theseBusinesses) {
                        if (business.getDistance() < MAX_DISTANCE_FOR_NEARBY) {
                            nearbyBusinesses.add(business);
                        }
                    }
                    ((NearbyAllListFragment)newFragment).setupScrollableContent(nearbyBusinesses);
                    context.navigationManager.handleNewFragmentCreated(newFragment);
                }
            });
        } else {
            ((CardView)nearbyButton).setCardBackgroundColor(getResources().getColor(R.color.couponBrownTransparent));
            ((CardView)nearbyButton).setElevation(0);
            ((CardView)nearbyButton).setClickable(false);
        }
    }
}
