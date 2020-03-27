package com.kinashe.kinasheandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;
import com.kinashe.kinasheandroid.Utils.ImageCard;
import com.kinashe.kinasheandroid.Utils.ImageCardHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardListAdapter;
import com.kinashe.kinasheandroid.Utils.TopBarHelper;

import java.util.List;

//handles the transportation tab on the app
public class TransportationActivity extends AppCompatActivity {
    private static final String TAG = "TransportationPage";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = TransportationActivity.this;

    List<ImageCard> cards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);

        cards = new ImageCardHelper("transportation").get();

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        ImageCardListAdapter myAdapter = new ImageCardListAdapter(this,cards);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);

        setupBottomBar();
        TopBarHelper.setTopText("Transportation | መጓጓዣ", TransportationActivity.this);
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
}

