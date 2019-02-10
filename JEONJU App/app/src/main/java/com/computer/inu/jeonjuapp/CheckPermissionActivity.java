package com.computer.inu.jeonjuapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class CheckPermissionActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        // 사용자 OS 버전이 마시멜로 이상인지 체크
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if(permissionResult== PackageManager.PERMISSION_DENIED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
                }else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);
                }
            }else {
                Intent intent = new Intent(getApplicationContext(), com.computer.inu.jeonjuapp.ShowPathActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            Intent intent = new Intent(getApplicationContext(), com.computer.inu.jeonjuapp.ShowPathActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
    }

    /*
    권한 요청에 대한 응답 부분
    requestCode : 요청코드
    permissions : 사용자가 요청한 권한들
    getResults : 권한에 대한 응답들(배열) -> 한번에 여러개 권한체크를 할 수 있다.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(getApplicationContext(), com.computer.inu.jeonjuapp.MainActivity.class);
                if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    startActivity(intent);
                    finish();
                }
            }else{
                Toast.makeText(this,"권한 요청을 거부하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}