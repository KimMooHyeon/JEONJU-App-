package com.computer.inu.jeonjuapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GetLocationManager {

    URL url = null; // url 주소로 프로토콜, 포트 ,경로 등을 나타낸다.
    HttpURLConnection httpURLConnection = null;
    BufferedReader bufferedReader = null; // 문자입력 스트림, 문자를 읽어들이거나 문자를 출력 스트림을 보낼때 버퍼링을 함으로써 문자, 문자열 등을 보다 효율적으로 처리할수있게 도와주는 객체
    HashMap<String,String> reqLocation = null;

    static final String REQUEST_LOCATION_URL = "https://api2.sktelecom.com/tmap/pois?version=1&appKey=30c2489c-3d2e-475f-8151-322374c27ed6&count=10&callback=application/json&";   // URL고정된값이므로 상수로 선언
    String params = "";
    String data = "";

    public GetLocationManager(HashMap<String,String> reqLocation){
        this.reqLocation = reqLocation;
        String searchKeyword = reqLocation.get("searchKeyword").toString();
        String searchType=reqLocation.get("searchType").toString();

        params = "searchKeyword="+searchKeyword+"&searchType="+searchType;
        Log.i("checkMyURL",""+params);

        try {
            url = new URL(REQUEST_LOCATION_URL+params);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
            httpURLConnection.setRequestProperty("Accept","application/json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String connect(){

        String line = "";

        try {
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while((line=bufferedReader.readLine())!=null){
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  data;
        }
    }

}
