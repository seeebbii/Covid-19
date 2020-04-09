package com.example.covid_19_updates;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchDataClass extends AsyncTask<Void, Void, Void> {
    public String data;
    public String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://ident.me/");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = reader.readLine();
                data += line;
            }
            ip = data.replaceAll("[^0-9.]", "");
            setIp(ip);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //FindMe.ipDisplay.setText(ip);
    }


}

