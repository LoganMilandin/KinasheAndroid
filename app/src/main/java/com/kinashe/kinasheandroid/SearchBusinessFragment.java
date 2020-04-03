package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import java.text.CollationElementIterator;
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
public class SearchBusinessFragment extends CustomFragment {

    public MainActivity context;

    private static final String TAG = "SearchFragment";
    private int navbarIndex;
    private boolean hasTyped;
    private View thisView;
    private EditText searchText;
    private RecyclerView businessDisplay;
    private BusinessListAdapter displayAdapter;

    private List<BusinessInfo> allBusinesses;
    //only the ones currently being displayed
    private List<BusinessInfo> searchedBusinesses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        setNavbarIndex(1);
        thisView = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = thisView.findViewById(R.id.search_text);
        setupInputListener();
        setupRefresher();
        return thisView;
    }

    public void setupScrollableContent(List<BusinessInfo> businesses) {
        Collections.shuffle(businesses);
        this.allBusinesses = businesses;
        this.searchedBusinesses = businesses;
        Log.d(TAG, "setting up homepage");
        businessDisplay = thisView.findViewById(R.id.business_display);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new BusinessListAdapter(searchedBusinesses, (MainActivity) getActivity(), this);
        businessDisplay.setAdapter(displayAdapter);
    }

    private void setupInputListener() {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String textEntered = s.toString().toLowerCase();
                if (count == 0) {
                    hasTyped = false;
                    searchedBusinesses = allBusinesses;
                    displayAdapter.changeList(searchedBusinesses);
                } else {
                    hasTyped = true;
                    searchedBusinesses = searchResults(textEntered);
                    displayAdapter.changeList(searchedBusinesses);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<BusinessInfo> searchResults(String prefix) {
        List<BusinessInfo> results = new ArrayList<>();
        for (BusinessInfo business: allBusinesses) {
            if (business.getCompanyName().toLowerCase().startsWith(prefix)) {
                //include it in search results
                results.add(business);
            }
        }
        return results;
    }

    private void setupRefresher() {
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
                        if (!hasTyped) {
                            allBusinesses = new ArrayList<>();
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
                                                            allBusinesses.add(business.getValue(BusinessInfo.class));
                                                        }
                                                    }
                                                }
                                            }
                                            if (context.location != null) {
                                                for (int i = 0; i < allBusinesses.size(); i++) {
                                                    Location targetLocation = new Location("");
                                                    targetLocation.setLatitude(Double.parseDouble(allBusinesses.get(i).getLat()));
                                                    targetLocation.setLongitude(Double.parseDouble(allBusinesses.get(i).getLon()));
                                                    //calculation and rounding done all at once
                                                    allBusinesses.get(i).setDistance((int) (context.location.distanceTo(targetLocation) / 10.0) / 100.0);
                                                }
                                            }
                                            Collections.shuffle(allBusinesses);
                                            displayAdapter.changeList(allBusinesses);
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
                        } else {
                            refresher.setRefreshing((false));
                        }
                    }
                }, 1000);
            }
        });
    }


}
