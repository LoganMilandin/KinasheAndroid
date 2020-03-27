package com.kinashe.kinasheandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.ActivityThatMakesCalls;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;
import com.kinashe.kinasheandroid.Utils.HomepageListAdapter;
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
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        ActivityThatMakesCalls {
    private static final String TAG = "MainActivity";

    //for navbar menu
    private static final int ACTIVITY_NUM = 0;

    //for location permissions
    public static final int LOCATION_REQUEST_CODE = 1;
    //for phone permissions
    public static final int CALL_REQUEST = 2;

    //for code reuse, notice all activities have identical
    //setupBottomBar methods
    private Context mContext = MainActivity.this;

    //for handling call requests
    private Intent callIntent;

    //handlers for scrolling view
    private RecyclerView businessDisplay;
    private RecyclerView.Adapter displayAdapter;
    private RecyclerView.LayoutManager displayManager;

    //handles getting user location
    private FusedLocationProviderClient locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomBar();
        setupScrollView();
    }

    private void setupScrollView() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //populate homepage with distances
            Log.d(TAG, "Already have permission");
            populateHomepageWithLocation();

        } else {
            PermissionUtils.requestPermission(MainActivity.this, LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottombar);
        BottomBarHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomBarHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void populateHomepage(final Location location) {
        Log.d(TAG, "populating homepage");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {
                        List<BusinessInfo> businesses = new ArrayList<>();
                        for (DataSnapshot businessType : data.getChildren()) {
                            if (!businessType.getKey().equals("Advertisements")) {
                                for (DataSnapshot business : businessType.getChildren()) {
                                    businesses.add(business.getValue(BusinessInfo.class));
                                }
                            }
                        }
                        businessDisplay = findViewById(R.id.businessDisplay);
                        displayManager = new LinearLayoutManager(MainActivity.this);
                        businessDisplay.setLayoutManager(displayManager);
                        displayAdapter = new HomepageListAdapter(businesses, MainActivity.this, location);
                        businessDisplay.setAdapter(displayAdapter);
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

    private void populateHomepageWithLocation() {

        locationProvider = LocationServices.getFusedLocationProviderClient(MainActivity.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "answer received: " + requestCode);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //populate homepage with distances
                populateHomepageWithLocation();
                Log.d(TAG, "location permission granted");
            } else {
                Log.d(TAG, "location permission not granted");
                populateHomepage(null);
            }
        } else if (requestCode == CALL_REQUEST) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.CALL_PHONE)) {
                //make the call
                PermissionUtils.makePhoneCall(MainActivity.this, this.callIntent, CALL_REQUEST);
                Log.d(TAG, "list adapter listener triggered");
            } else {
                Toast.makeText(MainActivity.this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setCallIntent(Intent intent) {
        this.callIntent = intent;
    }
}