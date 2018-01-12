package com.biloc.biloc;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {

    private OnFragmentInteractionListener mListener;
    TextView cityText;
    TextView stationNameText;
    TextView numberBikeText;
    TextView distanceText;
    View myView;
    StationItem currentStation;
    private FloatingActionButton addToFavorites;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(myView == null){
            myView = inflater.inflate(R.layout.fragment_detail, container, false);
        }

        //-----------------------------------------------------------------------------------
        // Ajout des listeners sur les trois floatting buttons
        //-----------------------------------------------------------------------------------
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

        FloatingActionButton seeStationOnMap = myView.findViewById(R.id.mapButton);
        seeStationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onDetailFragmentInteraction(currentStation,MainActivity.MAP_BUTTON);
                }
            }
        });

        FloatingActionButton startNavigation = myView.findViewById(R.id.navigationButton);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onDetailFragmentInteraction(currentStation,MainActivity.NAVIGATION_BUTTON);
                }
            }
        });


        //-----------------------------------------------------------------------------------
        // Récupération de l'information de distance de la station
        //-----------------------------------------------------------------------------------
        float distance;

        String sDist = "--";

        if(MainActivity.gpsAtivate &&
                MainActivity.myLocation != null) {

            distance=(float)currentStation.getDistance();

            DecimalFormat df = new DecimalFormat("##.##");
            sDist = df.format(distance)+"km";
        }


        //-----------------------------------------------------------------------------------
        // Remplissage des champs avec les informations de la station
        //-----------------------------------------------------------------------------------
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

        distanceText.setText(sDist);


        //-----------------------------------------------------------------------------------
        // Image street view
        //-----------------------------------------------------------------------------------
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.streetViewPanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        return myView;
    }

    //-----------------------------------------------------------------------------------
    // Modification du bouton addToFavorite en fonction de son état
    //-----------------------------------------------------------------------------------
    private void manageFavoriteState() {
        if(currentStation.getFavorite()){
            addToFavorites.setImageResource(R.mipmap.ic_favoriteblue3);
        }
        else{
            addToFavorites.setImageResource(R.mipmap.ic_favoriteblue2);
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
        currentStation = itemAtPosition;
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(this.currentStation.getCoordinates());
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
