package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.kinashe.kinasheandroid.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * child to the nearby all fragment, displaying businesses in a
 * list similar to homepage but filtered by category and choice of
 * nearby or all
 */
public class NearbyAllListFragment extends CustomFragment {

    private static final String TAG = "NearbyAllListFragment";
    public MainActivity context;
    private View thisView;
    private RecyclerView businessDisplay;
    private BusinessListAdapter displayAdapter;
    private List<BusinessInfo> theseBusinesses;
    //true iff the client selected nearby to get to this page
    private boolean isNearby;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        //this screen is close enough to the places or transportation layout that we can reuse it
        thisView = inflater.inflate(R.layout.fragment_nearby_all_list, container, false);
        setupView();
        return thisView;
    }

    /**
     * sets up initial scrollable content in list
     */
    public void setupView() {
        TextView title = thisView.findViewById(R.id.title);
        title.setText(getArguments().getString("title"));
        ImageView backButton = thisView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View back) {
                Log.d(TAG, "clicked back from NA list");
                context.navigationManager.handleBackClicked(NearbyAllListFragment.this);
            }
        });
        //this will be businesses, not types of course
        businessDisplay = thisView.findViewById(R.id.business_display);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new BusinessListAdapter(theseBusinesses, (MainActivity) getActivity(), this);
        businessDisplay.setAdapter(displayAdapter);
        setupRefresher();
    }

    public void setupScrollableContent(List<BusinessInfo> businesses) {
        theseBusinesses = businesses;
    }

    /**
     * On this page, refreshing again retrieves all data but only stores businesses
     * if they correspond to the type for this page. Also filters content depending
     * if this is a nearby list or an all list
     */
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
                        theseBusinesses = new ArrayList<>();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot data) {
                                        String type = NearbyAllListFragment.this.getArguments().getString("title");
                                        for (DataSnapshot businessType : data.getChildren()) {
                                            if (!businessType.getKey().equals("Advertisements")) {
                                                for (DataSnapshot business : businessType.getChildren()) {
                                                    BusinessInfo businessObj = business.getValue(BusinessInfo.class);
                                                    if (businessObj.getBusinessType().equalsIgnoreCase
                                                            (type.substring(0, type.indexOf("|") - 1).replace("/", "-"))) {
                                                        theseBusinesses.add(businessObj);
                                                    }
                                                }
                                            }
                                        }
                                        if (context.location != null) {
                                            PermissionUtils.setDistances(theseBusinesses, context.location);
                                            for (int i = 0; i < theseBusinesses.size(); i++) {
                                                if (NearbyAllListFragment.this.isNearby &&
                                                        theseBusinesses.get(i).getDistance() > NearbyAllFragment.MAX_DISTANCE_FOR_NEARBY) {
                                                    theseBusinesses.remove(i--);
                                                }
                                            }
                                        }
                                        Collections.sort(theseBusinesses, new Comparator<BusinessInfo>() {
                                            @Override
                                            public int compare(BusinessInfo first, BusinessInfo second) {
                                                if (context.location != null){
                                                    return (int) (first.getDistance() - second.getDistance());
                                                } else {
                                                    return 0;
                                                }
                                            }
                                        });
                                        ((BusinessListAdapter) displayAdapter).changeList(theseBusinesses);
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

    public void setNearby(boolean isNearby) {
        this.isNearby = isNearby;
    }

}
