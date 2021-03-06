package com.example.waterbase.pbd;

import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentActivity extends AppCompatActivity {
    private Intent barometerIntent;
    private static float groundFloorBaseline =(float) 922.0;
    private static String serverUrl = "https://localhost:8080";

    public static void setGroundFloorBaseline(float baseline) {
        groundFloorBaseline = baseline;
    }

    public static float getGroundFloorBaseline() {
        return groundFloorBaseline;
    }

    public static void setServerUrl(String url) {
        serverUrl = url;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        setContentView(R.layout.fragment_main);
//        android.support.v7.widget.Toolbar toolbar = findViewById(g);
//        setSupportActionBar(toolbar);
//        Log.d(this.getClass().getSimpleName(),user.getDisplayName());
//        Log.d(this.getClass().getSimpleName(),user.getIdToken(true).getResult().getToken()  );
        //// Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));

        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
        barometerIntent = new Intent(getApplicationContext(), BarometerReaderService.class);
        startService(barometerIntent);
        Log.d("FragmentActivity", "started Barometer service");
    }
}
