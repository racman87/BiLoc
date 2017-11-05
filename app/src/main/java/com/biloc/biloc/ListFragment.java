package com.biloc.biloc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    //Décalre et instanciation d une array list d'objet de type AndroidVersion
    public ArrayList<StationList> stationList = new ArrayList<StationList>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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

        //Remplissage de la liste cunstom
        initList(stationList);

        //Création de la custom list
        final ListAdapter adapter = new ListAdapter(getContext(), R.layout.cellule_list, stationList);
        final ListView list = (ListView) myView.findViewById(R.id.customlistView);
        list.setAdapter(adapter);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(5,MainActivity.LIST_FRAGMENT);
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
        void onFragmentInteraction(int position, int fragmentCaller );
    }

    //----------------------------------------------------------------------
    // Peuplage de la custom list
    // ON remplit l'array list d'androidVersion
    //----------------------------------------------------------------------
    private void initList(ArrayList<StationList> stationList) {
        StationList version = new StationList();
        version.setNumberOfBike("7");
        version.setStationDistance("15km");
        version.setStationName("PTSI");
        //On passe à la liste l'objet Android Version
        stationList.add(version);

        StationList version1 = new StationList();
        version1.setNumberOfBike("3");
        version1.setStationDistance("8km");
        version1.setStationName("Tech");
        //On passe à la liste l'objet Android Version
        stationList.add(version1);

        StationList version2 = new StationList();
        version2.setNumberOfBike("0");
        version2.setStationDistance("1km");
        version2.setStationName("Place de la gare");
        //On passe à la liste l'objet Android Version
        stationList.add(version2);
    }

}
