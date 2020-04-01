package com.kinashe.kinasheandroid;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.HomepageListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * handles the flame activity on the app, also happens to be the
 * home/launch screen but besides that it does the same thing as the other
 * activities. For simplicity, the others aren't commented because it's the same
 * idea as this
 */
public class SearchBusinessFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private View thisView;
    private EditText searchText;

    //handlers for scrolling view
    private RecyclerView businessDisplay;
    private HomepageListAdapter displayAdapter;

    private List<BusinessInfo> allBusinesses;
    private List<BusinessInfo> searchedBusinesses;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = thisView.findViewById(R.id.search_text);
        setupInputListener();
        return thisView;
    }

    public void setupScrollableContent(List<BusinessInfo> businesses, Location location) {
        this.allBusinesses = businesses;
        this.searchedBusinesses = new ArrayList<>();
        Log.d(TAG, "setting up homepage");
        businessDisplay = thisView.findViewById(R.id.business_display);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new HomepageListAdapter(searchedBusinesses, (MainActivity) getActivity(), this);
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
                    //make searchedBusinesses empty
                    searchedBusinesses = new ArrayList<>();
                    displayAdapter.changeList(searchedBusinesses);
                } else {
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

}
