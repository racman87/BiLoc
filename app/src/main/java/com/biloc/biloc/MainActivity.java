package com.biloc.biloc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

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
    private FirebaseUser currentUser;
    String TAG = "testBiloc";
    ListFragment listFragment;
    MapViewFragment mapFragment;
    ProfileFragment profileFragment;
    FavoritesFragment favoritesFragment;
    DetailFragment detailFragment;
    SettingsFragment settingsFragment;
    public static android.app.FragmentManager fragmentManager;
    public static ArrayList<StationItem> stationList;
    private static ArrayList<StationItem> favoritesList;

    private static boolean listInit=false;
    private static boolean noInternet=false;
    private static Snackbar snackbar;
    private AlertDialog.Builder builder1;
    private AlertDialog alert11;
    private boolean startApp=false;

    //private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;

    public static Location myLocation;

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

    public static boolean gpsAtivate=false;

    private static TextView internetStatus;
    private boolean ui;

    public static LocationManager locationManager;
    public FusedLocationProviderClient mFusedLocationClient;
    Context mContext;



    public static ArrayList<StationItem> getStationList() {
        return stationList;
    }

    public static ArrayList<StationItem> getFavoritesList() {
        return favoritesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("testBilocUser", "MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Log.i(TAG, "onCreate: findViewById");

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mContext=this;
            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if(checkPermission()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        0, locationListenerGPS);
                //isLocationEnabled();
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Enable Location");
                    alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
                    alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=alertDialog.create();
                    alert.show();
                }
                else{
                   /* AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Confirm Location");
                    alertDialog.setMessage("Your Location is enabled, please enjoy");
                    alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert=alertDialog.create();
                    alert.show();*/

                    //Check position
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                        Log.i(TAG, "onSuccess GPS: OK");
                                        MainActivity.myLocation=location;
                                        MainActivity.gpsAtivate=true;
                                    }
                                    else
                                    {
                                        Log.i(TAG, "onSuccess GPS: FAIL");
                                        MainActivity.gpsAtivate=false;
                                    }
                                }
                            });
                }
            }
            else askPermission();

            //Snackbar
            snackbar = Snackbar.make(findViewById(fragment_container), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

            //Internet connexion detection
            //NE PAS OUBLIER DE DESENREGISTRER!!!!!!!!!!!!!!
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(new InternetConnector_Receiver(), intentFilter);

            isNetworkAvailable();

            favoritesList = new ArrayList<>();
            stationList = new ArrayList<>();

        }

         /*   mapFragment = MapViewFragment.newInstance("TEST1", "TEST2");

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
            //initStationList();
            Log.i(TAG, "onCreate: INIT LIST**********************************************************");
            mapFragment.setStationList(stationList);
            ListFragment.setStationList(stationList);
        }*/


        //-----------------------------------------------------------------------------------
        // Toolbar / drawer / floating action button
        //-----------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onStart() {

        Log.i("testBilocUser", "MainActivity.onStart");
        super.onStart();

        // Login/user management
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        boolean uiSetter = currentUser != null;

        nav_Menu.findItem(R.id.nav_profile).setVisible(uiSetter);
        nav_Menu.findItem(R.id.nav_signOut).setVisible(uiSetter);
        nav_Menu.findItem(R.id.nav_signIn).setVisible(!uiSetter);
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
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
        if(startApp==true ) {
            if(noInternet==true)
            {
                Toast.makeText(this, "NO INTERNET : Station is not up to date", Toast.LENGTH_LONG).show();
            }
            if (id == R.id.nav_map) {
                onDrawerFragmentInteraction(mapFragment, getString(R.string.toolbarTitleMap));
            } else if (id == R.id.nav_list) {
                onDrawerFragmentInteraction(listFragment, getString(R.string.toolbarTitleList));
            } else if (id == R.id.nav_profile) {
                onDrawerFragmentInteraction(profileFragment, getString(R.string.toolbarTitleProfile));
            } else if (id == R.id.nav_favorites) {
                onDrawerFragmentInteraction(favoritesFragment, getString(R.string.toolbarTitleFavorites));
            } else if (id == R.id.nav_signIn) startIntent(SigninActivity.class);
            else if (id == R.id.nav_signOut) signOut();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else
        {
            Toast.makeText(this, "NO INTERNET: Activate to continue", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private void signOut() {
            mAuth.signOut();
            this.onStart();
    }

    private void startIntent(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
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


        /*
        if (id == R.id.action_settings) {

            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, settingsFragment)
                    .commit();
            //callFragment( settingsFragment, getString(R.string.toolbarTitleSettings));
            return true;
        }
         */

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
    /*public void initStationList() {
        StationItem station1 = new StationItem();
        station1.setNumberOfBike(7);
        station1.setFreeSlotNumber(5);
        station1.setStationName("Coop");
        station1.setStationCity("St-Imier");
        station1.setCoordinates(new LatLng(47.153448, 7.000458));
        stationList.add(station1);
        //station1.setDistance(MapViewFragment.getDistance(station1));

        StationItem station2 = new StationItem();
        station2.setNumberOfBike(3);
        station2.setFreeSlotNumber(2);
        station2.setDistance(8);
        station2.setStationName("Place du marché");
        station2.setStationCity("St-Imier");
        station2.setCoordinates(new LatLng(47.152615, 6.996265));
        stationList.add(station2);
        //station2.setDistance(MapViewFragment.getDistance(station2));

        StationItem station3 = new StationItem();
        station3.setNumberOfBike(5);
        station3.setFreeSlotNumber(0);
        station3.setDistance(1);
        station3.setStationName("Place de la gare");
        station3.setStationCity("St-Imier");
        station3.setCoordinates(new LatLng(47.151591, 6.999985));
        stationList.add(station3);

        //station3.setDistance(MapViewFragment.getDistance(station3));


        StationItem station4 = new StationItem();
        station4.setNumberOfBike(5);
        station4.setFreeSlotNumber(5);
        station4.setDistance(1);
        station4.setStationName("Vigie");
        station4.setStationCity("Lausanne");
        station4.setCoordinates(new LatLng(46.521367, 6.624278));
        stationList.add(station4);

        addStationToFavorites(station1);

    }*/

    private void processGETRequest() {
        Utils.processRequest(this, Request.Method.GET,  null,
                new Utils.VolleyCallback() {

                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        try {
                            Log.i(TAG, "onSuccessResponse -> result: "  +result);
                            JSONArray station = result.getJSONArray("station");
                            //String response = result.getString("AllowedBike");
                            Log.i(TAG, "onSuccessResponse -> response: "  +station);
                            stationList.clear();
                            for(int k=0; k<station.length(); k++)
                            {
                                JSONObject StationK = station.getJSONObject(k);
                                //Log.i(TAG, "onSuccessResponse -> Sation"+k+": "  +StationK);

                                initList(StationK,k);
                            }

                            if(startApp==false) {
                                startApp();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initList(JSONObject stationK, int k) {

        try {

            int  AllowedBike = stationK.getInt("AllowedBike");
            int  AvailableBike = stationK.getInt("AvailableBike");
            double  LocationLat = stationK.getDouble("LocationLat");
            double  LocationLng = stationK.getDouble("LocationLng");
            String Locality = stationK.getString("Locality");
            String StationName = stationK.getString("StationName");

            //Log.i(TAG, "onSuccessResponse -> Sation"+k+":\n AllowedBike: "+AllowedBike +"\n AvailableBike: "+AvailableBike +"\n LocationLat: "+LocationLat +  "\n LocationLng: "+LocationLng +  "\n Locality: "+Locality + "\n StationName: "+StationName);

            StationItem station = new StationItem();

            station.setNumberOfBike(AllowedBike);
            station.setFreeSlotNumber(AvailableBike);
            station.setStationName(StationName);
            station.setStationCity(Locality);
            station.setCoordinates(new LatLng(LocationLat, LocationLng));
            stationList.add(station);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startApp(){

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

            Log.i(TAG, "onCreate: INIT LIST**********************************************************");
            mapFragment.setStationList(stationList);
            ListFragment.setStationList(stationList);

            startApp=true;


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null;

    }

    public class InternetConnector_Receiver extends BroadcastReceiver {

        String TAG = "testBiloc";

        public InternetConnector_Receiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (isNetworkAvailable()==true) {
                    Log.i(TAG, "onReceive: TRUE ");

                    if(noInternet==true)
                    {
                        snackbar.setDuration(0);
                        snackbar.show();
                    }
                    noInternet=false;
                    processGETRequest();
                } else {
                    Log.i(TAG, "onReceive: FALSE");
                    snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    noInternet = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    // Check for permission to access Location
    public boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    public void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                MainActivity.REQ_PERMISSION);

    }

}


