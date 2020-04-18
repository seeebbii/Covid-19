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
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView totalCases, totalDeaths, totalRecovered, activeCases;
    public static TextView affectedCountries;
    private RequestQueue mQueue;
    private Button searchButton;
    private Button findBtn;
    public NumberFormat myFormat;

    // Update able variables
    int tCase;
    int tDeath;
    int tRec;
    int active;
    int affCountries;

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

        myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        searchButton =  findViewById(R.id.searchButton);

        mQueue = Volley.newRequestQueue(this);

        jsonParse("https://corona.lmao.ninja/v2/all");
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
                            tCase = response.getInt("cases");
                            tDeath = response.getInt("deaths");
                            tRec = response.getInt("recovered");
                            active = response.getInt("active");
                            affCountries = response.getInt("affectedCountries");
                            // Updating
                            totalCases.setText(myFormat.format(tCase));
                            totalDeaths.setText(myFormat.format(tDeath));
                            totalRecovered.setText(myFormat.format(tRec));
                            activeCases.setText(myFormat.format(active));
                            affectedCountries.setText(myFormat.format(affCountries));

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
