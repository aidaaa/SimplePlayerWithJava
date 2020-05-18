package com.example.simpleplayerwithjava;

import android.os.AsyncTask;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpClient extends AsyncTask<Void,Void,MutableLiveData<String>> {
    @Override
    protected MutableLiveData<String> doInBackground(Void... voids) {
        MutableLiveData<String> base_url=new MutableLiveData<>();

        try {
            URL url=new URL("http://192.168.10.40:1010");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            base_url.postValue("http://192.168.10.40:1010");

        } catch (Exception e) {
            e.printStackTrace();
            base_url.postValue(null);
        }
        return base_url;
    }
}
