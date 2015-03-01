package com.smartcl.communicationlibrary;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Get the server answer and run something accordingly.
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
