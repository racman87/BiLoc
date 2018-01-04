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
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements OnStreetViewPanoramaReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


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
    private FloatingActionButton addToFavorites;

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
        if(myView == null){
            myView = inflater.inflate(R.layout.fragment_detail, container, false);
        }
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

        float distance;

        String sDist = "--";

        if(MainActivity.gpsAtivate &&
                MainActivity.myLocation != null) {

            distance=(float)currentStation.getDistance();

            DecimalFormat df = new DecimalFormat("##.##");
            sDist = df.format(distance)+"km";
        }

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

        distanceText.setText(sDist);


        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.streetViewPanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        return myView;
    }

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
