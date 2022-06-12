package com.aktechzone.propertydeal;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SingletonVolley {
    private final int MY_SOCKET_TIMEOUT_MS = 20 * 1000;
    private static SingletonVolley mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private SingletonVolley(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }


//    private String findErrorType(Context context, VolleyError volleyError) {
//        String errorString = "Server is not Responding";
//        if (volleyError instanceof NoConnectionError) {
//            errorString = context.getResources().getString(R.string.error_no_connection);
//        } else if (volleyError instanceof TimeoutError) {
//            errorString = context.getResources().getString(R.string.error_timeout);
//        } else if (volleyError instanceof AuthFailureError) {
//            errorString = context.getResources().getString(R.string.error_auth_failure);
//        } else if (volleyError instanceof ServerError) {
//            errorString = context.getResources().getString(R.string.error_auth_failure);
//        } else if (volleyError instanceof NetworkError) {
//            errorString = context.getResources().getString(R.string.error_network);
//        } else if (volleyError instanceof ParseError) {
//            errorString = context.getResources().getString(R.string.error_parser);
//        }
//        return errorString;
//    }
}
