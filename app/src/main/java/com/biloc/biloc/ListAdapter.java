package com.biloc.biloc;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by manon.racine1 on 05.11.2017.
 */

public class ListAdapter extends ArrayAdapter<StationList> {
    private ArrayList<StationList> androidVersionsList;
    private Context context;
    private int viewRes;
    private Resources res;

    public ListAdapter(Context context, int textViewResourceID, ArrayList<StationList> stationList){
        super(context, textViewResourceID, stationList);
        this.androidVersionsList=stationList;
        this.context=context;
        this.viewRes=textViewResourceID;
        this.res=context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(viewRes, parent, false);
        }

        final StationList androidVersion = androidVersionsList.get(position);

        if(androidVersion != null){
            final TextView station_Name = (TextView) view.findViewById(R.id.station_name);
            final TextView number_Bike = (TextView) view.findViewById(R.id.number_bike);
            final TextView distance = (TextView) view.findViewById(R.id.station_distance);

            final String numberOfBike = androidVersion.getNumberOfBike() + " " + res.getString(R.string.list_bike)  ;
            number_Bike.setText(numberOfBike);

            final String stationDistance = res.getString(R.string.list_distance) + " " + androidVersion.getStationDistance();
            distance.setText(stationDistance);

            final String stationName = res.getString(R.string.list_station) + " " + androidVersion.getStationName();
            station_Name.setText(stationName);
        }
        return view;

    }


}
