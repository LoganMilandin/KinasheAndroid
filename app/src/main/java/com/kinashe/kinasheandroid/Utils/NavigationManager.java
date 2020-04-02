package com.kinashe.kinasheandroid.Utils;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;

import com.kinashe.kinasheandroid.CustomFragment;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;

public class NavigationManager {

    private static final String TAG = "NavigationManager";

    private MainActivity context;

    public NavigationManager(MainActivity context) {
        //all booleans default to false
        this.context = context;
    }

    public boolean handleBottombarItemSelected(MenuItem item) {
        CustomFragment toDisplay = null;
        FragmentTransaction transaction = context.manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.home:
                toDisplay = context.homeFragment;
                break;
            case R.id.search:
                toDisplay = context.searchFragment;
                break;
            case R.id.places:
                toDisplay = context.placesFragment;
                break;
            case R.id.transportation:
                toDisplay = context.transportationFragment;
                break;
            case R.id.addbusiness:
                toDisplay = context.addBusinessFragment;
                break;
        }
        if (item.isChecked()) {
            if (toDisplay == context.placesFragment || toDisplay == context.transportationFragment) {
                CustomFragment newFragment = new PlacesOrTransportationFragment();
                Bundle titleContainer = new Bundle();
                titleContainer.putString("title", toDisplay == context.placesFragment?
                        "Places | ቦታዎች":"Transportation | መጓጓዣ");
                newFragment.setArguments(titleContainer);
                CustomFragment child = toDisplay.getChild();
                while (child != null) {
                    transaction.remove(child);
                    child = child.getChild();
                }
                transaction.remove(toDisplay);
                transaction.add(R.id.topbar_and_content, newFragment).
                        hide(context.activeFragment).show(newFragment).commit();
                context.activeFragment = newFragment;
                return true;
            } else {
                //else we just display this fragment
                if (toDisplay.getChild() != null) {
                    transaction.remove(toDisplay.getChild());
                }
                transaction.hide(context.activeFragment).show(toDisplay).commit();
                context.activeFragment = toDisplay;
                toDisplay.setChild(null);
                return true;
            }
        } else while (toDisplay.getChild() != null) {
            toDisplay = toDisplay.getChild();
        }
        transaction.hide(context.activeFragment).show(toDisplay).commit();
        context.activeFragment = toDisplay;
        return true;
    }

    public void handleNewFragmentCreated(CustomFragment newFragment) {
        FragmentTransaction transaction = context.manager.beginTransaction();
        transaction.hide(newFragment.getParent());
        transaction.add(R.id.topbar_and_content, newFragment);
        transaction.show(newFragment);
        transaction.commit();
        context.activeFragment = newFragment;
    }

    public void handleBackClicked(CustomFragment oldFragment) {
        FragmentTransaction transaction = context.manager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.show(oldFragment.getParent());
        transaction.commit();
        oldFragment.getParent().setChild(null);
        context.activeFragment = oldFragment.getParent();
    }
}
