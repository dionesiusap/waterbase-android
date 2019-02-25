package com.example.waterbase.pbd;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpRequestApplication extends Application {
    private RequestQueue requestQueue;
    private static HttpRequestApplication mInstance;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized HttpRequestApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelRequests(String tag) {
        getRequestQueue().cancelAll(tag);
    }
}
