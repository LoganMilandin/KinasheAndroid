package com.kinashe.kinasheandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Firebase.NotificationAdHelper;
import com.kinashe.kinasheandroid.Utils.CustomFragment;
import com.kinashe.kinasheandroid.Utils.NavigationManager;
import com.kinashe.kinasheandroid.Utils.PermissionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * this is where all the magic happens. This activity runs the entire time the
 * app is open and manages a group of fragments corresponding to the different
 * screens the user can click to. Handles location services and clickables
 * like calling and openin navigation
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
    public Intent callIntent;

    //handles getting user location
    public FusedLocationProviderClient locationProvider;

    //stores firebase data in an ordered format
    public List<BusinessInfo> businesses;

    //handles all app navigation, including back navigation,
    //navbar clicks, and manual back navigation with back
    //button on phone
    public NavigationManager navigationManager;

    //fragments for home tab
    public CustomFragment homeFragment;//should be instance of HomeFragment
    public CustomFragment singleBusinessFragmentHome;//should be instance of SingleBusinessFragment

    //fragments for search tab
    public CustomFragment searchFragment;//should be instance of SearchFragment
    public CustomFragment singleBusinessFragmentSearch;//should be instance of SingleBusinessFragment

    //fragments for places tab
    public CustomFragment placesFragment;//should be instance of PlacesOrTransportationFragment
    public CustomFragment nearbyAllPlacesFragment;//should be instance of NearbyAllFragment
    public CustomFragment nearbyAllPlacesListFragment;//should be instance of NearbyAllListFragment

    //fragments for transportation tab
    public CustomFragment transportationFragment;//should be instance of PlacesOrTransportationFragment
    public CustomFragment nearbyAllTransportationFragment;//should be instance of NearbyAllFragment
    public CustomFragment nearbyAllTransportationListFragment;//should be instance of NearbyAllListFragment

    //fragment for add business tab
    public CustomFragment addBusinessFragment;//should be instance of AddBusiness fragment
    public CustomFragment activeFragment;

    //manages all fragments, usually via the NavigationManager instance
    public FragmentManager manager;

    private BottomNavigationViewEx bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Internet? " + hasInternet());
        if (hasInternet()) {
            //NotificationAdHelper.getAppId();
            locationProvider = LocationServices.getFusedLocationProviderClient(this);
            navigationManager = new NavigationManager(MainActivity.this);
            initializeFragments();
            setupBottomBar();
        } else {
            Toast.makeText(this, "You must have internet connection to use Kinashe. The app will now close.",
                    Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.finish();
                }
            }, 2000);
        }
    }

    public boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
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
        placesFragment.setNavbarIndex(2);
        //make transportation fragment with title in a bundle
        transportationFragment = new PlacesOrTransportationFragment();
        Bundle transportationBundle = new Bundle();
        transportationBundle.putString("title", "Transportation | መጓጓዣ");
        transportationFragment.setArguments(transportationBundle);
        transportationFragment.setNavbarIndex(3);
        addBusinessFragment = new AddBusinessFragment();
        //this is part of the workaround for lag when you first click the places tab
        //ultimately we want to hide all of these fragments except the homepage, but
        //if we hide them here they don't actually initialize their views before
        //getting hidden. So, we do the actual hiding after the firebase data is retrieved.
        manager.beginTransaction().
                add(R.id.topbar_and_content, searchFragment, "0").
                add(R.id.topbar_and_content, placesFragment, "1").
                add(R.id.topbar_and_content, transportationFragment, "2").
                add(R.id.topbar_and_content, addBusinessFragment, "3").
                add(R.id.topbar_and_content, homeFragment, "4").
                show(homeFragment).
                commit();
        activeFragment = homeFragment;
    }

    //requests permission and populates homepage depending on the result
    private void setupHomepageInitial() {
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

    //sorts the businesses and then renders them in a list on homepage
    public void populateHomepage() {
        Log.d(TAG, "populating homepage");
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
        ((HomeFragment) homeFragment).setupScrollableContent(businesses);
        //also pass input list to search screen while we're here
        ((SearchBusinessFragment) searchFragment).setupScrollableContent(new ArrayList<>(businesses));
    }

    //actually gets location and then populates homepage
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
    //retrieves data from firebase and renders it to the homepage,
    //also passing it to search fragment
    private void getFirebaseData() {
        Log.d(TAG, "populating homepage");
        businesses = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        DatabaseReference child = database.child("test");
        child.setValue("hello world").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "successful write!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
            }
        });



        database.addListenerForSingleValueEvent(
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
                        setupHomepageInitial();
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

    //callback when we get the result of permission requests
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

    //sets an intent to open a new call activity so it can be called from other
    public void setCallIntent(Intent intent) {
        this.callIntent = intent;
    }


    /**
     * BottomNavigationView setup
     */
    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        View container = findViewById(R.id.bottombar_container);
        bottomBar = container.findViewById(R.id.bottombar);
        disableBottomNavigationAnimations(bottomBar);
        bottomBar.setOnNavigationItemSelectedListener(bottombarListener);

    }
    //turns off default animations for bottom bar
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
                    Log.d(TAG, "home child null?" + (homeFragment.getChild() == null));
                    navigationManager.handleBottombarItemSelected(item);
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        navigationManager.handleBackClickedManual();
    }

    public BottomNavigationViewEx getBottomBar() {
        return bottomBar;
    }
}