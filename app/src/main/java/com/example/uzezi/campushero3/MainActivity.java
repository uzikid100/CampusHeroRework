package com.example.uzezi.campushero3;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private MapFragment mMapFragment;
    private ProfileFragment mProfileFragment;
    private POIFragment mPoiFragment;

    private LinearLayout mFabMapNormalLayout;
    private LinearLayout mFabMapHybridLayout;
    private LinearLayout mFabMapTerrainLayout;
    private LinearLayout mFabMapSatelliteLayout;

    private boolean fabExpanded = false;

    private FloatingActionButton mFabLocation;
    private FloatingActionButton mFabMapType;
    private FloatingActionButton fabNormal;
    private FloatingActionButton fabHybrid;
    private FloatingActionButton fabTerrain;
    private FloatingActionButton fabSatellite;

    private TabLayout.Tab mMapTab;
    private TabLayout.Tab mProfileTab;
    private TabLayout.Tab mPoiTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        //Sets the tab layout to the maps Fragment
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mProfileTab = tabLayout.getTabAt(0);
        mMapTab = tabLayout.getTabAt(1);
        mPoiTab = tabLayout.getTabAt(2);
        mMapTab.select();

        //Finds the layouts for fab action menu
        mFabMapNormalLayout = (LinearLayout) findViewById(R.id.layoutFabNormal);
        mFabMapHybridLayout= (LinearLayout) findViewById(R.id.layoutFabHybrid);
        mFabMapTerrainLayout= (LinearLayout) findViewById(R.id.layoutFabTerrain);
        mFabMapSatelliteLayout = (LinearLayout) findViewById(R.id.layoutFabSatellite);

        //Gets references to the Fabs in Menu
        mFabMapType = (FloatingActionButton) findViewById(R.id.fabMapType);
        mFabLocation = (FloatingActionButton) findViewById(R.id.fabLocation);
        fabNormal = (FloatingActionButton) findViewById(R.id.fabMapNormal);
        fabHybrid = (FloatingActionButton) findViewById(R.id.fabMapHybrid);
        fabTerrain = (FloatingActionButton) findViewById(R.id.fabMapTerrain);
        fabSatellite= (FloatingActionButton) findViewById(R.id.fabMapSatellite);

        mFabMapType.setOnClickListener(this);
        mFabLocation.setOnClickListener(this);
        fabNormal.setOnClickListener(this);
        fabHybrid.setOnClickListener(this);
        fabTerrain.setOnClickListener(this);
        fabSatellite.setOnClickListener(this);

        closeSubMenusFab();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == mMapTab){
                    enableFabs();
                }else{
                    disableFabs();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }////////End of OnCreate////////

    private void disableFabs(){
        mFabLocation.setVisibility(View.INVISIBLE);
        mFabMapType.setVisibility(View.INVISIBLE);
    }

    private void enableFabs(){
        mFabLocation.setVisibility(View.VISIBLE);
        mFabMapType.setVisibility(View.VISIBLE);
    }

    private void closeSubMenusFab(){
        mFabMapNormalLayout.setVisibility(View.INVISIBLE);
        mFabMapHybridLayout.setVisibility(View.INVISIBLE);
        mFabMapTerrainLayout.setVisibility(View.INVISIBLE);
        mFabMapSatelliteLayout.setVisibility(View.INVISIBLE);

//        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        mFabMapNormalLayout.setVisibility(View.VISIBLE);
        mFabMapHybridLayout.setVisibility(View.VISIBLE);
        mFabMapTerrainLayout.setVisibility(View.VISIBLE);
        mFabMapSatelliteLayout.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
//        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }


    private void setupViewPager(ViewPager viewPager) {
        mMapFragment = new MapFragment();
        mProfileFragment = new ProfileFragment();
        mPoiFragment = new POIFragment();
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(mProfileFragment, "Profile");
        adapter.addFragment(mMapFragment, "Campus Map");
        adapter.addFragment(mPoiFragment, "Points Of Interest");
        viewPager.setAdapter(adapter);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMapType:
                if(fabExpanded) {closeSubMenusFab();}
                  else {openSubMenusFab();}
                break;
            case R.id.fabLocation:
                mMapFragment.goToCurrentLocation();
            case R.id.fabMapNormal:
                mMapFragment.ChangeMapType(GoogleMap.MAP_TYPE_NORMAL);
                if(fabExpanded) {closeSubMenusFab();}
                break;
            case R.id.fabMapHybrid:
                mMapFragment.ChangeMapType(GoogleMap.MAP_TYPE_HYBRID);
                if(fabExpanded) {closeSubMenusFab();}
                break;
            case R.id.fabMapTerrain:
                mMapFragment.ChangeMapType(GoogleMap.MAP_TYPE_TERRAIN);
                if(fabExpanded) {closeSubMenusFab();}
                break;
            case R.id.fabMapSatellite:
                mMapFragment.ChangeMapType(GoogleMap.MAP_TYPE_SATELLITE);
                if(fabExpanded) {closeSubMenusFab();}
                break;
        }
    }

}
