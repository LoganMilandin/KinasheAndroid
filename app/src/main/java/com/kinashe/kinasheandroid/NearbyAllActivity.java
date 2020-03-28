package com.kinashe.kinasheandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;
import com.kinashe.kinasheandroid.Utils.ImageCard;
import com.kinashe.kinasheandroid.Utils.ImageCardHelper;
import com.kinashe.kinasheandroid.Utils.ImageCardListAdapter;
import com.kinashe.kinasheandroid.Utils.NearbyAllAdapter;
import com.kinashe.kinasheandroid.Utils.TopBarHelper;

import java.util.List;

//handles the place tab on the app
public class NearbyAllActivity extends AppCompatActivity {
    private static final String TAG = "NearbyAllPage";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = NearbyAllActivity.this;

    ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_all);

        image = (ImageView) findViewById(R.id.nearbyAllImage);

        Intent intent = getIntent();
        int receivedImage = intent.getExtras().getInt("image");
        String receivedText = intent.getExtras().getString("topText");

        image.setImageResource(receivedImage);

        setupBottomBar();
        TopBarHelper.setTopText(receivedText, NearbyAllActivity.this);
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