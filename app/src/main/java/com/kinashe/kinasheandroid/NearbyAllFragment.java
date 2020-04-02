package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.NavigationManager;

import java.util.ArrayList;
import java.util.List;

public class NearbyAllFragment extends CustomFragment {

    private MainActivity context;

    private static final String TAG = "AddBusinessPage";

    private static final int MAX_DISTANCE_FOR_NEARBY = 20;

    public List<BusinessInfo> theseBusinesses;

    private View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_nearby_all, container, false);
        context = (MainActivity) getActivity();
        setupView();
        return myView;
    }

    private void setupView() {
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
                newFragment.setArguments(getArguments());
                newFragment.setParent(NearbyAllFragment.this);
                NearbyAllFragment.this.setChild(newFragment);
                ((NearbyAllListFragment)newFragment).setupScrollableContent(theseBusinesses);
                context.navigationManager.handleNewFragmentCreated(newFragment);
            }
        });
        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                CustomFragment newFragment = new NearbyAllListFragment();
                newFragment.setArguments(getArguments());
                newFragment.setParent(NearbyAllFragment.this);
                NearbyAllFragment.this.setChild(newFragment);
                if (context.location == null) {
                    ((NearbyAllListFragment)newFragment).setupScrollableContent(theseBusinesses);
                } else {
                    List<BusinessInfo> nearbyBusinesses = new ArrayList<>();
                    for (BusinessInfo business: theseBusinesses) {
                        if (business.getDistance() < MAX_DISTANCE_FOR_NEARBY) {
                            nearbyBusinesses.add(business);
                        }
                    }
                    ((NearbyAllListFragment)newFragment).setupScrollableContent(nearbyBusinesses);
                }
                context.navigationManager.handleNewFragmentCreated(newFragment);
            }
        });
    }
}
