package com.smartcl.communicationlibrary;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Perform a server request.
 */
public class NetworkOperation {

    private RequestQueue _queue;
    private String _serverUrl;

    public NetworkOperation(Context context, String serverUrl) {
        _serverUrl = serverUrl;
        _queue = Volley.newRequestQueue(context);
    }

    public String getApiUrl() {
        return _serverUrl;
    }

    public void operationGet(final String url, NetworkAnswer answer) {

        StringRequest request = new StringRequest(Request.Method.GET, url,
                                                  answer, answer);
        _queue.add(request);
    }
}
