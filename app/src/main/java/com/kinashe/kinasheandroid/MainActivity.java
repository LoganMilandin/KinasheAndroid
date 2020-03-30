package com.kinashe.kinasheandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.NavigationManager;
import com.kinashe.kinasheandroid.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * handles the flame activity on the app, also happens to be the
 * home/launch screen but besides that it does the same thing as the other
 * activities. For simplicity, the others aren't commented because it's the same
 * idea as this
 */
public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";

    //for location permissions
    public static final int LOCATION_REQUEST_CODE = 1;
    //for phone permissions
    public static final int CALL_REQUEST_CODE = 2;

    //for handling call requests
    private Intent callIntent;

    //handles getting user location
    private FusedLocationProviderClient locationProvider;

    //for firebase data
    private List<BusinessInfo> businesses;

    //for navigation
    public NavigationManager navigationManager;

    //fragments for home view
    public Fragment homeFragment;
    public Fragment singleBusinessFragmentHome;

    //fragments for places view
    public Fragment placesFragment;
    public Fragment nearbyAllPlacesFragment;

    //fragments for transportation view
    public Fragment transportationFragment;
    public Fragment nearbyAllTransportationFragment;

    public Fragment addBusinessFragment;
    public Fragment activeFragment;
    public FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
        navigationManager = new NavigationManager(MainActivity.this);
        //make the homepage and fill with firebase data
        homeFragment = new HomeFragment();
        getFirebaseData();
        //make places fragment with title in a bundle
        placesFragment = new PlacesOrTransportationFragment();
        Bundle placeBundle = new Bundle();
        placeBundle.putString("title", "Places | ቦታዎች");
        placesFragment.setArguments(placeBundle);
        //make transportation fragment with title in a bundle
        transportationFragment = new PlacesOrTransportationFragment();
        Bundle transportationBundle = new Bundle();
        transportationBundle.putString("title", "Transportation | መጓጓዣ");
        transportationFragment.setArguments(transportationBundle);
        addBusinessFragment = new AddBusinessFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.topbar_and_content, placesFragment).hide(placesFragment).commit();
        manager.beginTransaction().add(R.id.topbar_and_content, transportationFragment).hide(transportationFragment).commit();
        manager.beginTransaction().add(R.id.topbar_and_content, addBusinessFragment).hide(addBusinessFragment).commit();
        manager.beginTransaction().add(R.id.topbar_and_content, homeFragment).commit();
        activeFragment = homeFragment;
        //make sure everything else is loaded before setting up navbar
        setupBottomBar();
    }

    private void setupHomepage() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "location permissions allowed");
            populateHomepageWithLocation();
        } else {
            Log.d(TAG, "requesting location permissions");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    public void populateHomepage(Location location) {
        Log.d(TAG, "business list size: " + businesses.size());
        HomeFragment home = (HomeFragment) homeFragment;
        home.setupScrollableContent(businesses, location);
    }

    private void populateHomepageWithLocation() {
        locationProvider.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d(TAG, "got location: " + (location == null));
                        if (location != null) {
                            Log.d(TAG, "location not null");
                            populateHomepage(location);
                        } else {
                            populateHomepage(null);
                        }
                    }
                });
    }

    private void getFirebaseData() {
        Log.d(TAG, "populating homepage");
        businesses = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {
                        businesses = new ArrayList<>();
                        for (DataSnapshot businessType : data.getChildren()) {
                            if (!businessType.getKey().equals("Advertisements")) {
                                for (DataSnapshot business : businessType.getChildren()) {
                                    BusinessInfo businessObj = business.getValue(BusinessInfo.class);
                                    if (businessObj.isVerified()) {
                                        businesses.add(business.getValue(BusinessInfo.class));
                                    }
                                    Log.d(TAG, "got data");
                                }
                            }
                        }
                        setupHomepage();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "answer received: " + requestCode);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //populate homepage with distances
                Log.d(TAG, "location permission granted");
                populateHomepageWithLocation();
            } else {
                Log.d(TAG, "location permission not granted");
                populateHomepage(null);
            }
        } else if (requestCode == CALL_REQUEST_CODE) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.CALL_PHONE)) {
                //make the call
                Log.d(TAG, "making call");
                startActivity(this.callIntent);
            } else {
                Toast.makeText(MainActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCallIntent(Intent intent) {
        this.callIntent = intent;
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        View container = findViewById(R.id.bottombar_container);
        BottomNavigationViewEx bottomNavigationViewEx = container.findViewById(R.id.bottombar);
        disableBottomNavigationAnimations(bottomNavigationViewEx);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(bottombarListener);

    }

    public static void disableBottomNavigationAnimations(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        //to stop the icons from moving around
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        //to center the icons in the bar
        bottomNavigationViewEx.setIconsMarginTop(0);

    }

    private BottomNavigationViewEx.OnNavigationItemSelectedListener bottombarListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return navigationManager.handleBottombarItemSelected(item.getItemId());
                }
            };
}