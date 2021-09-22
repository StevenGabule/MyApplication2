package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    @SuppressLint("StaticFieldLeak")
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }

}
