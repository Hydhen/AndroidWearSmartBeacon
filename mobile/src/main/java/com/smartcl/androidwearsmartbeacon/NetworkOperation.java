package com.smartcl.androidwearsmartbeacon;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Perform a server request.
 */
public class NetworkOperation {

    private RequestQueue _queue;

    public NetworkOperation(Context context) {
        _queue = Volley.newRequestQueue(context);
    }

    public void operationGet(final String url, NetworkAnswer answer) {

        StringRequest request = new StringRequest(Request.Method.GET, url,
                                                  answer, answer);
        _queue.add(request);
    }
}
