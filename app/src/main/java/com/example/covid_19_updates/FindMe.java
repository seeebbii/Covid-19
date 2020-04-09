package com.example.covid_19_updates;

import androidx.appcompat.app.AppCompatActivity;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    public TextView fetchedInfo;
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
        fetchedInfo = findViewById(R.id.fetchedInfo);
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
//                        Toast.makeText(FindMe.this, "We are loading information...", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(100);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PrettyDialog prettyDialog =  new PrettyDialog(FindMe.this);
                prettyDialog.setTitle(adapter.getItem(position).getCountryname())
                        .setMessage("Total Cases: " + adapter.getItem(position).getTotalcase() + "\n" + "Total Deaths: " + adapter.getItem(position).getTotaldeaths()
                                + "\n" + "Active Cases: " + adapter.getItem(position).getActivecases() + "\n" + "Total Recovered: " + adapter.getItem(position).getTotalrecovered())
                        .setIcon(R.drawable.searchbutton)
                        .addButton(
                                "OK",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.pdlg_color_green,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {
                                        // Do what you gotta do
                                        prettyDialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        });


    }



    private void parseJson(String url, String ip){
        Toast.makeText(FindMe.this, "Please wait we are calculating..." ,Toast.LENGTH_SHORT).show();
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
            for(int i = 0; i < CountryList.c.length; i++){
                if(countryName.getText().toString().equals(CountryList.c[i].getCountryname())){
                    arrayList.add(CountryList.c[i]);
                    fetchedInfo.setVisibility(View.VISIBLE);
                }
            }
            adapter = new CountryAdapter(FindMe.this, arrayList);
            listView.setAdapter(adapter);
        }catch (Exception e){
            final PrettyDialog prettyDialog =  new PrettyDialog(FindMe.this);
            prettyDialog.setTitle("Stupid!")
                    .setMessage("You have to search for your country at least once, don't waste my time...!")
                    .setIcon(R.drawable.searchbutton)
                    .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_green,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    // Do what you gotta do
                                    prettyDialog.dismiss();
                                }
                            }
                    )
                    .show();
            //Toast.makeText(FindMe.this, "You have to search for your country once at least, don't waste my time...!", Toast.LENGTH_LONG).show();
        }

    }
}
