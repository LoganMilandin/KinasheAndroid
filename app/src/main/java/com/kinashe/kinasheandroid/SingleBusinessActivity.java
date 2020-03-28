package com.kinashe.kinasheandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Firebase.Coupon;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;

import java.util.ArrayList;
import java.util.List;

public class SingleBusinessActivity extends AppCompatActivity {

    private static final String TAG = "SingleBusinessActivity";

    //for navbar menu
    private static final int ACTIVITY_NUM = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business);
        setupBottomBar();
        List<Coupon> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Coupon c = new Coupon();
            c.setTitle("this is a coupon");
            c.setDeal("this is a coupon");
            c.setAvailability("this is a coupon");
            c.setExpiration("this is a coupon");
            list.add(c);
        }
        for (Coupon c : list) {
            addCoupon(c);
        }
    }

    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottombar);
        BottomBarHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomBarHelper.enableNavigation(SingleBusinessActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void addCoupon(Coupon coupon) {
        ViewGroup list = findViewById(R.id.scrollable_part);
        View couponMarkup = LayoutInflater.from(this).inflate(R.layout.layout_coupon, list, false);
        TextView title = couponMarkup.findViewById(R.id.title);
        TextView deal = couponMarkup.findViewById(R.id.deal);
        TextView availability = couponMarkup.findViewById(R.id.availability);
        TextView expiration = couponMarkup.findViewById(R.id.expiration);

        title.setText(coupon.getTitle());
        deal.setText(coupon.getDeal());
        availability.setText(coupon.getAvailability());
        expiration.setText(coupon.getExpiration());
        list.addView(couponMarkup);
    }
}
