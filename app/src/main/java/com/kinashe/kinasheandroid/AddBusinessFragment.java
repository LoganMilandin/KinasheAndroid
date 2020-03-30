package com.kinashe.kinasheandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


//handles the handshake tab on the app
public class AddBusinessFragment extends Fragment {
    private static final String TAG = "AddBusinessPage";
    //for navbar
    private static final int ACTIVITY_NUM = 3;

    private View myView;

    private MainActivity context;

    public AddBusinessFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_add_business, container, false);
        context = (MainActivity) getActivity();
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