package com.computer.inu.jeonjuapp;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

public class GetLocationinfoAsyncTask extends AsyncTask<String,String,String>{
    ShowPathActivity showPathActivity = null;
    GetLocationManager getLocationManager = null;
    String getLocation = "";
    public GetLocationinfoAsyncTask(HashMap<String,String> reqLocation,ShowPathActivity showPathActivity){
        this.showPathActivity=showPathActivity;
        getLocationManager = new GetLocationManager((reqLocation));

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
       getLocation =  getLocationManager.connect();
       Log.i("json결과",""+getLocation);
        return getLocation;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        showPathActivity.setLocation(s); // 현재위치 api에서 받아서 지정
    }
}
