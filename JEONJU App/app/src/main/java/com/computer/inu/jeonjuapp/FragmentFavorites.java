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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FragmentFavorites  extends Fragment {
    private final static String[] ENTRY={"clTitle", "clContent", "addr", "cotel", "clStarttime", "clEndtime","clStartstudy",
            "clEndstudy", "coTime", "eduStartTime", "eduEndTime", "clGubunNm", "coName", "clMoney", "tcName", "clMaxstud"};
    private static int CLASS_ID;
    private int ID;
    private ListView listView;
    private FavoritesAdapter adapter;
    private int startPage;
    private boolean lastItemVisibleFlag;
    private String[] option;
    private View testView;
    private int totalCount;

    public FragmentFavorites() {
        lastItemVisibleFlag=false;
        startPage=1;
        totalCount=0;
        ID=CLASS_ID++;
    }

    ArrayList<FavoritesItem> favoritesItemList = new ArrayList<FavoritesItem>(); //리스트뷰에 띄울 데이터 목록


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
        testView = inflater.inflate(R.layout.fragment_favorites, container, false);
        listView = testView.findViewById(R.id.favoritesList);
        adapter = new FavoritesAdapter();
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    if(adapter.getCount()<totalCount) {
                        getPage(listView, adapter, ++startPage);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(),"마지막 데이터 입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getPage(listView, adapter, startPage);

        return testView;
    }

    public static FragmentFavorites newInstance(){
        return new FragmentFavorites();
    }

    private HashMap<String, Boolean> InitFlag(){
        HashMap<String, Boolean> newFlag=new HashMap<String, Boolean>();
        for(String element: ENTRY){
            newFlag.put(element, false);
        }
        return newFlag;
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

    private void getPage(ListView listview, FavoritesAdapter adapter, int startPage){
        StrictMode.enableDefaults();
        final DbHelper dbHelper = new DbHelper(getContext(),"MEMOJANG.db",null,1);

        try {

            favoritesItemList = dbHelper.getResultList_Favorites();   //db에 저장한 즐겨찾기 목록 불러오기

            //리스트에 출력
            for (int i = 0; i < favoritesItemList.size(); i++) {
                adapter.addItem(favoritesItemList.get(i).getTitle(),
                        favoritesItemList.get(i).getStart(), favoritesItemList.get(i).getEnd());
            }

        } catch (Exception e){
            Toast.makeText(getContext(),"오류발생: Exception e" ,Toast.LENGTH_SHORT).show();

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
                Log.e("FragmentFavorites", buffer);
            }
        }
    }

    public class FavoritesAdapter extends BaseAdapter {
        ArrayList<FavoritesItem> items = new ArrayList<FavoritesItem>();

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
            final FavoritesItemView view;
            if(convertView == null){
                view =new FavoritesItemView(getActivity());
            }else{
                view=(FavoritesItemView) convertView;
            }
            final FavoritesItem item = items.get(position);
            view.setTitle_tv(item.getTitle());
            view.setStart_tv(item.getStart());
            view.setEnd_tv(item.getEnd());

            final DbHelper dbHelper = new DbHelper(getActivity(),"MEMOJANG.db",null,1);

            view.btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = item.getTitle();

                    Toast.makeText(getActivity(), "즐겨찾기 해제", Toast.LENGTH_LONG).show();
                    dbHelper.deleteFavorites(item.getTitle());
                    items.remove(position);

                    adapter.notifyDataSetChanged();

                    //TabLectureActivity.fragmentReplace(0);
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

            FavoritesItem item = new FavoritesItem();
            // item에 아이템을 setting한다.
            item.setTitle(title);
            item.setStart(start);
            item.setEnd(end);

            // favorite_Items에 item을 추가한다.
            items.add(item);
        }
    }
}

