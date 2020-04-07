package com.example.covid_19_updates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country>
{
    private Context context;
    private ArrayList<Country> list;

    public CountryAdapter(Context context, ArrayList<Country> countries)
    {
        super(context, R.layout.demo_layour, countries);
        this.context = context;
        this.list = countries;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.demo_layour,parent , false);
        TextView countryname = convertView.findViewById(R.id.countryname);
        ImageView img = convertView.findViewById(R.id.imgview);

        countryname.setText(list.get(position).getCountryname());

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Country getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
