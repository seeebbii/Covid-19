package com.example.covid_19_updates;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountryList extends AppCompatActivity {

    private RequestQueue mQueue;
    private ListView listView;
    private ArrayList<Country> arrayList;
    private CountryAdapter adapter;
    public static Country[] c = new Country[Integer.parseInt((String) MainActivity.affectedCountries.getText())];
    public  ProgressDialog dialog;
    public  SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        listView = (ListView) findViewById(R.id.theList);
        arrayList = new ArrayList<Country>();
        // IGNORE ALL OF THIS
        mQueue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/v2/countries";
        jsonArrReqString(url);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PrettyDialog prettyDialog =  new PrettyDialog(CountryList.this);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearchBtn);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                ArrayList<Country> filteredList = new ArrayList<>();
                for (int i = 0 ; i < c.length; i++){
                    if(c[i].getCountryname().toLowerCase().startsWith(newText.toLowerCase())){
                        Log.i("countryName", c[i].getCountryname() + " " + c[i].getItemId());
                        filteredList.add(c[i]);
                    }
                }
                final CountryAdapter anotherAdapter = new CountryAdapter(CountryList.this, filteredList);
                listView.setAdapter(anotherAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final PrettyDialog prettyDialog =  new PrettyDialog(CountryList.this);
                        prettyDialog.setTitle(anotherAdapter.getItem(position).getCountryname())
                                .setMessage("Total Cases: " + anotherAdapter.getItem(position).getTotalcase() + "\n" + "Total Deaths: " + anotherAdapter.getItem(position).getTotaldeaths()
                                        + "\n" + "Active Cases: " + anotherAdapter.getItem(position).getActivecases() + "\n" + "Total Recovered: " + anotherAdapter.getItem(position).getTotalrecovered())
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
                return false;
            }
        });
        return true;
    }

    private void jsonArrReqString(String url) {
        final ProgressDialog dialog = new ProgressDialog(CountryList.this);
        dialog.setMessage("Loading please wait...");
        dialog.show();
        final RequestCreator[] tempImage = new RequestCreator[1];
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject nestedObj = response.getJSONObject(i);
                        JSONObject countryInfo = nestedObj.getJSONObject("countryInfo");
                        c[i] = new Country();
                        c[i].setItemId(i);
                        c[i].setFlagUrl(countryInfo.getString("flag"));
                        c[i].setTotalcase(Integer.parseInt(nestedObj.getString("cases")));
                        c[i].setActivecases(Integer.parseInt(nestedObj.getString("active")));
                        c[i].setCountryname(nestedObj.getString("country"));
                        c[i].setTotalrecovered(Integer.parseInt(nestedObj.getString("recovered")));
                        c[i].setTotaldeaths(Integer.parseInt(nestedObj.getString("deaths")));

                        arrayList.add(c[i]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                adapter = new CountryAdapter(CountryList.this, arrayList);
                listView.setAdapter(adapter);
               sortListView();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CountryList.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);

    }

    private void sortListView() {
        Country temp;
        arrayList.clear();
        listView.setAdapter(adapter);
        try{
            char letterOfFirst ;
            char letterOfSecond ;
            for(int i = 0 ; i < c.length; i++){
                for(int j = i + 1; j < c.length; j++){
                    letterOfFirst = c[i].getCountryname().charAt(0);
                    letterOfSecond = c[j].getCountryname().charAt(0);
                    if(letterOfFirst > letterOfSecond ){
                        temp = c[i];
                        c[i] = c[j];
                        c[j] = temp;
                    }
                }
            }
            for (int i = 0; i < c.length; i++){
                arrayList.add(c[i]);
                //Toast.makeText(CountryList.this, c[i].getCountryname(),Toast.LENGTH_LONG).show();

            }
            dialog.dismiss();
            listView.setAdapter(adapter);
        } catch (Exception e){
            //Toast.makeText(CountryList.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
