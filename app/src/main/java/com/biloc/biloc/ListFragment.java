package com.biloc.biloc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this listFragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListFragment extends Fragment {
    String TAG = "testBiloc";

    private OnFragmentInteractionListener mListener;
    private int position;

    private static ArrayList<StationItem> stationList1;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this listFragment
        View myView = inflater.inflate(R.layout.fragment_list, container, false);

        //-----------------------------------------------------------------------------------
        // Calcul des distance
        //-----------------------------------------------------------------------------------
        if(MainActivity.gpsAtivate)
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

        //Mettre les stations dans l'odre des distances
        stationList1 = Utils.orderStationList(stationList1);

        //Création de la custom list
        final StationListAdapter adapter = new StationListAdapter(getContext(), R.layout.cellule_list, MainActivity.getStationList());
        final ListView list = myView.findViewById(R.id.customlistView);
        list.setAdapter(adapter);
        //-----------------------------------------------------------------------------------
        // Ajout d'un listener sur la liste lorsque l'utilisateur clique sur un champ
        //-----------------------------------------------------------------------------------
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
