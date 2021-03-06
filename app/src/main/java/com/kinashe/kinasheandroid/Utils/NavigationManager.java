package com.kinashe.kinasheandroid.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;

import com.kinashe.kinasheandroid.Firebase.NotificationAdHelper;
import com.kinashe.kinasheandroid.MainActivity;
import com.kinashe.kinasheandroid.PlacesOrTransportationFragment;
import com.kinashe.kinasheandroid.R;

import java.util.LinkedList;

/**
 * The crutch of this app. Handles all app navigation
 * using my custom navigation schema that involved drawing a couple
 * graphs and trees.
 */
public class NavigationManager {

    private static final String TAG = "NavigationManager";
    private static final int MAX_BACKSTACK_SIZE = 8;

    private MainActivity context;
    private CustomFragStack<CustomFragment> backStack;
    //every time a new fragment is created, its tag is set to this
    //and this value is incremented
    private int fragmentIndex;

    public NavigationManager(MainActivity context) {
        //all booleans default to false
        this.context = context;
        backStack = new CustomFragStack<>();
        fragmentIndex = 5;
    }

    /**
     * handles an item from the navbar being selected. Long because
     * it needs to handle cases of clicking the icon that's already
     * selected and determining which fragment to navigate to when you click another screen
     * @param item
     */
    public void handleBottombarItemSelected(MenuItem item) {
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
                if (toDisplay == context.placesFragment) {
                    context.placesFragment = newFragment;
                    newFragment.setNavbarIndex(2);
                } else {
                    context.transportationFragment = newFragment;
                    newFragment.setNavbarIndex(3);
                }
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
                return;
            } else {
                //else we just display this fragment
                if (toDisplay.getChild() != null) {
                    transaction.remove(toDisplay.getChild());
                }
                transaction.hide(context.activeFragment).show(toDisplay).commit();
                context.activeFragment = toDisplay;
                toDisplay.setChild(null);
                return;
            }
        } else while (toDisplay.getChild() != null) {
            toDisplay = toDisplay.getChild();
        }
        transaction.hide(context.activeFragment).show(toDisplay).commit();
        backStack.push(context.activeFragment);
        context.activeFragment = toDisplay;
    }

    /**
     * handles when a click occurs somewhere in the app that would take the client
     * to a new screen
     * @param newFragment the fragment generated by the click
     */
    public void handleNewFragmentCreated(CustomFragment newFragment) {
        setFragmentNavbarIndex(newFragment);
        FragmentTransaction transaction = context.manager.beginTransaction();
        transaction.hide(newFragment.getParent());
        transaction.add(R.id.topbar_and_content, newFragment, String.valueOf(fragmentIndex++));
        transaction.show(newFragment);
        transaction.commit();
        context.activeFragment = newFragment;
        backStack.push(newFragment.getParent());
        NotificationAdHelper.showAd(context);

    }

    /**
     * handles the case where a back button in the app is clicked
     * @param oldFragment
     */
    public void handleBackClicked(CustomFragment oldFragment) {
        FragmentTransaction transaction = context.manager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.show(oldFragment.getParent());
        transaction.commit();
        oldFragment.getParent().setChild(null);
        if (backStack.peek() == oldFragment.getParent()) {
            backStack.pop();
        }
        context.activeFragment = oldFragment.getParent();
    }

    /**
     * handles the case where the client clicks back manually, using their
     * device's back button
     */
    public void handleBackClickedManual() {
        Log.d(TAG, "backstack size: " + backStack.size());
        if ((context.activeFragment == context.placesFragment || context.activeFragment == context.transportationFragment)
                && ((PlacesOrTransportationFragment) context.activeFragment).getTypeGridAdapter().getPreviousTitle() != null) {
            ((PlacesOrTransportationFragment) context.activeFragment).getTypeGridAdapter().goBack();
        } else if (!backStack.isEmpty()) {
            CustomFragment previous = backStack.peek();
            if (previous == context.activeFragment.getParent()) {
                handleBackClicked(context.activeFragment);
            } else if (context.manager.findFragmentByTag(previous.getTag()) != null) {
                context.manager.beginTransaction().hide(context.activeFragment).show(previous).commit();
                context.activeFragment = previous;
                backStack.pop();
                menuItemHelper(previous);
            } else {
                while (context.manager.findFragmentByTag(previous.getTag()) == null && backStack.size() > 1) {
                    backStack.pop();
                    previous = backStack.peek();
                }
                if (context.manager.findFragmentByTag(previous.getTag()) != null) {
                    context.manager.beginTransaction().hide(context.activeFragment).show(previous).commit();
                    context.activeFragment = previous;
                    backStack.pop();
                    menuItemHelper(previous);
                } else if (context.manager.findFragmentByTag(backStack.peek().getTag()) != null) {
                    // in this case we know the size is now 1, so check last element
                    previous = backStack.pop();
                    context.manager.beginTransaction().hide(context.activeFragment).show(previous).commit();
                    context.activeFragment = previous;
                    menuItemHelper(previous);
                }
            }
        } else {
            Log.d(TAG, "closing app");
            context.finish();
        }
    }

    private void menuItemHelper(CustomFragment toDisplay) {
        Menu menu = context.getBottomBar().getMenu();
        for (int i = 0; i < 5; i++) {
            menu.getItem(i).setChecked(false);
        }
        Log.d(TAG, "navIndex: " + toDisplay.getNavbarIndex());
        menu.getItem(toDisplay.getNavbarIndex()).setChecked(true);
    }

    /**
     * sets the correct navbar index for newly generated fragments
     * @param newFragment the newly generated fragment
     */
    private void setFragmentNavbarIndex(CustomFragment newFragment) {
        Menu menu = context.getBottomBar().getMenu();
        for (int i = 0; i < 5; i++) {
            if (menu.getItem(i).isChecked()) {
                newFragment.setNavbarIndex(i);
            }
        }
    }

    private class CustomFragStack<E> extends LinkedList<E> {
        public E pop() {
            return remove(size() - 1);
        }

        public void push(E item) {
            add(item);
            if (size() > MAX_BACKSTACK_SIZE) {
                remove();
            }
        }
        public E peek() {
            return get(size() - 1);
        }
    }
}
