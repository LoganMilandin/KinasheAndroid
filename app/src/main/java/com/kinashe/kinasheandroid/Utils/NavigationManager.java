package com.kinashe.kinasheandroid.Utils;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.NearbyAllFragment;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.SingleBusinessFragment;

public class NavigationManager {

    private static final String TAG = "NavigationManager";

    private boolean isSingleBusinessSelectedHome;

    private boolean isNearbyAllSelectedPlaces;
    private boolean isNearbyAllSelectedTransportation;

    private MainActivity context;

    public NavigationManager(MainActivity context) {
        this.isSingleBusinessSelectedHome = false;
        this.isNearbyAllSelectedPlaces = false;
        this.isNearbyAllSelectedTransportation = false;
        this.context = context;
    }

    public boolean handleBottombarItemSelected(int itemId) {
        switch (itemId) {
            case R.id.home:
                return handleHomeIconClicked();
            case R.id.places:
                return handlePlacesIconClicked();
            case R.id.transportation:
                return handleTransportationIconClicked();
            case R.id.addbusiness:
                context.manager.beginTransaction().hide(context.activeFragment).show(context.addBusinessFragment).commit();
                context.activeFragment = context.addBusinessFragment;
                return true;
        }
        return false;
    }

    //handles the case where the home icon was clicked
    private boolean handleHomeIconClicked() {
        //render the single business view unless we are already on the home fragment
        if (isSingleBusinessSelectedHome && context.activeFragment != context.singleBusinessFragmentHome) {
            Log.d(TAG, "home icon clicked");
            context.manager.beginTransaction().hide(context.activeFragment).show(context.singleBusinessFragmentHome).commit();
            context.activeFragment = context.singleBusinessFragmentHome;
        } else {
            //else just show the home fragment
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.homeFragment).
                    commit();
            context.activeFragment = context.homeFragment;
        }
        return true;
    }

    private boolean handlePlacesIconClicked() {
        //if we're already on this screen
        if (context.activeFragment == context.placesFragment ||
                context.activeFragment == context.nearbyAllPlacesFragment) {
            Log.d(TAG, "clicked places from places");
            //go back to top level places screen
            PlacesOrTransportationFragment newPlaces = new PlacesOrTransportationFragment();
            Bundle titleContainer = new Bundle();
            titleContainer.putString("title", "Places | ቦታዎች");
            newPlaces.setArguments(titleContainer);
            FragmentTransaction transaction = context.manager.beginTransaction();
            transaction.hide(context.activeFragment);
            if (context.nearbyAllPlacesFragment != null) {
                transaction.remove(context.nearbyAllPlacesFragment);
            }
            if (context.placesFragment != null) {
                transaction.remove(context.placesFragment);
            }
            transaction.add(R.id.topbar_and_content, newPlaces).show(newPlaces).commit();
            context.placesFragment = newPlaces;
            context.activeFragment = context.placesFragment;
            context.nearbyAllPlacesFragment = null;
            this.isNearbyAllSelectedPlaces = false;
            //set boolean for deeper fragment to false as well
        } else if (this.isNearbyAllSelectedPlaces) {//else we must have clicked from a different tab
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.nearbyAllPlacesFragment).
                    commit();
            context.activeFragment = context.nearbyAllPlacesFragment;
        } else {
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.placesFragment).
                    commit();
            context.activeFragment = context.placesFragment;
        }
        return true;
    }

    private boolean handleTransportationIconClicked() {
        //if we're already on this screen
        if (context.activeFragment == context.transportationFragment ||
                context.activeFragment == context.nearbyAllTransportationFragment) {
            Log.d(TAG, "clicked places from places");
            //go back to top level places screen
            PlacesOrTransportationFragment newTransportation = new PlacesOrTransportationFragment();
            Bundle titleContainer = new Bundle();
            titleContainer.putString("title", "Transportation | መጓጓዣ");
            newTransportation.setArguments(titleContainer);
            FragmentTransaction transaction = context.manager.beginTransaction();
            transaction.hide(context.activeFragment);
            if (context.nearbyAllTransportationFragment != null) {
                transaction.remove(context.nearbyAllTransportationFragment);
            }
            if (context.transportationFragment != null) {
                transaction.remove(context.transportationFragment);
            }
            transaction.add(R.id.topbar_and_content, newTransportation).show(newTransportation).commit();
            context.transportationFragment = newTransportation;
            context.activeFragment = context.transportationFragment;
            context.nearbyAllTransportationFragment = null;
            this.isNearbyAllSelectedTransportation = false;
            //set boolean for deeper fragment to false as well
        } else if (this.isNearbyAllSelectedTransportation) {//else we must have clicked from a different tab
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.nearbyAllTransportationFragment).
                    commit();
            context.activeFragment = context.nearbyAllTransportationFragment;
        } else {
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.transportationFragment).
                    commit();
            context.activeFragment = context.transportationFragment;
        }
        return true;
    }

    public void selectSingleBusinessFromHomepage(BusinessInfo business) {
        Fragment newFragment = new SingleBusinessFragment();
        Bundle businessWrapper = new Bundle();
        businessWrapper.putSerializable("businessInfo", business);
        newFragment.setArguments(businessWrapper);
        context.singleBusinessFragmentHome = newFragment;
        context.manager.
                beginTransaction().
                add(R.id.topbar_and_content, context.singleBusinessFragmentHome).
                hide(context.homeFragment).commit();
        context.activeFragment = context.singleBusinessFragmentHome;
        this.isSingleBusinessSelectedHome = true;
    }

    public void clickBackFromSingleBusinessHome() {
        context.manager.
                beginTransaction().
                remove(context.singleBusinessFragmentHome).
                show(context.homeFragment).
                commit();
        context.singleBusinessFragmentHome = null;
        context.activeFragment = context.homeFragment;
        this.isSingleBusinessSelectedHome = false;
    }

    public void handleLeafNodeClickedPlaces(Bundle categoryContainer) {
        if (context.nearbyAllPlacesFragment == null) {
            context.nearbyAllPlacesFragment = new NearbyAllFragment();
            context.nearbyAllPlacesFragment.setArguments(categoryContainer);
            context.manager.
                    beginTransaction().
                    hide(context.placesFragment).
                    add(R.id.topbar_and_content, context.nearbyAllPlacesFragment).
                    show(context.nearbyAllPlacesFragment).
                    commit();
            context.activeFragment = context.nearbyAllPlacesFragment;
            this.isNearbyAllSelectedPlaces = true;
        }
    }

    public void handleLeafNodeClickedTransportation(Bundle categoryContainer) {
        if (context.nearbyAllTransportationFragment == null) {
            Log.d(TAG, "hello world");
            context.nearbyAllTransportationFragment = new NearbyAllFragment();
            context.nearbyAllTransportationFragment.setArguments(categoryContainer);
            context.manager.
                    beginTransaction().
                    hide(context.transportationFragment).
                    add(R.id.topbar_and_content, context.nearbyAllTransportationFragment).
                    show(context.nearbyAllTransportationFragment).
                    commit();
            context.activeFragment = context.nearbyAllTransportationFragment;
            this.isNearbyAllSelectedTransportation = true;
        }
    }

    public void handleBackButtonClickedFromNearbyAllPlaces() {
        context.manager.
                beginTransaction().
                hide(context.activeFragment).
                remove(context.nearbyAllPlacesFragment).
                show(context.placesFragment).
                commit();
        context.activeFragment = context.placesFragment;
        context.nearbyAllPlacesFragment = null;
        this.isNearbyAllSelectedPlaces = false;
    }

    public void handleBackButtonClickedFromNearbyAllTransportation() {
        Log.d(TAG, String.valueOf(context.nearbyAllTransportationFragment == null));
        context.manager.
                beginTransaction().
                hide(context.activeFragment).
                remove(context.nearbyAllTransportationFragment).
                show(context.transportationFragment).
                commit();
        context.activeFragment = context.transportationFragment;
        context.nearbyAllTransportationFragment = null;
        this.isNearbyAllSelectedTransportation = false;
    }
}
