package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.BusinessListAdapter;
import com.kinashe.kinasheandroid.Utils.CustomFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * handles the home screen of the app. Businesses are displayed in order
 * of monthly payment or, to break ties, their distance from the user
 */
public class HomeFragment extends CustomFragment {

    public MainActivity context;

    private static final String TAG = "HomeFragment";
    private int navbarIndex = 0;
    private View thisView;

    //handlers for scrolling view
    private RecyclerView businessDisplay;
    private RecyclerView.Adapter displayAdapter;
    private RecyclerView.LayoutManager displayManager;
    private List<BusinessInfo> businesses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_home, container, false);
        context = (MainActivity) getActivity();
        return thisView;
    }

    /**
     * actually initialized the scrolling view of businesses, only to be called
     * after data is loaded from Firebase
     * @param businesses sorted list of businesses retrieved from Firebase
     */
    public void setupScrollableContent(List<BusinessInfo> businesses) {
        Log.d(TAG, "setting up homepage");
        this.businesses = businesses;
        for (BusinessInfo b: businesses) {
            Log.d(TAG, b.getCompanyName());
        }
        businessDisplay = thisView.findViewById(R.id.business_display);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new BusinessListAdapter(businesses, (MainActivity) getActivity(), this);
        businessDisplay.setAdapter(displayAdapter);
        setupRefresher();
    }

    /**
     * honestly this repeats a ton of code from the other pages with a refresh button but
     * the behavior is slightly different so I decided to just leave it
     */
    private void setupRefresher() {
        Log.d(TAG, "setting refresher");
        final SwipeRefreshLayout refresher = thisView.findViewById(R.id.refresher);
        refresher.setProgressBackgroundColorSchemeResource(R.color.searchBlue);
        refresher.setColorSchemeResources(R.color.couponBrown);
        refresher.setSize(SwipeRefreshLayout.LARGE);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresher.setRefreshing(true);
                Log.d(TAG, "refreshing");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        businesses = new ArrayList<>();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot data) {
                                        for (DataSnapshot businessType : data.getChildren()) {
                                            if (!businessType.getKey().equals("Advertisements")) {
                                                for (DataSnapshot business : businessType.getChildren()) {
                                                    BusinessInfo businessObj = business.getValue(BusinessInfo.class);
                                                    if (businessObj.isVerified()) {
                                                        Log.d(TAG, business.getValue(BusinessInfo.class).getCompanyName());
                                                        businesses.add(business.getValue(BusinessInfo.class));
                                                    }
                                                }
                                            }
                                        }
                                        if (context.location != null) {
                                            for (int i = 0; i < businesses.size(); i++) {
                                                Location targetLocation = new Location("");
                                                targetLocation.setLatitude(Double.parseDouble(businesses.get(i).getLat()));
                                                targetLocation.setLongitude(Double.parseDouble(businesses.get(i).getLon()));
                                                //calculation and rounding done all at once
                                                businesses.get(i).setDistance((int) (context.location.distanceTo(targetLocation) / 10.0) / 100.0);
                                            }
                                        }
                                        Collections.sort(businesses, new Comparator<BusinessInfo>() {
                                            @Override
                                            public int compare(BusinessInfo first, BusinessInfo second) {
                                                if (first.getMonthlyPayment() > second.getMonthlyPayment()) {
                                                    return -1;
                                                } else if (second.getMonthlyPayment() > first.getMonthlyPayment()) {
                                                    return 1;
                                                } else if (context.location != null){
                                                    return (int) (first.getDistance() - second.getDistance());
                                                } else {
                                                    return 0;
                                                }
                                            }
                                        });
                                        Log.d(TAG, "changing list");
                                        ((BusinessListAdapter) displayAdapter).changeList(businesses);
                                        refresher.setRefreshing(false);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message
                                        Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                                        refresher.setRefreshing(false);
                                        // ...
                                    }
                                }
                        );
                    }
                }, 1000);
            }
        });
    }

}
