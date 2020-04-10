package com.example.covid_19_updates;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.RequestCreator;

import java.math.BigInteger;

public class Country{

    private  String countryname;
    private Integer totalcase, totaldeaths, totalrecovered, activecases;
    private ImageView flag;
    private String flagUrl;

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public ImageView getFlag() {
        return flag;
    }

    public void setFlag(ImageView flag) {
        this.flag = flag;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public int getTotalcase() {
        return totalcase;
    }

    public void setTotalcase(int totalcase) {
        this.totalcase = totalcase;
    }

    public int getTotaldeaths() {
        return totaldeaths;
    }

    public void setTotaldeaths(int totaldeaths) {
        this.totaldeaths = totaldeaths;
    }

    public int getTotalrecovered() {
        return totalrecovered;
    }

    public void setTotalrecovered(int totalrecovered) {
        this.totalrecovered = totalrecovered;
    }

    public int getActivecases() {
        return activecases;
    }

    public void setActivecases(int activecases) {
        this.activecases = activecases;
    }

}
