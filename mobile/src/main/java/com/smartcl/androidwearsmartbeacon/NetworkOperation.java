package com.smartcl.androidwearsmartbeacon;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by bourdi_b on 19/02/2015.
 */
public class NetworkOperation {

    private RequestQueue _queue;

    public NetworkOperation(Context context) {
        _queue = Volley.newRequestQueue(context);
    }

    static String getApiUrl() {
        return "http://ec2-54-93-111-136.eu-central-1.compute.amazonaws.com:21996/";
    }

    public static String getAskQuestionUrl() {
        return getApiUrl() + "question/ask?status=student";
    }

    public void operationGet(final String url, Response.Listener correctResponseListener,
                             Response.ErrorListener errorResponseListener) {

        StringRequest request = new StringRequest(Request.Method.GET, url,
                                                        correctResponseListener,
                                                        errorResponseListener);
        _queue.add(request);
    }
}
