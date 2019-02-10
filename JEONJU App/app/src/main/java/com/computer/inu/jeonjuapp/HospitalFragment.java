package com.computer.inu.jeonjuapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalFragment extends Fragment {

    private ListView listView;
    private HospitalAdapter adapter;
    private boolean lastItemVisibleFlag;
    private String[] option;
    private View testView;
    private double myX=127.1412500;//경도
    private double myY=35.8468571;//위도
    public HospitalFragment() {
        lastItemVisibleFlag=false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            option = new String[getArguments().getInt("length")];
            for(int i=0; i<option.length; i++){
                option[i]=getArguments().getString("option"+i);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ArrayList<HospitalItem> hospitalItems = new ArrayList<HospitalItem>();
        testView = inflater.inflate(R.layout.hospital_fragment, container, false);
        listView=testView.findViewById(R.id.hospitalList);
        adapter= new HospitalAdapter();
        listView.setAdapter(adapter);

        final DbHelper dbHelper = new DbHelper(testView.getContext(),"TEST.db",null,1);
        final Cursor cursor=dbHelper.hospitalGetCursor(option);


        while(cursor.moveToNext()){
            hospitalItems.add(new HospitalItem(cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),
                    cursor.getString(5),cursor.getString(6),Integer.parseInt(cursor.getString(0))));
        }


        Comparator<HospitalItem> distance= new Comparator<HospitalItem>() {
            @Override
            public int compare(HospitalItem o1, HospitalItem o2) {
                Double dist1=Math.sqrt(Math.pow(Double.parseDouble(o1.getPosx())-myX,2))+Math.sqrt(Math.pow(Double.parseDouble(o1.getPosy())-myY,2));
                Double dist2=Math.sqrt(Math.pow(Double.parseDouble(o2.getPosx())-myX,2))+Math.sqrt(Math.pow(Double.parseDouble(o2.getPosy())-myY,2));
                return  dist1.compareTo(dist2);
            }
        };

        Collections.sort(hospitalItems, distance);

        int cnt=0;
        while(cnt<hospitalItems.size() && cnt<=20){
            adapter.addItem(hospitalItems.get(cnt));
            cnt++;
        }
        final int FIRST_COUNTED=cnt;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int cnt=FIRST_COUNTED;
            private int max=40;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if(cnt == hospitalItems.size()) {
                        Toast.makeText(testView.getContext(), "마지막 항목입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        while(cnt<hospitalItems.size()&&cnt<=max){
                            adapter.addItem(hospitalItems.get(cnt));
                            cnt++;
                        }
                        max+=20;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HospitalItem item= (HospitalItem) parent.getAdapter().getItem(position);
                CustumDialog dialog = new CustumDialog(testView.getContext(),item);
                dialog.show();
            }
        });
        return testView;
    }

    public static HospitalFragment newInstance(String[] option){
        Bundle args = new Bundle();
        for(int i=0; i<option.length; i++) {
            args.putString("option" + i, option[i]);
        }
        args.putInt("length",option.length);
        HospitalFragment fragment = new HospitalFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private class HospitalAdapter extends BaseAdapter {
        ArrayList<HospitalItem> items=new ArrayList<HospitalItem>();
        public void addItem(HospitalItem item){
            items.add(item);
        }

        public int getCount(){
            return items.size();
        }

        public Object getItem(int position){
            return items.get(position);
        }

        public long getItemId(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            HospitalItemView view;
            if(convertView == null){
                view =new HospitalItemView(getActivity());
            }else{
                view=(HospitalItemView) convertView;
            }
            HospitalItem item = items.get(position);
            view.setName(item.getName());
            view.setAddress(item.getAddress());
            view.setSort(item.getSort());
            view.setTel(item.getTel());
            return view;
        }
    }
    public class CustumDialog extends Dialog implements View.OnClickListener{
        private Context context;
        private HospitalItem item;
        private TextView title;
        private TextView address, sort, time, tel,emergencyY, emergencyN, holidayY, holidayN;
        private TextView[] days;
        private TextView cancel;
        private TextView call;
        private TextView map;
        public CustumDialog(@NonNull Context context){
            super(context);
            this.context= context;
        }
        public CustumDialog(Context context,HospitalItem item){
            super(context);
            this.context=context;
            this.item=item;
        }
        void setBackground(TextView[] Days, int start, int end){
            Days[start].setBackgroundResource(R.drawable.hospital_text_background_left_round);
            for(int i=start+1; i<end; i++){
                Days[i].setBackgroundResource(R.drawable.hospital_text_background_rect);
            }
            Days[end].setBackgroundResource(R.drawable.hospital_text_background_right_round);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.hospital_custom_dialog);
            final DbHelper dbHelper = new DbHelper(getContext(),"TEST.db",null,1);

            title=findViewById(R.id.hospitalDialogTitle);
            cancel=findViewById(R.id.hospitalDialogCancel);
            call=findViewById(R.id.hospitalDialogCall);
            map=findViewById(R.id.hospitalDialogMap);
            address=findViewById(R.id.hospitalAddress);
            sort=findViewById(R.id.hospitalSort);
            time=findViewById(R.id.hospitalTime);
            days=new TextView[]{findViewById(R.id.hospitalDayMon),findViewById(R.id.hospitalDayTue),
                    findViewById(R.id.hospitalDayWed),findViewById(R.id.hospitalDayThu),findViewById(R.id.hospitalDayFri),
                    findViewById(R.id.hospitalDaySat),findViewById(R.id.hospitalDaySun)};
            tel=findViewById(R.id.hospitalTel);
            emergencyY=findViewById(R.id.hospitalEmergencyYes);
            emergencyN=findViewById(R.id.hospitalEmergencyNo);
            holidayY=findViewById(R.id.hospitalHolidayYes);
            holidayN=findViewById(R.id.hospitalHolidayNo);

            title.setText(item.getName());
            Cursor cursor = dbHelper.hospitalGetAt(item.getAddress(), item.getSort());
            while(cursor.moveToNext()){
                address.setText(cursor.getString(2));
                sort.setText(cursor.getString(3));
                time.setText(cursor.getString(8)+" - "+cursor.getString(9));
                tel.setText(cursor.getString(4));
                String date = cursor.getString(7);
                int start=0, end=0;
                boolean endFlag=false;
                for(int i=0; i<date.length(); i++){
                    char ch=date.charAt(i);
                    switch(ch){
                        case '월':
                            if(endFlag){
                                end=0;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[0].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 0;
                            }
                            break;
                        case '화':
                            if(endFlag){
                                end=1;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[1].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 1;
                            }
                            break;
                        case '수':
                            if(endFlag){
                                end=2;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[2].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 2;
                            }
                            break;
                        case '목':
                            if(endFlag){
                                end=3;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[3].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 3;
                            }
                            break;
                        case '금':
                            if(endFlag){
                                end=4;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[4].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 4;
                            }
                            break;
                        case '토':
                            if(endFlag){
                                end=5;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[5].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 5;
                            }
                            break;
                        case '일':
                            if(endFlag){
                                end=6;
                                setBackground(days,start,end);
                                endFlag=false;
                            }else {
                                days[6].setBackgroundResource(R.drawable.hospital_text_background_round);
                                start = 6;
                            }
                            break;
                        case '~':
                            endFlag=true;
                            break;
                    }
                }


                if(cursor.getString(10).equals("1")){
                    emergencyY.setBackgroundResource(R.drawable.hospital_text_background_round);
                }else{
                    emergencyN.setBackgroundResource(R.drawable.hospital_text_background_round);
                }
                if(cursor.getString(13).equals("1")){
                    holidayY.setBackgroundResource(R.drawable.hospital_text_background_round);
                }else{
                    holidayN.setBackgroundResource(R.drawable.hospital_text_background_round);
                }

            }
            cancel.setOnClickListener(this);
            call.setOnClickListener(this);
            map.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.hospitalDialogCancel:
                    cancel();
                    break;
                case R.id.hospitalDialogCall:
                    //Toast.makeText(getContext(),"전화 기능 활성화",Toast.LENGTH_SHORT).show();
                    String tel=item.getTel();
                    if(tel.contains(",")){
                        String newTel="";
                        for(int i=0; tel.charAt(i)!=','&& i<tel.length(); i++){
                            newTel+=tel.charAt(i);
                            Log.d("telid",newTel);
                        }
                        tel=newTel;
                    }
                    if(tel.length()<9){
                        tel="063-"+tel;
                    }
                    startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+tel)));
                    cancel();
                    break;
                case R.id.hospitalDialogMap:
                    //TODO: Tmap 경로탐색 기능을 시작 해야 함


                    //Toast.makeText(getContext(),item.getPosx()+","+item.getPosy()+" T맵 활성화",Toast.LENGTH_SHORT).show();
                    cancel();
                    break;
            }
        }
    }

}
