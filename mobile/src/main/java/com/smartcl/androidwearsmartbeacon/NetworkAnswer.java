package com.smartcl.androidwearsmartbeacon;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by bourdi_bay on 21/02/2015.
 */
public class NetworkAnswer implements Response.Listener, Response.ErrorListener {

    protected NetworkOperation _network;

    public NetworkAnswer(NetworkOperation network) {
        _network = network;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        run(response);
    }

    public void run(Object response) {
    }
}
