package com.computer.inu.jeonjuapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

// 키값 : d3a6fa0f-3aa3-44a4-ad4d-8ccb72266547

public class ShowPathActivity extends AppCompatActivity {

    ImageButton btnStartSearch;
    ImageButton btnDestinationSearch;
    Button btnStartLocationWeb;
    Button btnEndLocationWeb;
    ImageButton btnCurrent;
    Button btnBigMap;
    Button btnSmallMap;
    Button btnPathSearch;
    ImageButton btnAround;
    Button btnCircle;
    Button btnLine;
    ImageButton btnCar;
    ImageButton btnPath;
    ImageButton ibtnPlus;
    ImageButton ibtnMinus;
    TMapView tMapView;
    TMapData tMapdata;
    String name;
    private String address;
    int MarkerId;
    String[] addr;
    TMapPolyLine tpolyline = new TMapPolyLine();
    TMapMarkerItem tMapMarkerItem;
    GetLocationinfoAsyncTask getLocationinfoAsyncTask = null;
    private double latitude;
    private double longitude;
    int check = 0;
    Bitmap bitmap;
    String str="";

    EditText edtStart;
    EditText edtDestination;

    RelativeLayout relMapView;
    LocationManager locationManager;
    TMapPOIItem tMapStartPOIItem;
    TMapPOIItem item;
    TMapPOIItem tMapDestinationPOIItem;
    TMapCircle tcircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_path);

        btnCar=(ImageButton) findViewById(R.id.btnCar);
        btnPath=(ImageButton)findViewById(R.id.btnPath);
        btnStartSearch = (ImageButton) findViewById(R.id.btnStartSearch);
        btnDestinationSearch = (ImageButton) findViewById(R.id.btnDestinationSearch);
        btnCurrent = (ImageButton) findViewById(R.id.btnCurrent);
        bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.arrow);

        // btnBigMap = (Button) findViewById(R.id.btnBigMap);
        // btnSmallMap = (Button) findViewById(R.id.btnSmallMap);
        //btnPathSearch = (Button) findViewById(R.id.btnPathSearch);
        //  btnCircle = (Button) findViewById(R.id.btnCircle);
        //  btnLine = (Button) findViewById(R.id.btnLine);
        ibtnPlus = (ImageButton)findViewById(R.id.ibtnPlus);
        ibtnMinus = (ImageButton)findViewById(R.id.ibtnMinus);
        btnAround = (ImageButton)findViewById(R.id.btnAround);
        tMapView = new TMapView(this);
        edtStart = (EditText) findViewById(R.id.edtStart);
        edtDestination = (EditText) findViewById(R.id.edtDestination);
        // btnStartLocationWeb = (Button) findViewById(R.id.btnStartLocationWeb);
        // btnEndLocationWeb = (Button) findViewById(R.id.btnEndLocationWeb);
        tcircle = new TMapCircle();
        tMapdata = new TMapData();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        relMapView = (RelativeLayout) findViewById(R.id.relMapView);
        tMapView.setSKTMapApiKey("30c2489c-3d2e-475f-8151-322374c27ed6");
        tMapView.setIconVisibility(true);
        relMapView.addView(tMapView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
            return;
        }
        setGps();


        btnCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try

                        {
                            TMapPolyLine polyLine = tMapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapStartPOIItem.getPOIPoint(), tMapDestinationPOIItem.getPOIPoint());
                            polyLine.setLineWidth(10);
                            tMapView.addTMapPolyLine("line1", polyLine);
                            tMapView.setCenterPoint((longitude + tMapDestinationPOIItem.getPOIPoint().getLongitude()) / 2, (latitude + tMapDestinationPOIItem.getPOIPoint().getLatitude()) / 2);
                            tMapView.setZoomLevel(10);
                        } catch (
                                Exception e)

                        {
                            e.getStackTrace();
                        }
                    }
                }.start();


                /*tMapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, strPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine tMapPolyLine) {

                        tMapView.addTMapPath(tMapPolyLine);
                        tMapView.setCenterPoint((longitude+endPoint.getLongitude())/2,(latitude+endPoint.getLatitude())/2);
                        tMapView.setZoomLevel(10);
                        tMapView.

                    }
                });*/
            }
        });

        btnPath.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try

                        {
                            TMapPolyLine polyLine = tMapdata.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapStartPOIItem.getPOIPoint(), tMapDestinationPOIItem.getPOIPoint());
                            polyLine.setLineWidth(10);
                            tMapView.addTMapPolyLine("line1", polyLine);
                            tMapView.setCenterPoint((longitude + tMapDestinationPOIItem.getPOIPoint().getLongitude()) / 2, (latitude + tMapDestinationPOIItem.getPOIPoint().getLatitude()) / 2);
                            tMapView.setZoomLevel(12);
                        } catch (
                                Exception e)

                        {
                            e.getStackTrace();
                        }
                    }
                }.start();

            }
        });

        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0) {
                    try {
                        setGps();
                        Toast.makeText(ShowPathActivity.this, "현재위치 탐색 시작", Toast.LENGTH_SHORT).show();
                        check++;
                    } catch (SecurityException e) {
                        Toast.makeText(ShowPathActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                } else if (check == 1) {
                    tMapView.setCompassMode(true);
                    Toast.makeText(ShowPathActivity.this, "컴퍼스 모드 시작", Toast.LENGTH_SHORT).show();
                    check++;
                } else {
                    tMapView.setCompassMode(false);
                    locationManager.removeUpdates(mLocationListener);
                    Toast.makeText(ShowPathActivity.this, "컴퍼스 모드 및 현재위치 탐색 종료", Toast.LENGTH_SHORT).show();
                    check = 0;
                }
            }
        });
        btnAround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAroundPOI();

            }
        });




        ibtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomIn();
            }
        });

        ibtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapView.MapZoomOut();
            }
        });




        btnStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edtStart.getText().toString();

              try {
                  if(str.equals("")) {
                      Toast.makeText(ShowPathActivity.this, "출발지를 입력하세요.", Toast.LENGTH_SHORT).show();
                  }else{
                  tMapdata.findAllPOI("" + str, 1, new TMapData.FindAllPOIListenerCallback() {
                  @Override
                  public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                      tMapStartPOIItem = (TMapPOIItem) arrayList.get(0);
                      Log.i("검색결과", "" + tMapStartPOIItem.getPOIAddress() + "|" + tMapStartPOIItem.getPOIName() + "|" + tMapStartPOIItem.getPOIContent() + "|" + tMapStartPOIItem.getPOIPoint());
                      tMapMarkerItem = new TMapMarkerItem();
                      tMapMarkerItem.setTMapPoint(tMapStartPOIItem.getPOIPoint());

                      tcircle.setCenterPoint(tMapStartPOIItem.getPOIPoint());
                      tcircle.setRadius(300);
                      tcircle.setLineColor(Color.BLUE);
                      tcircle.setLineAlpha(2);
                      tcircle.setAreaAlpha(100);
                      tcircle.setAreaColor(Color.GRAY);
                      tcircle.setRadiusVisible(true);
                      tMapView.addTMapCircle("circle", tcircle);
                      tMapView.addMarkerItem("1", tMapMarkerItem);
                      tMapView.setCenterPoint(longitude, latitude);
                  }
                });
                  }
              }catch (Exception e){
                  Toast.makeText(getApplicationContext(),"주소",Toast.LENGTH_SHORT).show();
              }
            }
        });


