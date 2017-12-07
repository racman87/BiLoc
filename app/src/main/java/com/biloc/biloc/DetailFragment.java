package com.biloc.biloc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.nearby.messages.Distance;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String TAG = "testBiloc";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView cityText;
    TextView stationNameText;
    TextView numberBikeText;
    TextView distanceText;
    View myView;
    StationItem currentStation;
    private Button addToFavorites;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_detail, container, false);
        addToFavorites = myView.findViewById(R.id.addFavoritesButton);
        manageFavoriteState();
        addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onDetailFragmentInteraction(currentStation,MainActivity.FAVORITES_BUTTON);
                    manageFavoriteState();
                }
            }
        });

        Button seeStationOnMap = myView.findViewById(R.id.mapButton);
        seeStationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onDetailFragmentInteraction(currentStation,MainActivity.MAP_BUTTON);
                }
            }
        });

        Button startNavigation = myView.findViewById(R.id.navigationButton);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onDetailFragmentInteraction(currentStation,MainActivity.NAVIGATION_BUTTON);
                }
            }
        });

        Location locationStation = new Location("Station");

        locationStation.setLatitude(currentStation.getCoordinates().latitude);
        locationStation.setLongitude(currentStation.getCoordinates().longitude);

       Location myLoc = new Location("My Position");

        myLoc.setLatitude(MainActivity.myLocation.getLatitude());
        myLoc.setLongitude(MainActivity.myLocation.getLongitude());

        float distance = locationStation.distanceTo(myLoc)/1000;

        Log.i(TAG, "onCreateView: Distance ->"+distance);


        //myView = this.getView();
        assert myView != null;
        cityText = myView.findViewById(R.id.cityText);
        cityText.setText(currentStation.getStationCity());
        stationNameText = myView.findViewById(R.id.stationNameText);
        stationNameText.setText(currentStation.getStationName());
        numberBikeText = myView.findViewById(R.id.numberBikeText);
        numberBikeText.setText(String.format("%d / %d",
                currentStation.getFreeSlotNumber(),
                currentStation.getNumberOfBike()));
        distanceText = myView.findViewById(R.id.distanceText);

        DecimalFormat df = new DecimalFormat("##.##");
        distanceText.setText(df.format(distance)+"km");

        return myView;
    }

    private void manageFavoriteState() {
        if(currentStation.getFavorite()){
            Drawable favorite = getContext().getDrawable(R.mipmap.ic_favorties2);
            addToFavorites.setCompoundDrawablesRelativeWithIntrinsicBounds(favorite, null, null, null);
            addToFavorites.setText(R.string.rem_favorite);
        }
        else{
            Drawable unFavorite = getContext().getDrawable(R.mipmap.ic_favoriteblue);
            addToFavorites.setCompoundDrawablesRelativeWithIntrinsicBounds(unFavorite, null, null, null);
            addToFavorites.setText(R.string.add_favorite);
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

    public void updateElement(StationItem itemAtPosition) {
        Log.i(TAG, "updateElement: new city=" + itemAtPosition.getStationCity());
        Log.i(TAG, "updateElement: new name=" + itemAtPosition.getStationName());
        currentStation = itemAtPosition;
        /*
        if(myView != null) {
            Log.i(TAG, "updateElement: view != null");
            Log.i(TAG, "updateElement: itemAtPosition.getStationName()="+ itemAtPosition.getStationName());
            stationNameText.setText(itemAtPosition.getStationName());
        }*/
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onDetailFragmentInteraction(StationItem currentStation, int buttonPressed);
    }
}
