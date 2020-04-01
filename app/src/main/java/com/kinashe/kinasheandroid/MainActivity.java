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
import java.util.Collections;
import java.util.Comparator;
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

    public Location location;

    //for handling call requests
    private Intent callIntent;

    //handles getting user location
    private FusedLocationProviderClient locationProvider;

    //for firebase data
    public List<BusinessInfo> businesses;

    //for navigation
    public NavigationManager navigationManager;

    //fragments for home view
    public HomeFragment homeFragment;
    public SingleBusinessFragment singleBusinessFragmentHome;

    //fragments for search view
    public SearchBusinessFragment searchFragment;
    public SingleBusinessFragment singleBusinessFragmentSearch;

    //fragments for places view
    public PlacesOrTransportationFragment placesFragment;
    public NearbyAllFragment nearbyAllPlacesFragment;
    public NearbyAllListFragment nearbyAllPlacesListFragment;

    //fragments for transportation view
    public PlacesOrTransportationFragment transportationFragment;
    public NearbyAllFragment nearbyAllTransportationFragment;
    public NearbyAllListFragment nearbyAllTransportationListFragment;

    public AddBusinessFragment addBusinessFragment;
    public Fragment activeFragment;
    public FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
        navigationManager = new NavigationManager(MainActivity.this);
        //make the homepage and fill with firebase data
        initializeFragments();
        //make sure everything else is loaded before setting up navbar
        setupBottomBar();
    }

    private void initializeFragments() {
        manager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        getFirebaseData();
        searchFragment = new SearchBusinessFragment();
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
        //this is part of the workaround for lag when you first click the places tab
        //ultimately we want to hide all of these fragments except the homepage, but
        //if we hide them here they don't actually initialize their views before
        //getting hidden. So, we do the actual hiding after the firebase data is retrieved.
        manager.beginTransaction().
                add(R.id.topbar_and_content, searchFragment).
                add(R.id.topbar_and_content, placesFragment).
                add(R.id.topbar_and_content, transportationFragment).
                add(R.id.topbar_and_content, addBusinessFragment).
                add(R.id.topbar_and_content, homeFragment).
                commit();
        activeFragment = homeFragment;
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

    public void populateHomepage() {
        if (location != null) {
            for (int i = 0; i < businesses.size(); i++) {
                Location targetLocation = new Location("");
                targetLocation.setLatitude(Double.parseDouble(businesses.get(i).getLat()));
                targetLocation.setLongitude(Double.parseDouble(businesses.get(i).getLon()));
                //calculation and rounding done all at once
                businesses.get(i).setDistance((int) (location.distanceTo(targetLocation) / 10.0) / 100.0);
            }
        }
        Collections.sort(businesses, new Comparator<BusinessInfo>() {
            @Override
            public int compare(BusinessInfo first, BusinessInfo second) {
                if (first.getMonthlyPayment() > second.getMonthlyPayment()) {
                    return -1;
                } else if (second.getMonthlyPayment() > first.getMonthlyPayment()) {
                    return 1;
                } else if (location != null){
                    return (int) (first.getDistance() - second.getDistance());
                } else {
                    return 0;
                }
            }
        });
        //also pass input list to search screen while we're here
        ((SearchBusinessFragment) searchFragment).setupScrollableContent(businesses, location);
        ((HomeFragment) homeFragment).setupScrollableContent(businesses, location);
    }

    private void populateHomepageWithLocation() {
        locationProvider.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        MainActivity.this.location = location;
                        populateHomepage();
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
                        for (DataSnapshot businessType : data.getChildren()) {
                            if (!businessType.getKey().equals("Advertisements")) {
                                for (DataSnapshot business : businessType.getChildren()) {
                                    BusinessInfo businessObj = business.getValue(BusinessInfo.class);
                                    if (businessObj.isVerified()) {
                                        businesses.add(business.getValue(BusinessInfo.class));
                                    }
                                }
                            }
                        }
                        //the other fragment views should have loaded in the background while
                        //data was fetched, so hide them now
                        manager.
                                beginTransaction().
                                hide(searchFragment).
                                hide(placesFragment).
                                hide(transportationFragment).
                                hide(addBusinessFragment).
                                commit();
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
                populateHomepage();
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