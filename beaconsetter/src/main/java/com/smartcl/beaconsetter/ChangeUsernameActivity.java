package com.smartcl.beaconsetter;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChangeUsernameActivity extends ActionBarActivity {

    TextView currentName;
    ListView listView;
    ProgressBar progressBar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        listView = (ListView) findViewById(R.id.BEAONS_LISTVIEW);
        progressBar = (ProgressBar) findViewById(R.id.PROGRESS_BAR);
        currentName = (TextView) findViewById(R.id.CURRENT_NAME_TEXTVIEW);
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences settings = getSharedPreferences("LclSmartbeaconPrefs", MODE_MULTI_PROCESS);
        currentName.setText("Current Name : " + settings.getString("name", ""));
        load();
    }

    private void load() {
        progressBar.setVisibility(View.VISIBLE);
        String requestUrl = ApiBridge.getAllUsersUrl(this);
        requestQueue.add(new StringRequest(Request.Method.GET,
                requestUrl,
                new Response.Listener() {
                    @Override
                    public void onResponse(final Object o) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(ChangeUsernameActivity.this.getClass().getSimpleName(), (String) o);
                        try {
                            final JSONArray response = new JSONArray((String) o);
                            String[] values = new String[response.length()];
                            for (int i = 0; i < response.length(); i++)
                            {
                                JSONObject user = response.getJSONObject(i);
                                values[i] = user.getString("name") + " - " + user.getString("status");
                            }
                            listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        setUsername(response.getJSONObject(position).getString("name"));
                                    } catch (JSONException e) {
                                        Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            Log.e(ChangeUsernameActivity.this.getClass().getSimpleName(), e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(ChangeUsernameActivity.this.getClass().getSimpleName(), "Cannot get Beacons list");
                        Toast.makeText(getApplicationContext(), "Cannot get Beacons list", Toast.LENGTH_SHORT).show();
                    }
                }
        ));
    }

    public void setUsername(String name) {
        SharedPreferences settings = getSharedPreferences("LclSmartbeaconPrefs", MODE_WORLD_READABLE);
        settings.edit().putString("name", name).apply();
        Toast.makeText(this, "Name set to " + name, Toast.LENGTH_SHORT).show();
        currentName.setText("Current Name : " + name);
    }
}
