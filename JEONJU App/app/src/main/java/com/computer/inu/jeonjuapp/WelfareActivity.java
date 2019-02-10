package com.computer.inu.jeonjuapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WelfareActivity extends AppCompatActivity {
    private final static String key = "rXCtpQeVNRRd29DXomKbdrLow%2FTYIYSNLF0z6GTdlwOj95Y8ubqUom0qtcf19P9t47SweSbC9wOopuoXYJC0fg%3D%3D"; //인증키

    private final static String[] ENTRY={"welFareName", "welFareContent", "welFareGuideMemo", "welFareChargeTel", "welFareChargeNm",
            "welFareStyleSort", "welFareReceiptOrgan", "welFareDepartment", "welFareHavePaper", "welFareIsApplication",
            "welFareHowApplication", "welFareHowSupported", "welFareDispauper", "welFareLifeCycleSort"};

    private ListView listWelfare;
    private WelfareAdapter welfareAdapter;
    private int startPage=1;
    private boolean lastItemVisibleFlag = false;

    ArrayList<WelfareItem> welfareItemList = new ArrayList<WelfareItem>(); //리스트뷰에 띄울 데이터 목록


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare);

        StrictMode.enableDefaults();    //인터넷 사용 제약해제
        setTitle("복지 서비스");

        listWelfare=(ListView)findViewById(R.id.listWelfare);
        welfareAdapter = new WelfareAdapter();
        listWelfare.setAdapter(welfareAdapter);

        int cnt = 20;
        final int FIRST_COUNTED = cnt;

        getPage(listWelfare, welfareAdapter, startPage);

        //리스트에 출력
        for(int i=0; i<20; i++){
            String lifeCycle = welfareItemList.get(i).getContents();
            if( !(lifeCycle.equals("아동") || lifeCycle.equals("청소년") || lifeCycle.equals("아동,청소년") || lifeCycle.equals("아동.청소년"))) {
                welfareAdapter.addItem(welfareItemList.get(i).getTitle(), welfareItemList.get(i).getContents());
            }
        }

        listWelfare.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int cnt = FIRST_COUNTED;
            private int max = 40;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if (cnt == welfareItemList.size()) {
                        Toast.makeText(WelfareActivity.this, "마지막 항목 입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        while (cnt < welfareItemList.size() && cnt <= max) {
                            String lifeCycle = welfareItemList.get(cnt).getContents();
                            if( !(lifeCycle.equals("영유아") || lifeCycle.equals("아동") || lifeCycle.equals("청소년")
                                    || lifeCycle.equals("아동,청소년") || lifeCycle.equals("아동.청소년"))) {
                                welfareAdapter.addItem(welfareItemList.get(cnt).getTitle(), welfareItemList.get(cnt).getContents());
                            }
                            cnt ++;
                        }
                        max += 20;
                        welfareAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        getPage(listWelfare, welfareAdapter, startPage++);
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

    private void getPage(ListView listview, WelfareAdapter adapter, int startPage){
        HashMap<String, Boolean> flag = InitFlag();
        HashMap<String, String> data = InitData();

        StrictMode.enableDefaults();
        final DbHelper dbHelper = new DbHelper(this,"MEMOJANG.db",null,1);

        //파싱 시작
        try{
            //db clear
            dbHelper.deleteAllWelfare();

            //API 다시불러오기(갱신)
            URL url = new URL("http://openapi.jeonju.go.kr/rest/welfarenew/getWelfareList?serviceKey=" + key
                    //+ "&clId=217"
                    //+ "&startPage=1"
                    + "&startPage=" + startPage
                    + "&pageSize=200"    //페이지당 항목개수
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱 시작");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG:   //parser가 시작 태그를 만나면 실행
                        flag.put(parser.getName(),true);
                        if(parser.getName().equals("message")){     //message 태그를 만나면 에러 출력
                            Toast.makeText(this,"오류발생: message 태그\n"+data.get("message") ,Toast.LENGTH_SHORT).show();
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        for(String element: ENTRY) {
                            if (flag.get(element)) {     //isTitle이 true일 때 태그의 내용을 저장.
                                data.put(element, parser.getText());
                                flag.put(element, false);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("list")){    //db에 파싱한 데이터 추가

                            dbHelper.insertWelfare("WELFARE", data.get("welFareName"), data.get("welFareContent"), data.get("welFareGuideMemo"), data.get("welFareChargeTel"), data.get("welFareChargeNm"),
                                        data.get("welFareStyleSort"), data.get("welFareReceiptOrgan"), data.get("welFareDepartment"), data.get("welFareHavePaper"), data.get("welFareIsApplication"),
                                        data.get("welFareHowApplication"), data.get("welFareHowSupported"), data.get("welFareDispauper"), data.get("welFareLifeCycleSort"));

                                //welfareAdapter.addItem(data.get("welFareName"), data.get("welFareChargeTel"));

                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            Toast.makeText(this,"오류발생: Exception e" ,Toast.LENGTH_SHORT).show();

            String buffer=null;
            StackTraceElement[] elements = e.getStackTrace();
            for(StackTraceElement element: elements) {
                String className = element.getClassName();
                String methodName = element.getMethodName();
                String fileName = element.getFileName();
                int lineNumber = element.getLineNumber();
                buffer = buffer + "\n\nelement:" + element +
                        "\nclassName:" + className +
                        "\nmethodName:" + methodName +
                        "\nfileName:" + fileName +
                        "\nlineNumber:" + lineNumber;
                Log.e("WelfareActiviy", buffer);
            }
        }


        welfareItemList = dbHelper.getResultList_Welfare();

    }


    public class WelfareAdapter extends BaseAdapter {


        private ArrayList<WelfareItem> welfareItems = new ArrayList<>();

        @Override
        public int getCount() {
            return welfareItems.size();
        }

        @Override
        public WelfareItem getItem(int position) {
            return welfareItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final WelfareItemView view;
            if(convertView == null){
                view =new WelfareItemView(getApplicationContext());
            }else{
                view=(WelfareItemView) convertView;
            }
            WelfareItem item = welfareItems.get(position);
            view.setTitle(item.getTitle());
            view.setLifeCycle(item.getContents());


            final DbHelper dbHelper = new DbHelper(WelfareActivity.this,"MEMOJANG.db",null,1);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    ArrayList<String> list = dbHelper.getResultList_Welfare2(getItem(position).getTitle());
                    String name = list.get(4);
                    String tel = list.get(5);
                    if(name.equals(null)) {
                        name = "-";
                    }
                    if(tel.equals(null)) {
                        tel = "-";
                    }

                    new AlertDialog.Builder(WelfareActivity.this).setTitle("상세정보")
                            .setMessage("[지원대상]  " + list. get(2) +
                                        "\n" + list.get(3) +
                                        "\n\n-담당자 : " + name +
                                        "\n-전화번호 : " + tel +
                                        "\n-담당부서 : " + list.get(9) +
                                        "\n\n-신청방법 : " + list.get(6) +
                                        "\n-지원방식 : " + list.get(7) +
                                    "\n\n[지원내용]\n" + list.get(8)
                            )
                            .setPositiveButton("확인",null)
                            .show();


                    //Intent intent= new Intent(getApplicationContext(), LectureMoreInfoActivity.class);
                    //startActivity(intent);
                }
            });


            return view;
        }


        // 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
        public void addItem(String title, String lifeCycle) {

            WelfareItem item = new WelfareItem();

            // item에 아이템을 setting한다.
            item.setTitle(title);
            item.setContents(lifeCycle);

            // favorite_Items에 item을 추가한다.
            welfareItems.add(item);
        }

    }
}
