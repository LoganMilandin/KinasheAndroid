package com.kinashe.kinasheandroid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kinashe.kinasheandroid.Firebase.BusinessInfo;
import com.kinashe.kinasheandroid.Utils.BottomBarHelper;
import com.kinashe.kinasheandroid.Utils.HomepageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * handles the flame activity on the app, also happens to be the
 * home/launch screen but besides that it does the same thing as the other
 * activities. For simplicity, the others aren't commented because it's the same
 * idea as this
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //for navbar menu
    private static final int ACTIVITY_NUM = 0;

    //for code reuse, notice all activities have identical
    //setupBottomBar methods
    private Context mContext = MainActivity.this;

    private RecyclerView businessDisplay;
    private RecyclerView.Adapter displayAdapter;
    private RecyclerView.LayoutManager displayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomBar();

        businessDisplay = findViewById(R.id.businessDisplay);
        displayManager = new LinearLayoutManager(this);
        businessDisplay.setLayoutManager(displayManager);

        List<String> test = new ArrayList<>();
        test.add("hello worldasdhfkjsahdsahf");
        test.add("hsjkaskjdhkajsf");
        test.add("blah blah blah");
        test.add("testtestetstetste");
        test.add("avery sucks dick");
        test.add("I dont know how to program");

        displayAdapter = new HomepageListAdapter(test);
        businessDisplay.setAdapter(displayAdapter);

    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomBar() {
        Log.d(TAG, "setupBottomBar: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottombar);
        BottomBarHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomBarHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void testDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Hotel/zyIUAAaI64NEpQIJFpHrgclfKPv1");

        myRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "firebase data:");

                        BusinessInfo data = dataSnapshot.getValue(BusinessInfo.class);
                        Log.d(TAG, "firebase data:" + data);

                    }

                    //
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                }
        );
    }
}