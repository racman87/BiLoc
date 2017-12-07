package com.biloc.biloc;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * Created by manon.racine1 on 05.12.2017.
 */

class Utils {

    private static final String TAG = "testResponse";

    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result);
    }

    static void processRequest(final Context ctxt, String server, String port , String ressource, int method,
                               JSONObject jsonValue, final VolleyCallback callback){

        String url = server + port + "/" + ressource;

        String testurl = "https://postman-echo.com/get?test=123";

        Log.i(TAG, "processRequest -> URL: "+url);

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

        // Access the RequestQueue through your singleton class.
        SingletonRequestQueue singletonRequestQueue = new SingletonRequestQueue(ctxt);
        SingletonRequestQueue.getInstance(ctxt).addToRequestQueue(jsObjRequest);
    }
}
