package com.kinashe.kinasheandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class NearbyAllFragment extends Fragment {

    private static final String TAG = "AddBusinessPage";
    //for navbar
    private static final int ACTIVITY_NUM = 3;

    private View myView;

    private MainActivity context;


    public NearbyAllFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_nearby_all, container, false);
        context = (MainActivity) getActivity();
        //setup view
        setupView();
        return myView;
    }

    private void setupView() {
        TextView title = myView.findViewById(R.id.title);
        ImageView image = myView.findViewById(R.id.nearby_all_image);
        title.setText(getArguments().getString("title"));
        image.setImageResource(getArguments().getInt("image"));
        ImageView backButton = myView.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View back) {
                if (getArguments().getString("screen").equals("Places | ቦታዎች")) {
                    context.navigationManager.handleBackButtonClickedFromNearbyAllPlaces();
                } else {
                    context.navigationManager.handleBackButtonClickedFromNearbyAllTransportation();
                }
            }
        });
    }
}
