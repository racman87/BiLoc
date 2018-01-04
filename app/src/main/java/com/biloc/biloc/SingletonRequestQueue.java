package com.biloc.biloc;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonRequestQueue {
    private static SingletonRequestQueue ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public static SingletonRequestQueue getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SingletonRequestQueue(context);
            mCtx = context;
        }
        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    SingletonRequestQueue(Context context) {
    }
}
