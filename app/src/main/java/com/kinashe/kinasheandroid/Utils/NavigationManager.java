package com.kinashe.kinasheandroid.Utils;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.NearbyAllFragment;
import com.kinashe.kinasheandroid.NearbyAllListFragment;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;
import com.kinashe.kinasheandroid.SingleBusinessFragment;

public class NavigationManager {

    private static final String TAG = "NavigationManager";

    private boolean isSingleBusinessSelectedHome;
    private boolean isSingleBusinessSelectedSearch;

    private boolean isNearbyAllSelectedPlaces;
    private boolean isNearbyAllListSelectedPlaces;

    private boolean isNearbyAllSelectedTransportation;
    private boolean isNearbyAllListSelectedTransportation;

    private MainActivity context;

    public NavigationManager(MainActivity context) {
        //all booleans default to false
        this.context = context;
    }

    public boolean handleBottombarItemSelected(int itemId) {
        switch (itemId) {
            case R.id.home:
                return handleHomeIconClicked();
            case R.id.search:
                return handleSearchIconClicked();
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
            Log.d(TAG, "" + (context.singleBusinessFragmentHome == null));
            context.manager.beginTransaction().hide(context.activeFragment).show(context.singleBusinessFragmentHome).commit();
            context.activeFragment = context.singleBusinessFragmentHome;
        } else {
            //else just show the home fragment
            Log.d(TAG, "home icon clicked from home");
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.homeFragment).
                    commit();
            context.activeFragment = context.homeFragment;
        }
        return true;
    }

    private boolean handleSearchIconClicked() {
        if (isSingleBusinessSelectedSearch && context.activeFragment != context.singleBusinessFragmentSearch) {
            context.manager.beginTransaction().hide(context.activeFragment).show(context.singleBusinessFragmentSearch).commit();
            context.activeFragment = context.singleBusinessFragmentSearch;
        } else {
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.searchFragment).
                    commit();
            context.activeFragment = context.searchFragment;
        }
        return true;
    }

    private boolean handlePlacesIconClicked() {
        //if we're already on this screen
        if (context.activeFragment == context.placesFragment ||
                context.activeFragment == context.nearbyAllPlacesFragment ||
                context.activeFragment == context.nearbyAllPlacesListFragment) {
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
            this.isNearbyAllListSelectedPlaces = false;
            //set boolean for deeper fragment to false as well
        } else if (this.isNearbyAllListSelectedPlaces) {
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.nearbyAllPlacesListFragment).
                    commit();
            context.activeFragment = context.nearbyAllPlacesListFragment;
        } else if (this.isNearbyAllSelectedPlaces) {//else we must have clicked from a different tab
            Log.d(TAG, "nearby all selected places");
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
                context.activeFragment == context.nearbyAllTransportationFragment ||
                context.activeFragment == context.nearbyAllTransportationListFragment) {
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
            this.isNearbyAllListSelectedTransportation = false;
            //set boolean for deeper fragment to false as well
        } else if (this.isNearbyAllListSelectedTransportation) {
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    show(context.nearbyAllTransportationListFragment).
                    commit();
            context.activeFragment = context.nearbyAllTransportationListFragment;
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

    public void handleSingleBusinessSelectedFromHomepage(SingleBusinessFragment newFragment) {
        context.singleBusinessFragmentHome = newFragment;
        context.manager.
                beginTransaction().
                add(R.id.topbar_and_content, context.singleBusinessFragmentHome).
                hide(context.activeFragment).commit();
        context.activeFragment = context.singleBusinessFragmentHome;
        this.isSingleBusinessSelectedHome = true;
    }

    public void handleSingleBusinessSelectedFromSearch(SingleBusinessFragment newFragment) {
        context.singleBusinessFragmentSearch = newFragment;
        context.manager.
                beginTransaction().
                add(R.id.topbar_and_content, context.singleBusinessFragmentSearch).
                hide(context.activeFragment).
                commit();
        context.activeFragment = context.singleBusinessFragmentSearch;
        this.isSingleBusinessSelectedSearch = true;
    }

    public void handleBackClickedFromSingleBusinessHome() {
        context.manager.
                beginTransaction().
                remove(context.singleBusinessFragmentHome).
                show(context.homeFragment).
                commit();
        context.singleBusinessFragmentHome = null;
        context.activeFragment = context.homeFragment;
        this.isSingleBusinessSelectedHome = false;
    }

    public void handleBackClickedFromSingleBusinessSearch() {
        context.manager.
                beginTransaction().
                remove(context.singleBusinessFragmentSearch).
                show(context.searchFragment).
                commit();
        context.singleBusinessFragmentSearch = null;
        context.activeFragment = context.placesFragment;
        this.isSingleBusinessSelectedSearch = false;
    }

    public void handleLeafNodeClickedPlaces(NearbyAllFragment newFragment) {
        if (context.nearbyAllPlacesFragment == null) {
            context.nearbyAllPlacesFragment = newFragment;
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
                    add(R.id.topbar_and_content, context.nearbyAllPlacesFragment).
                    show(context.nearbyAllPlacesFragment).
                    commit();
            context.activeFragment = context.nearbyAllPlacesFragment;
            this.isNearbyAllSelectedPlaces = true;
        }
    }

    public void handleLeafNodeClickedTransportation(NearbyAllFragment newFragment) {
        if (context.nearbyAllTransportationFragment == null) {
            context.nearbyAllTransportationFragment = newFragment;
            context.manager.
                    beginTransaction().
                    hide(context.activeFragment).
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

    public void handleNearbyAllClickedPlaces(NearbyAllListFragment newFragment) {
        context.nearbyAllPlacesListFragment = newFragment;
        context.manager.beginTransaction().
                hide(context.activeFragment).
                add(R.id.topbar_and_content, context.nearbyAllPlacesListFragment).
                show(context.nearbyAllPlacesListFragment).
                commit();
        context.activeFragment = context.nearbyAllPlacesListFragment;
        this.isNearbyAllListSelectedPlaces = true;
    }

    public void handleNearbyAllClickedTransportation(NearbyAllListFragment newFragment) {
        context.nearbyAllTransportationListFragment = newFragment;
        context.manager.beginTransaction().
                hide(context.activeFragment).
                add(R.id.topbar_and_content, context.nearbyAllTransportationListFragment).
                show(context.nearbyAllTransportationListFragment).
                commit();
        context.activeFragment = context.nearbyAllTransportationListFragment;
        this.isNearbyAllListSelectedTransportation = true;
    }

    public void handleBackButtonClickedFromNearbyAllListPlaces() {
        context.manager.
                beginTransaction().
                hide(context.activeFragment).
                remove(context.nearbyAllPlacesListFragment).
                show(context.nearbyAllPlacesFragment).
                commit();
        context.activeFragment = context.nearbyAllPlacesFragment;
        this.isNearbyAllListSelectedPlaces = false;
    }

    public void handleBackButtonClickedFromNearbyAllListTransportation() {
        context.manager.
                beginTransaction().
                hide(context.activeFragment).
                remove(context.nearbyAllTransportationListFragment).
                show(context.nearbyAllTransportationFragment).
                commit();
        this.isNearbyAllListSelectedTransportation = false;
        context.activeFragment = context.nearbyAllTransportationFragment;
    }

}
