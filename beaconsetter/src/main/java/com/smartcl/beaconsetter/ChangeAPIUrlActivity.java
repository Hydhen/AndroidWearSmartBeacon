package com.smartcl.beaconsetter;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ChangeAPIUrlActivity extends ActionBarActivity {

    TextView CurrentUrlTextView;
    EditText NewUrlTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_apiurl);
        CurrentUrlTextView = (TextView) findViewById(R.id.CURRENT_URL_TEXT);
        NewUrlTextField = (EditText) findViewById(R.id.URL_TEXTFIELD);
        SharedPreferences settings = getSharedPreferences("LclSmartbeaconPrefs", MODE_MULTI_PROCESS);
        CurrentUrlTextView.setText("Current Url : " + settings.getString("api_url", ""));
    }

    public void setUrl(View view) {
        SharedPreferences settings = getSharedPreferences("LclSmartbeaconPrefs", MODE_MULTI_PROCESS);
        settings.edit().putString("api_url", NewUrlTextField.getText().toString()).apply();
        CurrentUrlTextView.setText("Current Url : " + NewUrlTextField.getText().toString());
        Toast.makeText(this, "Url Set", Toast.LENGTH_SHORT).show();
    }
}
