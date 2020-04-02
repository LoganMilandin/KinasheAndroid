package com.kinashe.kinasheandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.BusinessListAdapter;

import java.util.List;

/**
 * handles the flame activity on the app, also happens to be the
 * home/launch screen but besides that it does the same thing as the other
 * activities. For simplicity, the others aren't commented because it's the same
 * idea as this
 */
public class NearbyAllListFragment extends CustomFragment {

    public MainActivity context;

    private static final String TAG = "NearbyAllListFragment";
    private View thisView;

    //handlers for scrolling view
    private RecyclerView businessDisplay;
    private BusinessListAdapter displayAdapter;

    private List<BusinessInfo> theseBusinesses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        //this screen is close enough to the places or transportation layout that we can reuse it
        thisView = inflater.inflate(R.layout.fragment_places_or_transportation, container, false);
        setupView();
        return thisView;
    }

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
        businessDisplay = thisView.findViewById(R.id.type_grid);
        businessDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        displayAdapter = new BusinessListAdapter(theseBusinesses, (MainActivity) getActivity(), this);
        businessDisplay.setAdapter(displayAdapter);
    }

    public void setupScrollableContent(List<BusinessInfo> businesses) {
        this.theseBusinesses = businesses;
    }

}
