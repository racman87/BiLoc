package com.biloc.biloc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.View;

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
        OnMapReadyCallback{
    String TAG = "testBiloc";
    ListFragment listFragment;
    MapViewFragment mapFragment;
    ProfileFragment profileFragment;
    FavoritesFragment favoritesFragment;
    DetailFragment detailFragment;
    private GoogleMap mMap;
    public static android.app.FragmentManager fragmentManager;
    //Décalre et instanciation d une array list d'objet de type AndroidVersion
    public static ArrayList<StationItem> stationList = new ArrayList<StationItem>();

    public static final int MAP_DRAWER = 1;
    public static final int LIST_DRAWER = 2;
    public static final int PROFILE_DRAWER = 3;
    public static final int FAVORITES_DRAWER = 4;
    public static final int MAP_FRAGMENT = 5;
    public static final int LIST_FRAGMENT = 6;
    public static final int PROFILE_FRAGMENT = 7;
    public static final int FAVORITES_FRAGMENT = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(fragment_container)!= null){
            if(savedInstanceState != null){
                return;
            }
            Log.i(TAG, "onCreate: findViewById");

            mapFragment = MapViewFragment.newInstance("TEST1", "TEST2");

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mapFragment);
            fragmentTransaction.commit();


            if(mapFragment == null){
                Log.i(TAG, "onCreate: mapFragment == null");
            } else{
                Log.i(TAG, "onCreate: mapFragment != null");
            }
            listFragment = new ListFragment();
            initList(stationList);

            mapFragment.setStationList(stationList);
            profileFragment = new ProfileFragment();
            favoritesFragment = new FavoritesFragment();
            detailFragment = new DetailFragment();

        }

        //-----------------------------------------------------------------------------------
        // Toolbar / drawer / floating action button
        //-----------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
        switch (fragmentIndexToCall){
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
                fragmentToCall = detailFragment;//favoritesFragment;
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

    /*
    @Override
    public void onFragmentInteraction(int position, int fragmentCaller ) {

        Fragment fragmentToCall = null;

        switch (fragmentCaller){
        */
            /****************************
             * Drawer list management
             ****************************/
        /*    case MAP_DRAWER:
                fragmentToCall = mapFragment;
                break;
            case LIST_DRAWER:
                fragmentToCall = listFragment;
                break;
            case PROFILE_DRAWER:
                fragmentToCall = profileFragment;
                break;
            case FAVORITES_DRAWER:
                fragmentToCall = detailFragment;//favoritesFragment;
                break;
                */
            /****************************
             * Other lists management
             ****************************/
        /*
            case LIST_FRAGMENT:
                fragmentToCall = detailFragment;
                break;
            case MAP_FRAGMENT:
                fragmentToCall = mapFragment;
            case PROFILE_FRAGMENT:
                fragmentToCall = mapFragment;
            case FAVORITES_FRAGMENT:
                fragmentToCall = mapFragment;
        }*/
/*
        Log.i(TAG, "onCreate: listFragment==null");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this listFragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(fragment_container, fragmentToCall);
        transaction.addToBackStack(null);
        Log.i(TAG, "onCreate: addToBackStack");
        // Commit the transaction
        transaction.commit();
        Log.i(TAG, "onCreate: commit");
        Log.i(TAG, "onCreate: listFragment!=null");
    }
*/

    @Override
    public void onListFragmentInteraction(StationItem itemAtPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        detailFragment.updateElement(itemAtPosition);
        transaction.replace(fragment_container, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFavoritesFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onDetailFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onProfileFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onMapFragmentInteraction(int position, int fragmentCaller) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Lausanne and move the camera
        LatLng lausanne = new LatLng(46.523026, 6.610657);
        mMap.addMarker(new MarkerOptions().position(lausanne).title("Lausanne HES-SO"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lausanne));

    }

    //----------------------------------------------------------------------
    // Peuplage de la custom list
    // ON remplit l'array list d'androidVersion
    //----------------------------------------------------------------------
    public void initList(ArrayList<StationItem> stationList) {
        StationItem station1 = new StationItem();
        station1.setNumberOfBike(7);
        station1.setDistance(15);
        station1.setStationName("PTSI");
        station1.setStationCity("St-Imier");
        station1.setCoordinates(new LatLng(47.154794, 7.002895));
        //On passe à la liste l'objet Android Version
        stationList.add(station1);

        StationItem station2 = new StationItem();
        station2.setNumberOfBike(3);
        station2.setDistance(8);
        station2.setStationName("Tech");
        station2.setStationCity("St-Imier");
        station2.setCoordinates(new LatLng(47.150236, 6.992532));
        //On passe à la liste l'objet Android Version
        stationList.add(station2);

        StationItem station3 = new StationItem();
        station3.setNumberOfBike(0);
        station3.setDistance(1);
        station3.setStationName("Place de la gare");
        station3.setStationCity("St-Imier");
        station3.setCoordinates(new LatLng(47.151778, 7.000812));
        //On passe à la liste l'objet Android Version
        stationList.add(station3);
    }
}
