package com.kinashe.kinasheandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


/**
 * handles the 5th tab on the app, where there is no nested fragments and
 * the only clickable content is the link to the Kinashe website
 */
public class AddBusinessFragment extends CustomFragment {

    public MainActivity context;

    private static final String TAG = "AddBusinessPage";
    private int navbarIndex = 4;
    private View myView;


    //only thing to do is initialize the view and set click listener for web link
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        setNavbarIndex(4);
        myView = inflater.inflate(R.layout.fragment_add_business, container, false);
        setWebpageClickListener();
        return myView;
    }

    public void setWebpageClickListener() {
        View addContainer = myView.findViewById(R.id.add_container);
        addContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View container) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kinashe.com/"));
                context.startActivity(browserIntent);
            }
        });
    }
}