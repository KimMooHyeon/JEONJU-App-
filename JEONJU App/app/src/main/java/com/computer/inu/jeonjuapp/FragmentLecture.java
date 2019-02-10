package com.computer.inu.jeonjuapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLecture extends Fragment {
    private final static String KEY="rXCtpQeVNRRd29DXomKbdrLow%2FTYIYSNLF0z6GTdlwOj95Y8ubqUom0qtcf19P9t47SweSbC9wOopuoXYJC0fg%3D%3D"; //인증키

    private final static String[] ENTRY={"clTitle", "clContent", "addr", "cotel", "clStarttime", "clEndtime","clStartstudy",
            "clEndstudy", "coTime", "eduStartTime", "eduEndTime", "clGubunNm", "coName", "clMoney", "tcName", "clMaxstud"};
    private static int CLASS_ID;
    private int ID;
    private ListView listView;
    private LectureAdapter adapter;
    private int startPage;
    private boolean lastItemVisibleFlag;
    private String[] option;
    private View testView;
    private int totalCount;

    public FragmentLecture() {
        lastItemVisibleFlag=false;
        startPage=1;
        totalCount=0;
        ID=CLASS_ID++;
    }

    ArrayList<LectureItem> lectureItemList = new ArrayList<LectureItem>(); //리스트뷰에 띄울 데이터 목록


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            option = new String[getArguments().getInt("length")];
            Log.d("argcheck",ID+", option length= "+option.length);
            for(int i=0; i<option.length; i++){
                option[i]=getArguments().getString("option"+i);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        testView = inflater.inflate(R.layout.fragment_lecture, container, false);
        listView = testView.findViewById(R.id.lectureList);
        adapter = new LectureAdapter();
        listView.setAdapter(adapter);

        int cnt = 20;
        final int FIRST_COUNTED = cnt;

        getPage(listView, adapter, startPage);

        //리스트에 출력
        for(int i=0; i<20; i++){
            adapter.addItem(lectureItemList.get(i).getTitle(),
                    lectureItemList.get(i).getStart(), lectureItemList.get(i).getEnd());
        }


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int cnt = FIRST_COUNTED;
            private int max = 40;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if (cnt == lectureItemList.size()) {
                        Toast.makeText(getContext(), "마지막 항목 입니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        while (cnt < lectureItemList.size() && cnt <= max) {
                            adapter.addItem(lectureItemList.get(cnt).getTitle(),
                                    lectureItemList.get(cnt).getStart(), lectureItemList.get(cnt).getEnd());
                            cnt ++;
                        }
                        max += 20;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


        return testView;
    }

    public static FragmentLecture newInstance(){
        return new FragmentLecture();
    }

    public String getDayString(String dayString){
        int i = 0;
        String year, month, day;
        String result ="";

        StringTokenizer token = new StringTokenizer(dayString);
        year = token.nextToken("-");
        month = token.nextToken("-");
        day = token.nextToken("-");


        result = year +"."+ month +"."+ day;

        return result;
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

    private void getPage(ListView listview, LectureAdapter adapter,int startPage){
        HashMap<String, Boolean> flag = InitFlag();
        HashMap<String, String> data = InitData();

        StrictMode.enableDefaults();
        final DbHelper dbHelper = new DbHelper(getContext(),"MEMOJANG.db",null,1);

        try{
            //db clear
           dbHelper.deleteAllLecture();

            //API 다시불러오기(갱신)
            URL url = new URL("http://openapi.jeonju.go.kr/rest/lifestudy/getLifeStudyList?ServiceKey=" + KEY
                    + "&startPage="+startPage+"&pageSize=200");
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
                        Log.d("xml","<"+parser.getName()+">");
                        break;
                    case XmlPullParser.TEXT:
                        for(String element: ENTRY){
                            if(flag.get(element)){
                                data.put(element, parser.getText());
                                flag.put(element, false);
                            }
                        }
                        Log.d("xml","<"+parser.getText()+">");
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d("xml","<"+parser.getName()+"/>");
                        if(parser.getName().equals("list")){

                            dbHelper.insertLecture("LECTURES", data.get("clTitle"), data.get("clContent"), data.get("addr"), data.get("cotel"), getDayString(data.get("clStarttime")),
                                    getDayString(data.get("clEndtime")), getDayString(data.get("clStartstudy")), getDayString(data.get("clEndstudy")), data.get("coTime"), data.get("eduStartTime"), data.get("eduEndTime"),
                                    data.get("clGubunNm"), data.get("coName"), data.get("clMoney"), data.get("tcName"), data.get("clMaxstud"));

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
        Log.d("argcheck","count:"+adapter.getCount());

        lectureItemList = dbHelper.getResultList_Lecture();   //db에 저장한 즐겨찾기 목록 불러오기

    }


    public class LectureAdapter extends BaseAdapter {
        ArrayList<LectureItem> items = new ArrayList<LectureItem>();

        public void addItem(LectureItem item){
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            final LectureItemView view;
            if(convertView == null){
                view =new LectureItemView(getActivity());
            }else{
                view=(LectureItemView) convertView;
            }
            final LectureItem item = items.get(position);
            view.setTitle_tv(item.getTitle());
            view.setStart_tv(item.getStart());
            view.setEnd_tv(item.getEnd());

            final DbHelper dbHelper = new DbHelper(getActivity(),"MEMOJANG.db",null,1);

            if(dbHelper.getFavorites(item.getTitle()) != 0) {
                view.btn_star.setChecked(true);
            }
            else {
                view.btn_star.setChecked(false);
            }

            view.btn_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(view.btn_star.isChecked()) {
                        //MainActivity.db.execSQL("INSERT INTO contact VALUES(null, '"+name+"','"+tel+"');");
                        Toast.makeText(getActivity(), "즐겨찾기 추가", Toast.LENGTH_LONG).show();

                        ArrayList<String> lectures = new ArrayList<String>();
                        lectures.clear();
                        lectures = dbHelper.getResultList_Lecture2(item.getTitle());


                        dbHelper.insertFavorites(lectures.get(0), lectures.get(1), lectures.get(2), lectures.get(3),
                                lectures.get(4), lectures.get(5), lectures.get(6), lectures.get(7), lectures.get(8), lectures.get(9),
                                lectures.get(10), lectures.get(11), lectures.get(12), lectures.get(13), lectures.get(14), lectures.get(15));

                        adapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(getActivity(), item.getTitle() + " 즐겨찾기 해제", Toast.LENGTH_LONG).show();
                        dbHelper.deleteFavorites(item.getTitle());
                        adapter.notifyDataSetChanged();
                    }

                    //TabLectureActivity.fragmentReplace(1);

                }
            });

            //리스트 터치
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> list = dbHelper.getResultList_Lecture2(item.getTitle());
                    String money = list.get(13);
                    String max = list.get(15);
                    String week = list.get(8);

                    StringTokenizer token = new StringTokenizer(money);
                    token.hasMoreTokens();
                    money = token.nextToken(".");

                    token = new StringTokenizer(max);
                    token.hasMoreTokens();
                    max = token.nextToken(".");

                    token = new StringTokenizer(week);
                    week = token.nextToken("요일");

                    /*
                    new AlertDialog.Builder(getActivity()).setTitle(item.getTitle())
                            .setMessage("수강대상 :  " + list. get(11) +
                                    "\n수강인원 :  " + max + " 명" +
                                    "\n수강비 :  " + money + " 원" +
                                    "\n\n접수기간 :  " + list. get(4) + " - " + list. get(5) +
                                    "\n수강기간 :  " +  list. get(6) + " - " + list. get(7) +
                                    "\n수강요일 :  " + list. get(8) +
                                    "\n강의시간 :  " + list. get(16) +
                                    "\n강사명 :  " + list.get(14) +
                                    "\n\n강의내용 :  " + list.get(1) +
                                    "\n\n수강장소 :  " + list.get(12) +
                                    "\n주소 :  " + list.get(2)
                            )
                            .setPositiveButton("확인",null)
                            .show();
                      */


                    // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                    DialogLecture dialogLecture = new DialogLecture(getContext());

                    // 커스텀 다이얼로그를 호출한다.
                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                    ArrayList<String> content = new ArrayList<String>();
                    content.clear();
                    content.add("수강인원 :  " + max + " 명"); content.add("수강비 :  " + money + " 원");
                    content.add("접수기간 :  " + list. get(4) + " - " + list. get(5));
                    content.add("수강기간 :  " +  list. get(6) + " - " + list. get(7));
                    content.add("강의시간 :  " + list. get(16) + " (" + week + ")");
                    content.add("강사 :  " + list.get(14)); content.add("강의내용 :  " + list.get(1));
                    content.add("수강장소 :  " + list.get(12)); content.add(list.get(2));


                    dialogLecture.callFunction(item.getTitle(), list.get(11), content);
                }
            });

            return view;
        }


        // 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성
        public void addItem(String title, String start, String end) {

            LectureItem item = new LectureItem();
            // item에 아이템을 setting한다.
            item.setTitle(title);
            item.setStart(start);
            item.setEnd(end);

            // favorite_Items에 item을 추가한다.
            items.add(item);
        }
    }
}

