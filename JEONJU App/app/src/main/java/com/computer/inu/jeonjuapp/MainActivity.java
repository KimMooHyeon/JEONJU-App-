package com.computer.inu.jeonjuapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_main_map     ;
    ImageButton btn_main_lecture  ;
    ImageButton btn_main_service;
    ImageButton btn_main_emergency  ;
    ImageButton btn_main_emergency_NumberSettings ;
    ImageButton btn_main_medicine ;
    ImageButton btn_main_hospital;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_main_emergency_NumberSettings = (ImageButton)findViewById(R.id.btn_main_emergency_NumberSettings) ;
        btn_main_emergency = (ImageButton) findViewById(R.id.btn_main_emergency) ;
       btn_main_map = (ImageButton)findViewById(R.id.ib_main_map);
        btn_main_lecture = (ImageButton)findViewById(R.id.ib_main_lecture);
        btn_main_service = (ImageButton)findViewById(R.id.ib_main_service);
        btn_main_medicine = (ImageButton)findViewById(R.id.ib_main_medicine);
        btn_main_hospital = (ImageButton)findViewById(R.id.ib_main_hospital);
        final DbHelper dbHelper = new DbHelper(this,"MEMOJANG.db",null,1);

        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            } else {

            // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            }
        }


        btn_main_emergency_NumberSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TelSettingActivity.class);
                startActivity(intent);
            }
        });

        btn_main_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View messageView = inflater.inflate(R.layout.activity_message_setting, null);
                EditText txtMessage = (EditText)messageView.findViewById(R.id.editText);

                //초기값
                String message=txtMessage.getText().toString();
                ArrayList<ListViewItem> phone = new ArrayList<ListViewItem>();
                phone.clear();
                String stringMsg = "";
                String stringPhone = "";
                int countContactsLine=0;    //연락처에서 몇번째 줄까지 읽었는지 저장

                //(2-1)******메시지 불러오기
                try {
                    FileInputStream fileMessage = openFileInput("mytextfile.txt");    //사용자지정 메시지
                    InputStreamReader InputMessage = new InputStreamReader(fileMessage);

                    char[] inputMessageBuffer = new char[READ_BLOCK_SIZE];
                    int charMessage;

                    while ((charMessage = InputMessage.read(inputMessageBuffer)) > 0) {
                        // char to string conversion
                        String readMessage = String.copyValueOf(inputMessageBuffer, 0, charMessage);
                        stringMsg += readMessage;
                    }
                    InputMessage.close();

                } catch (Exception e) { //파일 스트림 Exception
                    e.printStackTrace();
                }

                //(2-2)******전화번호 불러오기
                try {

                    phone = dbHelper.getResultList_Contacts();
                    stringPhone = phone.get(0).getTel();

                } catch (Exception e) { //파일 스트림 Exception
                    e.printStackTrace();
                }

                //(3)문자 전송----------------------------------------------------------------------
                //사용자가 입력한 메시지가 있으면, 그 메시지를 저장(오류:사용자 메시지를 꼭 지정해줘야 전송됨)
                if (! stringMsg.equals("")) message = stringMsg;

                //사용자가 입력한 번호가 없으면 112에만 문자 전송(테스트를 위해 토스트 메시지로만 확인. 실제 전송x)
                if (stringPhone.equals("")) {
                    Toast.makeText(getApplicationContext(), "저장된 번호가 없습니다", Toast.LENGTH_LONG).show();
                }
                else
                {   //사용자가 입력한 번호로 문자 전송
                    StringTokenizer stok = new StringTokenizer(stringPhone, "\n");    //문자열 끊기

                    for(int i = 0; i< phone.size(); i++){
                        SendSMS(phone.get(i).getTel(), message );
                    }

                    Toast.makeText(getApplicationContext(), "문자를 전송하였습니다.", Toast.LENGTH_LONG).show();
                }
            }

        });
        btn_main_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HospitalActivity.class));
            }
        });
        btn_main_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MedicineActivity.class));
            }
        });
        btn_main_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowPathActivity.class));
            }
        });
        btn_main_lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TabLectureActivity.class);
                startActivity(intent);
            }
        });
        btn_main_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),WelfareActivity.class);
                startActivity(intent);
            }
        });

    }
    public void SendSMS(String phoneNum, String message) {
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(phoneNum, null, message, null, null);
    }
}
