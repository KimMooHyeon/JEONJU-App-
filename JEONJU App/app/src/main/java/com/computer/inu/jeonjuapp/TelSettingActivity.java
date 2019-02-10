package com.computer.inu.jeonjuapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TelSettingActivity extends Activity {
    //연락처 불러오기
    private static final int RESULT_PICK_CONTACT = 85500;
    private TextView textView1;
    private TextView textView2;
    static final int READ_BLOCK_SIZE = 100;
    ListView list;
    ListViewAdapter adapter;
    ArrayList<ListViewItem> arrData;
    EditText txtName, txtPhone, txtMail;
    Button btnSave;
    CheckBox checkBox;

    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        //연락처 설정
        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        btnSave = (Button) findViewById(R.id.btnSave);
        arrData = new ArrayList<ListViewItem>();
        textView1 = (TextView) findViewById(R.id.txtName);
        textView2 = (TextView) findViewById(R.id.txtPhone);
        //메시지 설정
        editText = (EditText) findViewById(R.id.editText);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        //메모장에서 item 불러오기
        ReadBtn(this);

        //어댑터 생성
        adapter = new ListViewAdapter(this, arrData);


        //리스트뷰에 어댑터 연결
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        //리스트뷰 이벤트 설정
        list.setOnItemLongClickListener(new ListViewItemLongClickListener());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBox.isChecked() == true) {
                    editText.setEnabled(true);
                    editText.setClickable(true);
                    editText.setFocusable(true);
                } else {
                    //editText.setFocusable(false);
                    //editText.setClickable(false);
                    editText.setEnabled(false);
                }

            }
        });

    }

    public void pickContact(View v) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            textView1.setText(name);
            textView2.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 연락처 db에 저장하기-------------------------------------------------------------------------
    public void WriteBtn(ListViewItem contacts) {
        String name = contacts.getName();
        String phone = contacts.getTel();

        DbHelper dbHelper = new DbHelper(this,"MEMOJANG.db",null,1);

        try {

            dbHelper.insertContacts(name, phone);

            //리스트 갱신
            arrData.add(new ListViewItem(name, phone));
            arrData = dbHelper.getResultList_Contacts();

            list.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void InputData(View v) {
        String name = txtName.getText().toString();
        String phone = txtPhone.getText().toString();


        //이름, 전화번호 입력은 필수
        if (name.equals("")) {
            Toast.makeText(getBaseContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.equals("")) {
            Toast.makeText(getBaseContext(), "전화번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        txtName.setText("");
        txtPhone.setText("");

        WriteBtn(new ListViewItem(name, phone));

        Toast.makeText(getBaseContext(), "연락처를 저장하였습니다", Toast.LENGTH_SHORT).show();
    }


    // 연락처db 불러오기----------------------------------------------------------------------------
    public void ReadBtn(TelSettingActivity v) {
        //reading text from file
        try {

            DbHelper dbHelper = new DbHelper(this,"MEMOJANG.db",null,1);

            ArrayList<ListViewItem> contacts = new ArrayList<ListViewItem>();

            contacts = dbHelper.getResultList_Contacts();

            for(int i = 0; i<contacts.size(); i++){
                arrData.add(new ListViewItem(contacts.get(i).getName(), contacts.get(i).getTel()));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //아이템 삭제-----------------------------------------------------------------------------------
    //int selectedPos = -1;

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            //selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle("연락처 삭제");

            final DbHelper dbHelper = new DbHelper(TelSettingActivity.this,"MEMOJANG.db",null,1);

            // '삭제' 버튼 클릭
            alertDlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        String name = arrData.get(position).getName();
                        arrData.remove(position);
                        adapter=new ListViewAdapter(TelSettingActivity.this, arrData);
                        dbHelper.deleteContacts(name);
                        Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT ).show();
                        list.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.



                    //arrData = dbHelper.getResultList_Contacts();

                    dialog.dismiss();  // AlertDialog를 닫는다.

                }
            });

            // '취소' 버튼 클릭
            alertDlg.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });


            alertDlg.setMessage(String.format("연락처를 삭제하시겠습니까?"));
            alertDlg.show();
            return false;
        }

    }



    //================================================================================메시지 설정

    public void WriteBtn(View v) {
        // add-write text into file
        try {
            FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(editText.getText().toString());
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "메시지를 저장하였습니다",
                    Toast.LENGTH_SHORT).show();

            //editText.setText(""); ````````````````

            ReadBtn(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read text from file
    public void ReadBtn() {
        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();
            //Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
            editText.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}