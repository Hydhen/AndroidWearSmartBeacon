package com.smartcl.beaconsetter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BeaconListActivity extends ActionBarActivity {

    ListView        listView;
    ProgressBar     progressBar;
    RequestQueue    requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);
        listView = (ListView) findViewById(R.id.BEAONS_LISTVIEW);
        progressBar = (ProgressBar) findViewById(R.id.PROGRESS_BAR);
        requestQueue = Volley.newRequestQueue(this);
        load();
    }

    private void load() {
        progressBar.setVisibility(View.VISIBLE);
        String requestUrl = ApiBridge.getAllBeaconUrl(this);
        requestQueue.add(new StringRequest(Request.Method.GET,
                requestUrl,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(this.getClass().getSimpleName(), (String) o);
                        try {
                            JSONArray response = new JSONArray((String) o);
                            String[] values = new String[response.length()];
                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject beacon = response.getJSONObject(i);
                                values[i] = beacon.getString("major") + "/" + beacon.getString("minor") + " - " + beacon.getString("profile");
                            }
                            listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values));
                        } catch (JSONException e) {
                            Log.e(getClass().getSimpleName(), e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(this.getClass().getSimpleName(), "Cannot get Beacons list");
                        Toast.makeText(getApplicationContext(), "Cannot get Beacons list", Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }
}
