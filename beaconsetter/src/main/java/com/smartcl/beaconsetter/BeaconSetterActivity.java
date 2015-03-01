package com.smartcl.beaconsetter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class BeaconSetterActivity extends ActionBarActivity {

    EditText       majorText;
    EditText       minorText;
    EditText       profileText;
    EditText       usecaseText;

    RequestQueue   requestQueue;
    ProgressBar    progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_setter);
        progressBar = (ProgressBar) findViewById(R.id.PROGRESS_BAR);
        majorText = (EditText) findViewById(R.id.MAJOR_TEXTFIELD);
        minorText = (EditText) findViewById(R.id.MINOR_TEXTFIELD);
        profileText = (EditText) findViewById(R.id.PROFILE_TEXTFIELD);
        usecaseText = (EditText) findViewById(R.id.USECASE_TEXTFIELD);
        progressBar.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void setBeacon(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String requestUrl = ApiBridge.getSetBeaconUrl(this,
                majorText.getText().toString(),
                minorText.getText().toString(),
                profileText.getText().toString(),
                usecaseText.getText().toString());
        Log.d(getClass().getSimpleName(), "Request url : " + requestUrl);
        requestQueue.add(new StringRequest(Request.Method.GET,
                requestUrl,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(BeaconSetterActivity.this.getClass().getSimpleName(), (String) o);
                        Toast.makeText(getApplicationContext(), "Beacon has been set", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(BeaconSetterActivity.this.getClass().getSimpleName(), "err");
                        Toast.makeText(getApplicationContext(), "Cannot set Beacon", Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }
}
