package com.example.covid_19_updates;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;

public class FindMe extends AppCompatActivity {
    public RequestQueue mQueue;
    public Button generateBtn;
    public static TextView ipDisplay;
    private ArrayList<Country> arrayList;
    private CountryAdapter adapter;
    public TextView countryName;
    private String country = "";
    public String ipAdress = "";
    public Button correctBtn;
    public Button incorrectBtn;
    public ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me);
        mQueue = Volley.newRequestQueue(FindMe.this);

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<Country>();

        correctBtn = findViewById(R.id.correctBtn);
        incorrectBtn = findViewById(R.id.incorrectBtn);
        countryName = findViewById(R.id.countryNameTxtView);
        generateBtn = findViewById(R.id.generateBtn);
        ipDisplay = findViewById(R.id.ipTxtView);

        final FetchDataClass fetchData = new FetchDataClass();
        fetchData.execute();
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipDisplay.setText(fetchData.getIp());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ipAdress = fetchData.getIp();
                Toast.makeText(FindMe.this, ipAdress, Toast.LENGTH_LONG).show();
               // String url = "https://tools.keycdn.com/geo.json?host=";
                parseJson("https://tools.keycdn.com/geo.json?host=", ipAdress);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                correctBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FindMe.this, "We are loading information...", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Toast.makeText(FindMe.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
//                        String url = "https://corona.lmao.ninja/countries/";
                        try {
                            countryFetch("https://corona.lmao.ninja/countries/");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

                incorrectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FindMe.this, "Sorry we cannot find your location.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });


    }



    private void parseJson(String url, String ip){
        Toast.makeText(FindMe.this, "Please wait we are calculating..." ,Toast.LENGTH_LONG).show();
//        String finalUrl = url.concat(ip);
        //Toast.makeText(FindMe.this, url+ip ,Toast.LENGTH_LONG).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+ip, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String data = "";
                try {
                    JSONObject nestedObj = response.getJSONObject("data");
                    JSONObject geo = nestedObj.getJSONObject("geo");
                    country = geo.getString("country_name");
                    countryName.setText(country);
                    correctBtn.setVisibility(View.VISIBLE);
                    incorrectBtn.setVisibility(View.VISIBLE);
                    Thread.sleep(1000);
                } catch (JSONException | InterruptedException e) {
                    countryName.setText(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countryName.setText(error.getMessage());            }
        });
        mQueue.add(request);
    }

    public void countryFetch(String url) throws InterruptedException {
        Thread.sleep(2000);
        try{
            //String finalUrl = url + countryName.getText().toString() +"?strict=true";
            Toast.makeText(FindMe.this, url + countryName.getText().toString(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(FindMe.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
