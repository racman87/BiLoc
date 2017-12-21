package com.biloc.biloc;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this listFragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this listFragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the listFragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String TAG = "testBiloc";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int position;

    private static ArrayList<StationItem> stationList1;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this listFragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of listFragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "ListFragment onCreate: ");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this listFragment
        Log.i(TAG, "ListFragment onCreateView: ");
        View myView = inflater.inflate(R.layout.fragment_list, container, false);
        //Calcul des distances
        if(MainActivity.gpsAtivate==true)
        {
            for (StationItem station: stationList1) {
                station.setDistance(MainActivity.getDistance(station));
            }
        }
        else
        {
            for (StationItem station: stationList1) {
                station.setDistance(0.00);
            }
        }


        //Création de la custom list
        final StationListAdapter adapter = new StationListAdapter(getContext(), R.layout.cellule_list, MainActivity.getStationList());
        final ListView list = myView.findViewById(R.id.customlistView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                manageItem((StationItem) adapterView.getItemAtPosition(position));
            }
        });
        return myView;
    }

    public static void setStationList(ArrayList<StationItem> stationList) {
        stationList1 = stationList;
    }

    private void manageItem(StationItem itemAtPosition) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(itemAtPosition);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onListFragmentInteraction(5);
        }
    }


    /*public static double getDistance(StationItem station) {
        Location locationStation = new Location("Station");

        locationStation.setLatitude(station.getCoordinates().latitude);
        locationStation.setLongitude(station.getCoordinates().longitude);

        Location myLoc = new Location("My Position");

        float distance = 0;
        if(MainActivity.myLocation != null){

            myLoc.setLatitude(MainActivity.myLocation.getLatitude());
            myLoc.setLongitude(MainActivity.myLocation.getLongitude());

            distance = locationStation.distanceTo(myLoc)/1000;
        }

        Log.i("testBiloc", "onCreateView: Distance ->"+distance);
        return distance;
    }*/

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
        //void onFragmentInteraction(int position, int fragmentCaller );

        void onListFragmentInteraction(StationItem itemAtPosition);
    }





}
