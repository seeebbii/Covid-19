package com.example.covid_19_updates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView totalCases, totalDeaths, totalRecovered, activeCases, affectedCountries;
    private RequestQueue mQueue;
    private ImageButton searchButton;
    private Button findBtn;

    // Update able variables
    String tCase, tDeath, tRec, active, affCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findBtn = findViewById(R.id.findBtn);
        totalCases = findViewById(R.id.totalCases);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalRecovered = findViewById(R.id.totalRecovered);
        activeCases = findViewById(R.id.activeCases);
        affectedCountries = findViewById(R.id.affectedCountries);

        searchButton = findViewById(R.id.searchButton);

        mQueue = Volley.newRequestQueue(this);

        jsonParse("https://corona.lmao.ninja/all");
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountryList.class);
                startActivity(intent);
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindMe.class);
                startActivity(intent);
            }
        });


    }

    private void jsonParse(String URL){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            tCase = response.getString("cases");
                            tDeath = response.getString("deaths");
                            tRec = response.getString("recovered");
                            active = response.getString("active");
                            affCountries = response.getString("affectedCountries");
                            // Updating
                            totalCases.setText(tCase);
                            totalDeaths.setText(tDeath);
                            totalRecovered.setText(tRec);
                            activeCases.setText(active);
                            affectedCountries.setText(affCountries);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }
}
