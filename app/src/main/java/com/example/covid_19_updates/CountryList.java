package com.example.covid_19_updates;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CountryList extends AppCompatActivity {

    private RequestQueue mQueue;
    private ListView listView;
    private ArrayList<Country> arrayList;
    private ArrayAdapter arrayAdapter;
    private String[] countryNames;
    private CountryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        listView = (ListView) findViewById(R.id.theList);
        arrayList = new ArrayList<Country>();

//        arrayList.add("Hey");
//        arrayList.add("this");
//        arrayList.add("is");
//        arrayList.add("just");
//        arrayList.add("for");
//        arrayList.add("testing");
//        arrayAdapter = new ArrayAdapter(CountryList.this, android.R.layout.simple_list_item_1, arrayList);
//        listView.setAdapter(arrayAdapter);


        // IGNORE ALL OF THIS
        mQueue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/countries";
        jsonArrReqString(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearchBtn);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void jsonArrReqString(String url) {
        final ProgressDialog dialog = new ProgressDialog(CountryList.this);
        dialog.setMessage("Loading please wait...");
        dialog.show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject nestedObj = response.getJSONObject(i);
//                        JSONObject countryInfo = nestedObj.getJSONObject("countryInfo");
//                        Toast.makeText(MainActivity.this, countryInfo.getString("iso3"), Toast.LENGTH_SHORT).show();
                        //arrayList.add(nestedObj.getString("country"));
                        Country c = new Country();
                        c.setCountryname(nestedObj.getString("country"));
                        arrayList.add(c);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               // Collections.sort(arrayList);
                dialog.dismiss();
               // arrayAdapter = new ArrayAdapter(CountryList.this, android.R.layout.simple_list_item_1, arrayList);
                //listView.setAdapter(arrayAdapter);
                adapter = new CountryAdapter(CountryList.this, arrayList);
                listView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //data.setText(error.getMessage());
            }
        });
        mQueue.add(request);
    }

}