/*btnCircle.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(check==0){
        tcircle.setCenterPoint(tMapStartPOIItem.getPOIPoint());
        tcircle.setRadius(300);
        tcircle.setLineColor(Color.BLUE);
        tcircle.setLineAlpha(2);
        tcircle.setAreaAlpha(100);
        tcircle.setAreaColor(Color.GRAY);
        tcircle.setRadiusVisible(true);
        tMapView.addTMapCircle("circle",tcircle);
        check++;
        }
        else{
            tcircle.setCenterPoint(tMapDestinationPOIItem.getPOIPoint());
            tcircle.setRadius(300);
            tcircle.setLineColor(Color.BLUE);
            tcircle.setLineAlpha(2);
            tcircle.setAreaColor(Color.GRAY);
            tcircle.setRadiusVisible(true);
            tMapView.addTMapCircle("circle",tcircle);
            check--;

        }

    }
});*/



        btnDestinationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtDestination.getText().toString();
                if(str.equals("")){
                    Toast.makeText(ShowPathActivity.this,"도착지를 입력하세요.",Toast.LENGTH_SHORT).show();
                }else {

                    tMapdata.findAllPOI("" + str, 1, new TMapData.FindAllPOIListenerCallback() {
                        @Override
                        public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                            tMapDestinationPOIItem = (TMapPOIItem) arrayList.get(0);
                            Log.i("검색결과", "" + tMapDestinationPOIItem.getPOIAddress() + "|" + tMapDestinationPOIItem.getPOIName() + "|" + tMapDestinationPOIItem.getPOIContent() + "|" + tMapDestinationPOIItem.getPOIPoint());
                            tMapMarkerItem = new TMapMarkerItem();
                            tMapMarkerItem.setTMapPoint(tMapDestinationPOIItem.getPOIPoint());
                            tcircle.setCenterPoint(tMapDestinationPOIItem.getPOIPoint());
                            tcircle.setRadius(300);
                            tcircle.setLineColor(Color.BLUE);
                            tcircle.setLineAlpha(2);
                            tcircle.setAreaAlpha(100);
                            tcircle.setAreaColor(Color.GRAY);
                            tcircle.setRadiusVisible(true);
                            tMapView.addTMapCircle("circle", tcircle);


                            tMapView.addMarkerItem("1", tMapMarkerItem);
                            tMapView.setCenterPoint(tMapDestinationPOIItem.getPOIPoint().getLongitude(), tMapDestinationPOIItem.getPOIPoint().getLatitude());
                        }
                    });
                }
            }
        });




