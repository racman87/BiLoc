package com.biloc.biloc;

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
        OnMapReadyCallback {
    String TAG = "testBiloc";
    ListFragment listFragment;
    MapViewFragment mapFragment;
    ProfileFragment profileFragment;
    FavoritesFragment favoritesFragment;
    DetailFragment detailFragment;
    public static android.app.FragmentManager fragmentManager;
    //Décalre et instanciation d une array list d'objet de type AndroidVersion
    private static ArrayList<StationItem> stationList = new ArrayList<StationItem>();
    private static ArrayList<StationItem> favoritesList = new ArrayList<StationItem>();

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
            initStationList();

            profileFragment = new ProfileFragment();
            favoritesFragment = new FavoritesFragment();
            detailFragment = new DetailFragment();

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            onDrawerFragmentInteraction(MAP_DRAWER);
        } else if (id == R.id.nav_list) {
            onDrawerFragmentInteraction(LIST_DRAWER);
        } else if (id == R.id.nav_profile) {
            onDrawerFragmentInteraction(PROFILE_DRAWER);
        } else if (id == R.id.nav_favorites) {
            onDrawerFragmentInteraction(FAVORITES_DRAWER);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onDrawerFragmentInteraction(int fragmentIndexToCall) {
        Fragment fragmentToCall = null;
        switch (fragmentIndexToCall) {
            /****************************
             * Drawer list management
             ****************************/
            case MAP_DRAWER:
                fragmentToCall = mapFragment;
                break;
            case LIST_DRAWER:
                fragmentToCall = listFragment;
                break;
            case PROFILE_DRAWER:
                fragmentToCall = profileFragment;
                break;
            case FAVORITES_DRAWER:
                fragmentToCall = favoritesFragment;
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this listFragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(fragment_container, fragmentToCall);
        transaction.addToBackStack(null);
        Log.i(TAG, "onCreate: addToBackStack");
        // Commit the transaction
        transaction.commit();
    }

    private void addStationToFavorites(StationItem stationToAdd) {
        favoritesList.add(stationToAdd);
        stationList.get(stationList.indexOf(stationToAdd)).setFavorite();
    }

    private void removeStationFromFavorites(StationItem stationToRemove) {
        favoritesList.remove(stationToRemove);
        stationList.get(stationList.indexOf(stationToRemove)).setUnFavorite();
    }

    private void callDetailFragment(StationItem itemAtPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        detailFragment.updateElement(itemAtPosition);
        transaction.replace(fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(StationItem itemAtPosition) {
        callDetailFragment(itemAtPosition);
    }

    @Override
    public void onFavoritesFragmentInteraction(StationItem itemAtPosition) {
        callDetailFragment(itemAtPosition);
    }

    @Override
    public void onDetailFragmentInteraction(StationItem station, int buttonPressed) {
        switch (buttonPressed) {
            /****************************
             * Drawer list management
             ****************************/
            case FAVORITES_BUTTON:
                if (!favoritesList.contains(station)) {
                    addStationToFavorites(station);
                } else {
                    removeStationFromFavorites(station);
                }
                break;
            case MAP_BUTTON:
                //TODO
                break;
            case NAVIGATION_BUTTON:
                //TODO
                break;
        }
    }

    @Override
    public void onProfileFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onMapFragmentInteraction(int position, int fragmentCaller) {

    }

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
        station2.setFreeSlotNumber(3);
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
