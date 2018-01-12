package com.biloc.biloc;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.Collections;

class Utils {

    private static final String TAG = "testBiloc";

    //-----------------------------------------------------------------------------------
    // Partie requête au serveur pour la récupération des stations
    //-----------------------------------------------------------------------------------
    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result);
    }

    static void processRequest(final Context ctxt, int method,
                               JSONObject jsonValue, final VolleyCallback callback){
        String url = "http://54.186.104.143/stations/v2";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (method, url, jsonValue, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "error: " + error);
                    }
                });

        SingletonRequestQueue singletonRequestQueue = new SingletonRequestQueue(ctxt);
        SingletonRequestQueue.getInstance(ctxt).addToRequestQueue(jsObjRequest);
    }

    //-----------------------------------------------------------------------------------
    // Triage des stations selon
    //-----------------------------------------------------------------------------------
    public static ArrayList<StationItem> orderStationList(ArrayList<StationItem> stationList) {

        Collections.sort(stationList);

        return stationList;
    }
}
