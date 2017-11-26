package com.biloc.biloc;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this listFragment must implement the
 * {@link MapViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this listFragment.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the listFragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button mButton;
    private static GoogleMap mMap;
    StationItem currentStation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LatLng currentStation_LatLng;
    boolean zoomOnStation=false;
    int zoomMap=13;

    private OnFragmentInteractionListener mListener;


    String TAG = "testBiloc";
    private ArrayList<StationItem> stationList;

    public MapViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this listFragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of listFragment MapViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapViewFragment newInstance(String param1, String param2) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.i(TAG, "onCreate: mapFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this listFragment

        Log.i(TAG, "onCreateView: mapFragment");
        View myView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //LatLng toto = new LatLng(30, 20);
        //mMap.addMarker(new MarkerOptions().position(toto).title("PTSI"));
        /*mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(3);
            }
        });*/
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onMapFragmentInteraction(position, MainActivity.MAP_FRAGMENT);
        }
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

        //mMap.setMyLocationEnabled(true);

        if(!zoomOnStation)
        {
            zoomMap=13;
            //LatLng lausanne = new LatLng(46.523026, 6.610657);
            LatLng stImier = new LatLng(47.155150, 7.002794);
            currentStation_LatLng= stImier;
        }
        else
        {
            zoomOnStation=false;
        }

        for (StationItem station: stationList) {

            float fColor = BitmapDescriptorFactory.HUE_RED;
            if(station.getFreeSlotNumber()>0){
                fColor = BitmapDescriptorFactory.HUE_GREEN;
            }
            BitmapDescriptor color = BitmapDescriptorFactory.defaultMarker(fColor);
            String marker = station.getStationName() + " " +
                    station.getFreeSlotNumber() + "/" +
                    station.getNumberOfBike();
            mMap.addMarker(new MarkerOptions()
                    .position(station.getCoordinates())
                    .title(marker)
                    .icon(color));
        }
        //mMap.addMarker(new MarkerOptions().position(lausanne).title("Lausanne HES-SO"));
        //mMap.addMarker(new MarkerOptions().position(stImier).title("PTSI"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(stImier)); //ESSAI currentStation.getCoordinates()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentStation_LatLng));

        // Create a LatLngBounds that includes Australia.
        //LatLngBounds SUISSE = new LatLngBounds(lausanne,stImier);

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stImier, 13));//ESSAI
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentStation_LatLng, zoomMap));

        //Set the location pointer if permission accepted
        if(checkPermission()) {
            mMap.setMyLocationEnabled(true);
            Log.i(TAG, "onMapReady: POSITION OK");
            // Show zoon controls on the map layer
            mMap.getUiSettings().setZoomControlsEnabled(true);
            //Show My Location Button on the map
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else askPermission();

    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                MainActivity.REQ_PERMISSION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case MainActivity.REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    if(checkPermission())
                        mMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied

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
        currentStation_LatLng = currentStation.getCoordinates();
        zoomOnStation=true;
        zoomMap=20;

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
        void onMapFragmentInteraction(int position, int fragmentCaller );
    }

}
