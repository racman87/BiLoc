package com.biloc.biloc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this listFragment must implement the
 * {@link MapViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback{
    private static GoogleMap mMap;
    StationItem currentStation;
    private FusedLocationProviderClient mFusedLocationClient;

    boolean zoomOnStation=false;

    private OnFragmentInteractionListener mListener;


    String TAG = "testBiloc";
    private ArrayList<StationItem> stationList;


    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this listFragment
        View myView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentStation_LatLng;
        int zoomMap;

        //---------------------------------------------------------------------------------
        // Afichage de  la postion utilisateur ainsi que des bouttons google map
        //---------------------------------------------------------------------------------
        if(checkPermission()) {
            mMap.setMyLocationEnabled(true);
            // Show zoom controls on the map layer
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //Show My Location Button on the map
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

           //Check position
           mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                MainActivity.myLocation=location;
                                MainActivity.gpsAtivate=true;
                            }
                            else
                            {
                                MainActivity.gpsAtivate=false;
                            }
                        }
                    });

        }
        else askPermission();

        //---------------------------------------------------------------------------------
        // Positionnement de la caméra et du zoom
        //---------------------------------------------------------------------------------
        if(!zoomOnStation)
        {
            LatLng lausanne = new LatLng(46.523026, 6.610657);
            //LatLng stImier = new LatLng(47.155150, 7.002794);
            if(MainActivity.gpsAtivate)
            {
                currentStation_LatLng= new LatLng(MainActivity.myLocation.getLatitude(), MainActivity.myLocation.getLongitude());;
                zoomMap=13;
            }
            else
            {
                currentStation_LatLng= lausanne;
                zoomMap=8;
            }


        }
        else
        {
            currentStation_LatLng = currentStation.getCoordinates();
            zoomOnStation=false;
            zoomMap=17;
        }

        //---------------------------------------------------------------------------------
        // Affichage des marqueurs pour chaque station
        //---------------------------------------------------------------------------------
        for (StationItem station: stationList) {

            float fColor = BitmapDescriptorFactory.HUE_RED;
            int freeSlotNumber = station.getFreeSlotNumber();
            if(freeSlotNumber>=3){
                fColor = BitmapDescriptorFactory.HUE_GREEN;
            }
            else if(freeSlotNumber >0 && freeSlotNumber < 3){
                fColor = BitmapDescriptorFactory.HUE_ORANGE;
            }
            BitmapDescriptor color = BitmapDescriptorFactory.defaultMarker(fColor);
            String marker = station.getStationName() + " " +
                    station.getFreeSlotNumber() + "/" +
                    station.getNumberOfBike();
            mMap.addMarker(new MarkerOptions()
                    .position(station.getCoordinates())
                    .title(marker)
                    .alpha(0.8f)
                    .icon(color)).setTag(station);

        }

        //---------------------------------------------------------------------------------
        // Ajout d'un listener sur les maqueurs pour détecter un clique et pouvoir ouvrir
        // la page détail correspondante
        //---------------------------------------------------------------------------------
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(final Marker marker) {
                // Retrieve the data from the marker.
                StationItem station = (StationItem) marker.getTag();

                // Check if a click count was set, then display the click count.
                if (station != null) {
                    mListener.onMapFragmentInteraction(station);
                }

                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).
                return false;
            }

        });
        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentStation_LatLng, zoomMap));




        //---------------------------------------------------------------------------------
        // Changement du style de la carte
        //---------------------------------------------------------------------------------
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }


    }

    // Check for permission to access Location
    public boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    public void askPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                MainActivity.REQ_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case MainActivity.REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    if(checkPermission())
                        mMap.setMyLocationEnabled(true);

                }
                break;
            }
        }
    }

    public void setStationList(ArrayList<StationItem> stationList) {
        this.stationList = stationList;
    }

    public void updateElement(StationItem itemAtPosition) {
        currentStation = itemAtPosition;
        zoomOnStation=true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * listFragment to allow an interaction in this listFragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMapFragmentInteraction(StationItem itemAtPosition);
    }



}
