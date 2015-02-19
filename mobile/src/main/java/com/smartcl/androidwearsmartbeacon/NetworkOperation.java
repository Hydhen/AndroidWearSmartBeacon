package com.smartcl.androidwearsmartbeacon;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by bourdi_b on 19/02/2015.
 */
public class NetworkOperation {

    private RequestQueue _queue;

    public NetworkOperation(Context context) {
        _queue = Volley.newRequestQueue(context);
    }

    public void operationGet(final String url, Response.Listener<JSONObject> correctResponseListener,
                             Response.ErrorListener errorResponseListener) {
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                                             correctResponseListener, errorResponseListener);

        _queue.add(getRequest);
    }
}
