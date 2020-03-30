package com.kinashe.kinasheandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Utils.ImageCard;
import com.kinashe.kinasheandroid.Utils.ImageCardHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardListAdapter;

import java.util.List;

public class PlacesOrTransportationFragment extends Fragment {

    private static final String TAG = "Places Fragment";

    private View thisView;
    private RecyclerView typeGrid;
    private ImageCardListAdapter typeGridAdapter;
    private GridLayoutManager typeGridManager;
    private List<ImageCard> currentCards;
    private ImageCardHelper cardHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_places_or_transportation, container, false);
        createGridLayout();
        return thisView;
    }

    public PlacesOrTransportationFragment() {
        cardHelper = new ImageCardHelper();
    }

    private void createGridLayout() {
        Log.d(TAG, "trying to set up grid");
        String titleText = getArguments().getString("title");
        typeGrid = thisView.findViewById(R.id.type_grid);
        currentCards = cardHelper.getCards(titleText);
        typeGridAdapter = new ImageCardListAdapter(thisView, (MainActivity) getActivity(),
                PlacesOrTransportationFragment.this, currentCards);
        typeGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        typeGrid.setAdapter(typeGridAdapter);
        //also set top text while we're here
        TextView title = thisView.findViewById(R.id.title);
        title.setText(titleText);
        //also disable back button from top level page
        ImageView backButton = thisView.findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);
    }

    public ImageCardHelper getCardHelper() {
        return cardHelper;
    }
}
