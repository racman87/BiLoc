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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FavoritesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private int position;
    String TAG = "testBiloc";

    public FavoritesFragment() {
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
        View myView = inflater.inflate(R.layout.fragment_favorites, container, false);
        //Création de la custom list
        final StationListAdapter adapter = new StationListAdapter(getContext(), R.layout.cellule_list, MainActivity.getFavoritesList());
        final ListView list = myView.findViewById(R.id.customlistView);
        if(list == null){
            Log.i(TAG, "onCreateView: list == null");

        }
        list.setAdapter(adapter);
        //-----------------------------------------------------------------------------------
        // Ajout d'un listener sur la liste des favoris
        //-----------------------------------------------------------------------------------
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                manageItem((StationItem) adapterView.getItemAtPosition(position));
            }

            private void manageItem(StationItem itemAtPosition) {
                if(mListener != null){
                    mListener.onFavoritesFragmentInteraction(itemAtPosition);
                }
            }
        });


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
        void onFavoritesFragmentInteraction(StationItem itemAtPosition);
    }
}
