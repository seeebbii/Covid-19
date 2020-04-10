package com.example.covid_19_updates;

import androidx.appcompat.app.AppCompatActivity;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
    public Country c;

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
        fetchedInfo = findViewById(R.id.fetchedInfo);
        ipDisplay = findViewById(R.id.ipTxtView);

        final FetchDataClass fetchData = new FetchDataClass();
        fetchData.execute();

        try {
            Thread.sleep(2000);
            Toast.makeText(FindMe.this, "We are calculating information...", Toast.LENGTH_LONG).show();
            ipDisplay.setText(fetchData.getIp());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
            ipAdress = fetchData.getIp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        parseJson("https://tools.keycdn.com/geo.json?host=", ipAdress);


        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FindMe.this, "We are loading information...", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Toast.makeText(FindMe.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
//                        String url = "https://corona.lmao.ninja/countries/";
                countryFetch("https://corona.lmao.ninja/countries/");

            }
        });

        incorrectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FindMe.this, "Sorry we cannot find your location.", Toast.LENGTH_SHORT).show();
                finish();
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
                    Thread.sleep(500);
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

    public void countryFetch(String Url) {
        String url = Url + countryName.getText().toString();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                c = new Country();
                try {
                    c.setCountryname(response.getString("country"));
                    JSONObject nestedObj = response.getJSONObject("countryInfo");
                    c.setFlagUrl(nestedObj.getString("flag"));
                    c.setTotalcase(Integer.parseInt(response.getString("cases")));
                    c.setTotaldeaths(Integer.parseInt(response.getString("deaths")));
                    c.setTotalrecovered(Integer.parseInt(response.getString("recovered")));
                    c.setActivecases(Integer.parseInt(response.getString("active")));
                    arrayList.add(c);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new CountryAdapter(FindMe.this, arrayList);
                fetchedInfo.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }
}
