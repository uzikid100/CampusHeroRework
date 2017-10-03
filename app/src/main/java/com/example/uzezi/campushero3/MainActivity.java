package com.example.uzezi.campushero3;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {


    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private LinearLayout mFabMapNormalLayout;
    private LinearLayout mFabMapHybridLayout;
    private LinearLayout mFabMapTerrainLayout;
    private LinearLayout mFabMapSatelliteLayout;

    private boolean fabExpanded = false;
    private FloatingActionButton fabSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        //Sets the tab layout to the maps Fragment
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();

        //Finds the layouts for fab action menu
        mFabMapNormalLayout = (LinearLayout) findViewById(R.id.layoutFabNormal);
        mFabMapHybridLayout= (LinearLayout) findViewById(R.id.layoutFabHybrid);
        mFabMapTerrainLayout= (LinearLayout) findViewById(R.id.layoutFabTerrain);
        mFabMapSatelliteLayout = (LinearLayout) findViewById(R.id.layoutFabSatellite);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Current Location...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
                return false;
            }
        });

        closeSubMenusFab();
    }

    private void closeSubMenusFab(){
        mFabMapNormalLayout.setVisibility(View.INVISIBLE);
        mFabMapHybridLayout.setVisibility(View.INVISIBLE);
        mFabMapTerrainLayout.setVisibility(View.INVISIBLE);
        mFabMapSatelliteLayout.setVisibility(View.INVISIBLE);

//        fabSettings.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
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
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new MapFragment(), "Campus Map");
        adapter.addFragment(new ProfileFragment(), "Points Of Interest");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeMenus(View view) {
        if(fabExpanded)
            closeSubMenusFab();
    }
}