/*btnLine.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ArrayList arPoint = tpolyline.getLinePoint();
        tpolyline.setLineColor(Color.BLUE);
        tpolyline.setLineWidth(10);
        tpolyline.addLinePoint(tMapStartPOIItem.getPOIPoint());
        tpolyline.addLinePoint(tMapDestinationPOIItem.getPOIPoint());
        tMapView.addTMapPolyLine("line1",tpolyline);

    }
});*/




/*btnPathSearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String str1 = edtStart.getText().toString();
        String str2 = edtDestination.getText().toString();
        Intent intent = new Intent(getApplicationContext(),ShowPathActivity.class);
        intent.putExtra("StartName", ""+tMapStartPOIItem.getPOIName());
        intent.putExtra("StartLat", ""+tMapStartPOIItem.getPOIPoint().getLatitude());
        intent.putExtra("StartLon", ""+tMapStartPOIItem.getPOIPoint().getLongitude());
        intent.putExtra("EndName", ""+tMapDestinationPOIItem.getPOIName());
        intent.putExtra("EndLat", ""+tMapDestinationPOIItem.getPOIPoint().getLatitude());
        intent.putExtra("EndLon", ""+tMapDestinationPOIItem.getPOIPoint().getLongitude());
        setResult(RESULT_OK,intent);
        startActivity(intent);


    }
});*/
        /*btnStartLocationWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectCheck();
            }
        });

        btnEndLocationWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectCheck1();
            }
        });*/
    }



    public void setLocation(String location){
        Log.i("Main_setLocation",""+location);
        try {
            JSONObject jsonObject1 = new JSONObject(location);
            Log.i("Main_setLocation",""+jsonObject1.getJSONObject("searchPoiInfo"));
            JSONObject jsonObject2 = jsonObject1.getJSONObject("searchPoiInfo");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("pois");
            Log.i("Main_setLocation",""+jsonObject2);
            JSONArray jsonArray1 = jsonObject3.getJSONArray("poi");
            Log.i("Main_setLocation",""+jsonArray1);
            Log.i("Main_setLocation",""+jsonArray1.get(0));
            JSONObject jsonObject4 = (JSONObject)jsonArray1.get(0); // 타입캐스팅 해야함
            double noorLat = Double.parseDouble(jsonObject4.getString("noorLat"));
            Log.i("Main_setLocation",""+noorLat);
            double noorLon = Double.parseDouble(jsonObject4.getString("noorLon"));
            Log.i("Main_setLocation",""+noorLon);
            name=jsonObject4.getString("name");

            tMapMarkerItem.setTMapPoint(new TMapPoint(noorLat,noorLon));
            tMapView.addMarkerItem("",tMapMarkerItem);







        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void connectCheck(){
        ConnectivityManager connectivityManager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); // 인터넷연결상태 확인
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        String inputData = edtStart.getText().toString();
        HashMap<String,String> reqLocation = new HashMap<String,String>();
        reqLocation.put("searchKeyword",""+inputData);
        reqLocation.put("searchType","all");

        if((networkInfo!=null)&&networkInfo.isConnected()){
            getLocationinfoAsyncTask = new GetLocationinfoAsyncTask(reqLocation,this);
            getLocationinfoAsyncTask.execute("");
        }else{
            Toast.makeText(this,"인터넷 연결상태를 확인하세요.",Toast.LENGTH_SHORT).show();
        }
    }

    public void connectCheck1(){
        ConnectivityManager connectivityManager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); // 인터넷연결상태 확인
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        String inputData = edtDestination.getText().toString();
        HashMap<String,String> reqLocation = new HashMap<String,String>();
        reqLocation.put("searchKeyword",""+inputData);
        reqLocation.put("searchType","all");

        if((networkInfo!=null)&&networkInfo.isConnected()){
            getLocationinfoAsyncTask = new GetLocationinfoAsyncTask(reqLocation,this);
            getLocationinfoAsyncTask.execute("");
        }else{
            Toast.makeText(this,"인터넷 연결상태를 확인하세요.",Toast.LENGTH_SHORT).show();
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            //현재위치의 좌표를 알수있는 부분
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tMapdata.convertGpsToAddress(latitude, longitude, new TMapData.ConvertGPSToAddressListenerCallback() {
                    @Override
                    public void onConvertToGPSToAddress(String straddress) {
                        address = straddress;
                        edtStart.setText(address);
                        Log.d("test1234",address);

                    }
                });

                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude);
                Log.d("test123", longitude+","+latitude);

            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    public void setGps() {
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                (float) 0.1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

    }

    int i;
    public void getAroundPOI(){

        final TMapData tmapdata = new TMapData();
        TMapPoint point = tMapView.getCenterPoint();
        tmapdata.findAroundNamePOI(point, "편의점;은행", 1, 10, new TMapData.FindAroundNamePOIListenerCallback() {
            @Override
            public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItmes) {
                addr=new String[poiItmes.size()];
                for(i = 0; i<poiItmes.size();i++){
                    TMapPOIItem item = poiItmes.get(i);
                    Log.d("편의시설","POI Name: "+item.getPOIName()+","+"Address: "+item.getPOIAddress().replace("null",""));
                    tMapMarkerItem = new TMapMarkerItem();
                    tMapMarkerItem.setTMapPoint(item.getPOIPoint());
                    tMapMarkerItem.setCalloutTitle(item.getPOIName());
                    tMapMarkerItem.setCanShowCallout(true);
                    tMapMarkerItem.setAutoCalloutVisible(true);
                    tMapMarkerItem.setCalloutRightButtonImage(bitmap);
                    tMapdata.convertGpsToAddress(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude(), new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String straddress) {
                            addr[i]=straddress;
                        }
                    });

                    tMapView.setCenterPoint( item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude());
                    tMapView.setZoomLevel(17);
                    tMapView.addMarkerItem(""+i,tMapMarkerItem);
                }
            }
        });


    }


}