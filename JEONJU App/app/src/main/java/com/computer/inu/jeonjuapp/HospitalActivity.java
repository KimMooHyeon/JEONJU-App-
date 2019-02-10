package com.computer.inu.jeonjuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class HospitalActivity extends AppCompatActivity {
    int idle=0;
    private final static String KEY="rXCtpQeVNRRd29DXomKbdrLow%2FTYIYSNLF0z6GTdlwOj95Y8ubqUom0qtcf19P9t47SweSbC9wOopuoXYJC0fg%3D%3D";
    private final static String[] ENTRY={"resultCode","resultMsg","pageIndex",
            "pageSize","startPage","totalCount", "mediAddr","mediCdb","mediCdbStr",
            "mediCdm","mediCdmStr","mediConDate", "mediEmergencyEtime","mediEmergencyStime","mediEtime",
            "mediHolidayEtime","mediHolidayStime","mediIsEmergency", "mediIsHoliday","mediName","mediSid",
            "mediStime","mediTel","memo", "posx","posy"};
    final private String[] TAP_TITLE={"일반","종합","외과","내과","치과"};
    final DbHelper dbHelper = new DbHelper(this,"TEST.db",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_tab_menu);
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("주변 병원");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorHospital)));
        actionBar.show();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorHospital));
        }
        Log.d("HOSPITAL_OK","진입OK");
        if(dbHelper.hospitalGetCount()<1319) {
            new LoadDataProgress().execute("");
        }
        TestPagerAdapter tesAdapter = new TestPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(tesAdapter);

        TabLayout tab = findViewById(R.id.hospitalTabs);
        tab.setupWithViewPager(viewPager);

    }
    private HashMap<String, Boolean> InitFlag(){
        HashMap<String, Boolean> newFlag=new HashMap<String, Boolean>();
        for(String element: ENTRY){
            newFlag.put(element, false);
        }
        return newFlag;
    }
    private HashMap<String, String> InitData(){
        HashMap<String, String> newData=new HashMap<String, String>();
        for(String element: ENTRY){
            newData.put(element, null);
        }
        return newData;
    }
    private void getHospitalInfo(DbHelper dbHelper){
        HashMap<String, Boolean> flag = InitFlag();
        HashMap<String, String> data = InitData();

        StrictMode.enableDefaults();

        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/medicalnew/getMedicalList?ServiceKey="
                    +KEY+"&mediCdt=1&startPage=1&pageSize=10000");
            XmlPullParserFactory parserCreator=XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream is=url.openStream();
            parser.setInput(is, null);
            Log.d("argcheck","inputStream: "+is.toString());
            int parserEvent=parser.getEventType();

            while(parserEvent!=XmlPullParser.END_DOCUMENT){

                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        flag.put(parser.getName(),true);
                        break;
                    case XmlPullParser.TEXT:
                        for(String element: ENTRY){
                            if(flag.get(element)){
                                data.put(element, parser.getText());
                                flag.put(element, false);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("list")){
                            dbHelper.hospitalInsert(data.get("mediName"),data.get("mediAddr"),data.get("mediCdmStr"),
                                    data.get("mediTel"),data.get("posx"),data.get("posy"),
                                    data.get("mediConDate"),data.get("mediStime"),data.get("mediEtime"),
                                    data.get("mediIsEmergency"),data.get("mediEmergencyStime"),data.get("mediEmergencyEtime"),
                                    data.get("mediIsHoliday"),data.get("mediHolidayStime"),data.get("mediHolidayEtime"),
                                    data.get("memo"),Integer.parseInt(data.get("mediSid")));
                        }
                        break;
                }
                parserEvent=parser.next();
            }
        }catch(Exception e){
            String buffer=null;
            StackTraceElement[] elements = e.getStackTrace();
            for(StackTraceElement element: elements){
                String className = element.getClassName();
                String methodName = element.getMethodName();
                String fileName=element.getFileName();
                int lineNumber=element.getLineNumber();
                buffer = buffer+"\n\nelement:"+element+
                        "\nclassName:"+className+
                        "\nmethodName:"+methodName+
                        "\nfileName:"+fileName+
                        "\nlineNumber:"+lineNumber;
                Log.e("MyException",buffer);
            }
        }
    }
    private void getPharmacyInfo(DbHelper dbHelper){
        HashMap<String, Boolean> flag = InitFlag();
        HashMap<String, String> data = InitData();

        StrictMode.enableDefaults();

        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/medicalnew/getMedicalList?ServiceKey="
                    +KEY+"&mediCdt=2&startPage=1&pageSize=10000");
            XmlPullParserFactory parserCreator=XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream is=url.openStream();
            parser.setInput(is, null);
            Log.d("argcheck","inputStream: "+is.toString());
            int parserEvent=parser.getEventType();

            while(parserEvent!=XmlPullParser.END_DOCUMENT){

                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        flag.put(parser.getName(),true);
                        break;
                    case XmlPullParser.TEXT:
                        for(String element: ENTRY){
                            if(flag.get(element)){
                                data.put(element, parser.getText());
                                flag.put(element, false);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("list")){
                            dbHelper.hospitalInsert(data.get("mediName"),data.get("mediAddr"),data.get("mediCdmStr"),
                                    data.get("mediTel"),data.get("posx"),data.get("posy"),
                                    data.get("mediConDate"),data.get("mediStime"),data.get("mediEtime"),
                                    data.get("mediIsEmergency"),data.get("mediEmergencyStime"),data.get("mediEmergencyEtime"),
                                    data.get("mediIsHoliday"),data.get("mediHolidayStime"),data.get("mediHolidayEtime"),
                                    data.get("memo"),Integer.parseInt(data.get("mediSid")));
                        }
                        break;
                }
                parserEvent=parser.next();
            }
        }catch(Exception e){
            String buffer=null;
            StackTraceElement[] elements = e.getStackTrace();
            for(StackTraceElement element: elements){
                String className = element.getClassName();
                String methodName = element.getMethodName();
                String fileName=element.getFileName();
                int lineNumber=element.getLineNumber();
                buffer = buffer+"\n\nelement:"+element+
                        "\nclassName:"+className+
                        "\nmethodName:"+methodName+
                        "\nfileName:"+fileName+
                        "\nlineNumber:"+lineNumber;
                Log.e("MyException",buffer);
            }
        }
    }
    private class TestPagerAdapter extends FragmentStatePagerAdapter {
        public TestPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i){
                case 0:
                    return HospitalFragment.newInstance(new String[]{"일반병원"});
                case 1:
                    return HospitalFragment.newInstance(new String[]{"종합병원"});
                case 2:
                    return HospitalFragment.newInstance(new String[]{"외과"});
                case 3:
                    return HospitalFragment.newInstance(new String[]{"내과"});
                case 4:
                    return HospitalFragment.newInstance(new String[]{"치과", "치과병원", "치과의원"});
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return TAP_TITLE[position];
                case 1:
                    return TAP_TITLE[position];
                case 2:
                    return TAP_TITLE[position];
                case 3:
                    return TAP_TITLE[position];
                case 4:
                    return TAP_TITLE[position];
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
    private class LoadDataProgress extends AsyncTask<String, Void, Boolean>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HospitalActivity.this);
            pDialog.setTitle("데이터베이스 구성중");
            pDialog.setMessage("네트워크 상태에 따라 시간이 소요됩니다...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String ...strings) {
            try {
                while(dbHelper.hospitalGetCount() == 0) {
                    getHospitalInfo(dbHelper);
                    getPharmacyInfo(dbHelper);
                }
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            pDialog.dismiss();
            if(!isSuccess) Toast.makeText(HospitalActivity.this,"데이터베이스 구성에 실패하였습니다.",Toast.LENGTH_SHORT).show();
            else{
                Intent intent=HospitalActivity.this.getIntent();
                HospitalActivity.this.finish();
                startActivity(intent);
            }
        }
    }

}
