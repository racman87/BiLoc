package com.biloc.biloc;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.biloc.biloc.R.id.fragment_container;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MapViewFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        ListFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener,
        DetailFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        OnMapReadyCallback {
    String TAG = "testBiloc";
    ListFragment listFragment;
    MapViewFragment mapFragment;
    ProfileFragment profileFragment;
    FavoritesFragment favoritesFragment;
    DetailFragment detailFragment;
    SettingsFragment settingsFragment;
    public static android.app.FragmentManager fragmentManager;
    private static ArrayList<StationItem> stationList;
    private static ArrayList<StationItem> favoritesList;

    public static final int MAP_DRAWER = 1;
    public static final int LIST_DRAWER = 2;
    public static final int PROFILE_DRAWER = 3;
    public static final int FAVORITES_DRAWER = 4;
    public static final int MAP_FRAGMENT = 5;
    public static final int LIST_FRAGMENT = 6;
    public static final int PROFILE_FRAGMENT = 7;
    public static final int FAVORITES_FRAGMENT = 8;

    public static final int FAVORITES_BUTTON = 1;
    public static final int MAP_BUTTON = 2;
    public static final int NAVIGATION_BUTTON = 3;

    public static final int REQ_PERMISSION = 1;


    public static ArrayList<StationItem> getStationList() {
        return stationList;
    }

    public static ArrayList<StationItem> getFavoritesList() {
        return favoritesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Log.i(TAG, "onCreate: findViewById");

            mapFragment = MapViewFragment.newInstance("TEST1", "TEST2");

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mapFragment);
            fragmentTransaction.commit();

            if (mapFragment == null) {
                Log.i(TAG, "onCreate: mapFragment == null");
            } else {
                Log.i(TAG, "onCreate: mapFragment != null");
            }
            listFragment = new ListFragment();
            profileFragment = new ProfileFragment();
            favoritesFragment = new FavoritesFragment();
            detailFragment = new DetailFragment();
            settingsFragment = SettingsFragment.newInstance();

            favoritesList = new ArrayList<>();
            stationList = new ArrayList<>();
            initStationList();
            mapFragment.setStationList(stationList);
        }


        //-----------------------------------------------------------------------------------
        // Toolbar / drawer / floating action button
        //-----------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //----------------------------------------------------------------------
    // Drawer methods
    //----------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            onDrawerFragmentInteraction(mapFragment, getString(R.string.toolbarTitleMap));
        } else if (id == R.id.nav_list) {
            onDrawerFragmentInteraction(listFragment, getString(R.string.toolbarTitleList));
        } else if (id == R.id.nav_profile) {
            onDrawerFragmentInteraction(profileFragment, getString(R.string.toolbarTitleProfile));
        } else if (id == R.id.nav_favorites) {
            onDrawerFragmentInteraction(favoritesFragment, getString(R.string.toolbarTitleFavorites));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onDrawerFragmentInteraction(Fragment fragmentToCall, String toolBarTitle) {
        callFragment(fragmentToCall, toolBarTitle);
        setTitle(toolBarTitle);
    }

    //----------------------------------------------------------------------
    // Action bar methods
    //----------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
            //callFragment( settingsFragment, getString(R.string.toolbarTitleSettings));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------
    // Fragment Interaction helper methods
    //----------------------------------------------------------------------

    private void addStationToFavorites(StationItem stationToAdd) {
        favoritesList.add(stationToAdd);
        stationList.get(stationList.indexOf(stationToAdd)).setFavorite(true);
    }

    private void removeStationFromFavorites(StationItem stationToRemove) {
        favoritesList.remove(stationToRemove);
        stationList.get(stationList.indexOf(stationToRemove)).setFavorite(false);
    }

    private void callFragment(Fragment fragmentToCall, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setTitle(title);
        transaction.replace(fragment_container, fragmentToCall);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //----------------------------------------------------------------------
    // Fragment Interaction's Listeners
    //----------------------------------------------------------------------
    @Override
    public void onListFragmentInteraction(StationItem itemAtPosition) {
        detailFragment.updateElement(itemAtPosition);
        callFragment(detailFragment, getString(R.string.toolbarTitleDetail));
    }

    @Override
    public void onFavoritesFragmentInteraction(StationItem itemAtPosition) {
        detailFragment.updateElement(itemAtPosition);
        callFragment(detailFragment, getString(R.string.toolbarTitleDetail));
    }

    @Override
    public void onDetailFragmentInteraction(StationItem station, int buttonPressed) {
        switch (buttonPressed) {
            /************************************
             * DetailFragment button management
             ************************************/
            case FAVORITES_BUTTON:
                Log.i(TAG, "onDetailFragmentInteraction: ADD TO FAVORITES ");
                if (!favoritesList.contains(station)) {
                    addStationToFavorites(station);
                } else {
                    removeStationFromFavorites(station);
                }
                break;
            case MAP_BUTTON:
                Log.i(TAG, "onDetailFragmentInteraction: SHOW STATION ON MAP ");
                mapFragment.updateElement(station);
                callFragment(mapFragment, getString(R.string.toolbarTitleMap));
                break;
            case NAVIGATION_BUTTON:
                Log.i(TAG, "onDetailFragmentInteraction: NAVIGATION ");

                // Create a Uri from an intent string. Use the result to create an Intent.
                LatLng latLng=station.getCoordinates();
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLng.latitude+","+latLng.longitude+"&mode=b");

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);

                //TODO
                break;
        }
    }

    @Override
    public void onProfileFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onMapFragmentInteraction(StationItem itemAtPosition) {
        detailFragment.updateElement(itemAtPosition);
        callFragment(detailFragment, getString(R.string.toolbarTitleDetail));
    }

    @Override
    public void onSettingsFragmentInteraction(Uri uri) {

    }

    //----------------------------------------------------------------------
    // Maps method
    //----------------------------------------------------------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Lausanne and move the camera
        LatLng lausanne = new LatLng(46.523026, 6.610657);
        googleMap.addMarker(new MarkerOptions().position(lausanne).title("Lausanne HES-SO"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lausanne));
    }

    //----------------------------------------------------------------------
    // Peuplage de la custom list
    // ON remplit l'array list d'androidVersion
    //----------------------------------------------------------------------
    public void initStationList() {
        StationItem station1 = new StationItem();
        station1.setNumberOfBike(7);
        station1.setFreeSlotNumber(5);
        station1.setDistance(15);
        station1.setStationName("PTSI");
        station1.setStationCity("St-Imier");
        station1.setCoordinates(new LatLng(47.154794, 7.002895));
        stationList.add(station1);

        StationItem station2 = new StationItem();
        station2.setNumberOfBike(3);
        station2.setFreeSlotNumber(2);
        station2.setDistance(8);
        station2.setStationName("Tech");
        station2.setStationCity("St-Imier");
        station2.setCoordinates(new LatLng(47.150236, 6.992532));
        stationList.add(station2);

        StationItem station3 = new StationItem();
        station3.setNumberOfBike(5);
        station3.setFreeSlotNumber(0);
        station3.setDistance(1);
        station3.setStationName("Place de la gare");
        station3.setStationCity("St-Imier");
        station3.setCoordinates(new LatLng(47.151778, 7.000812));
        stationList.add(station3);

        addStationToFavorites(station1);

    }

}

