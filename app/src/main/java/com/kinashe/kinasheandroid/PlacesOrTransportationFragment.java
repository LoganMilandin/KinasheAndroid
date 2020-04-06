package com.kinashe.kinasheandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kinashe.kinasheandroid.Utils.CustomFragment;
import com.kinashe.kinasheandroid.Utils.ImageCard;
import com.kinashe.kinasheandroid.Utils.ImageCardHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardListAdapter;

import java.util.List;

/**
 * handles both the places tab and the transportation tab. A different instance
 * of this fragment is used for each tab, but each one just recycles their views
 * until a leaf node is clicked
 */
public class PlacesOrTransportationFragment extends CustomFragment {

    private static final String TAG = "PlacesOrTransportation";

    public MainActivity context;
    private View thisView;
    private RecyclerView typeGrid;
    private ImageCardListAdapter typeGridAdapter;
    private GridLayoutManager typeGridManager;
    private List<ImageCard> currentCards;
    private ImageCardHelper cardHelper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = (MainActivity) getActivity();
        thisView = inflater.inflate(R.layout.fragment_places_or_transportation, container, false);
        createGridLayout();
        return thisView;
    }

    public PlacesOrTransportationFragment() {
        Log.d(TAG, "creating card helper");
        cardHelper = new ImageCardHelper();
    }

    /**
     * creates initial grid layout with top level cards. When one is selected it changes
     * the list, only generating a new fragment if a leaf node is clicked
     */
    private void createGridLayout() {
        Log.d(TAG, "trying to set up grid");
        String titleText = getArguments().getString("title");
        typeGrid = thisView.findViewById(R.id.type_grid);
        currentCards = cardHelper.getCards(titleText);
        typeGridAdapter = new ImageCardListAdapter(thisView, (MainActivity) getActivity(),
                PlacesOrTransportationFragment.this, currentCards);
        //change settings on this grid layout manager
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        typeGrid.setLayoutManager(manager);
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

    public ImageCardListAdapter getTypeGridAdapter() { return typeGridAdapter; }

}
