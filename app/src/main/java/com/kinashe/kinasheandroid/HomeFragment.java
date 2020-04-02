package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.BusinessListAdapter;

import java.util.List;

/**
 * handles the flame activity on the app, also happens to be the
 * home/launch screen but besides that it does the same thing as the other
 * activities. For simplicity, the others aren't commented because it's the same
 * idea as this
 */
public class HomeFragment extends CustomFragment {

    public MainActivity context;

    private static final String TAG = "HomeFragment";
    private View thisView;

    //handlers for scrolling view
    private RecyclerView businessDisplay;
    private RecyclerView.Adapter displayAdapter;
    private RecyclerView.LayoutManager displayManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_home, container, false);
        context = (MainActivity) getActivity();
        return thisView;
    }

    public void setupScrollableContent(List<BusinessInfo> businesses) {
        Log.d(TAG, "setting up homepage");
        businessDisplay = thisView.findViewById(R.id.business_display);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new BusinessListAdapter(businesses, (MainActivity) getActivity(), this);
        businessDisplay.setAdapter(displayAdapter);
    }

}
