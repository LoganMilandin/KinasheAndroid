package com.kinashe.kinasheandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardListAdapter;
import com.kinashe.kinasheandroid.Utils.TopBarHelper;

import java.util.List;

//handles the place tab on the app
public class PlacesActivity extends AppCompatActivity {
    private static final String TAG = "PlacesPage";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = PlacesActivity.this;

    List<ImageCard> cards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        cards = new ImageCardHelper("places").get();

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        ImageCardListAdapter myAdapter = new ImageCardListAdapter(this, cards);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);

        setupBottomBar();
        TopBarHelper.setTopText("Places | ቦታ", PlacesActivity.this);
    }

    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottombar);
        BottomBarHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomBarHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void setupTopBar() {
        TextView topText = findViewById(R.id.toptext);
        topText.setText("hello world");
    }
}

