package com.computer.inu.jeonjuapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    //db헬퍼의 생성자로 관리할 db이름과 버젼 정보를 받는부분
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);

    }

    //db를 생성하는 부분
    @Override
    public void onCreate(SQLiteDatabase db) {

        //강의, 즐겨찾기
        db.execSQL("CREATE TABLE FAVORITES (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, contents TEXT, addr TEXT, tel Text, startR TEXT, endR TEXT," +
                " startStudy TEXT, endStudy TEXT, day TEXT, startTime TEXT, endTime TEXT, who TEXT, center TEXT, money Text, teach Text, max Text);"); //db쿼리문
        db.execSQL("CREATE TABLE LECTURES (_id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT, contents TEXT, addr TEXT, tel Text, startR TEXT, endR TEXT," +
                " startStudy TEXT, endStudy TEXT, day TEXT, startTime TEXT, endTime TEXT, who TEXT, center TEXT, money Text, teach Text, max Text);");

        //복지 서비스
        db.execSQL("CREATE TABLE WELFARE (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, contents TEXT, memo TEXT, tel Text, name TEXT, style TEXT," +
                " receipt TEXT, department TEXT, paper TEXT, isApplication TEXT, howApplication TEXT, howSupport TEXT, who TEXT, lifeCycle Text);");

        //연락처
        db.execSQL("CREATE TABLE CONTACTS (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, tel TEXT);");
        //병원 테이블 생성
        db.execSQL("CREATE TABLE HOSPITAL (hospital_id INTEGER PRIMARY KEY, hospital_name TEXT," +
                "hospital_address TEXT,hospital_sort TEXT, hospital_tel TEXT, " +
                "hospital_posx TEXT, hospital_posy TEXT, hospital_date TEXT, " +
                "hospital_stime TEXT,hospital_etime TEXT,hospital_is_emergency TEXT," +
                "hospital_emer_stime TEXT, hospital_emer_etime TEXT,"+
                "hospital_is_holiday TEXT, hospital_holi_stime TEXT, hospital_holi_etime TEXT," +
                "hospital_memo TEXT);");
    }
    //병원/약국 정보를 주소와 분류로 검색(커서 리턴)
    public Cursor hospitalGetAt(String hospital_address, String hospital_sort){
        SQLiteDatabase db = getWritableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("SELECT * FROM HOSPITAL WHERE hospital_address='"+hospital_address+"'AND hospital_sort='"+hospital_sort+"';",null);
        return cursor;
    }
    //기존db를 삭제하고 새로운 버젼의 db로 교체하는 부분
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //병원/약국 정보 삽입
    public void hospitalInsert(String name, String address, String sort,
                               String tel,String posx, String posy,
                               String date, String stime, String etime,
                               String isEmergency, String emerStime, String emerEtime,
                               String isHoliday, String holidayStime, String holidayEtime, String memo, int mediSid){

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO HOSPITAL VALUES('"+mediSid+"','"+name+"','"+address+"','"+sort+"'," +
                "'"+tel+"','"+posx+"','"+posy+"','"+date+"'," +
                "'"+stime+"','"+etime+"','"+isEmergency+"','"+emerStime+"','"+emerEtime+"'," +
                "'"+isHoliday+"'," + "'"+holidayStime+"','"+holidayEtime+"','"+memo+"');");
        close();
    }
    //병원/약국의 개수를 리턴
    public int hospitalGetCount(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM HOSPITAL;" ,null);
        int result=0;
        while(cursor.moveToNext()){
            if(cursor.getString(0)!=null) {
                result=Integer.parseInt(cursor.getString(0));
            }
        }
        return result;
    }
    //병원/약국의 분류로 검색(커서 리턴)
    public Cursor hospitalGetCursor(String[] hospital_sort){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor;
        String sql="SELECT * FROM HOSPITAL WHERE hospital_sort='"+hospital_sort[0]+"'";
        for(int i=1; i<hospital_sort.length; i++){
            sql+="OR hospital_sort='"+hospital_sort[i]+"'";
        }
        sql+=";";
        cursor = db.rawQuery(sql ,null);
        return cursor;
    }

    //강좌, 즐겨찾기, 복지서비스 삽입
    public void insertLecture(String table, String title, String contents, String addr, String tel, String startR, String endR, String startStudy, String endStudy,
                              String day, String startTime, String endTime, String who, String center, String money, String teach, String max){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO "+table+" VALUES(null,'"+title+"','"+contents+"','"+addr+"','"+tel+"','"+startR+"','"+endR+"'," +
                "'"+startStudy+"','"+endStudy+"','"+day+"','"+startTime+"','"+endTime+"','"+who+"','"+center+"','"+money+"','"+teach+"','"+max+"');");
        close();
    }
    public void insertFavorites(String title, String contents, String addr, String tel, String startR, String endR, String startStudy, String endStudy,
                                String day, String startTime, String endTime, String who, String center, String money, String teach, String max){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO FAVORITES VALUES(null,'"+title+"','"+contents+"','"+addr+"','"+tel+"','"+startR+"','"+endR+"'," +
                "'"+startStudy+"','"+endStudy+"','"+day+"','"+startTime+"','"+endTime+"','"+who+"','"+center+"','"+money+"','"+teach+"','"+max+"');");
        close();
    }
    public void insertWelfare(String table, String title, String contents, String memo, String tel, String name, String style, String receipt, String department,
                              String paper, String isApplication, String howApplication, String howSupport, String who, String lifeCycle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO "+table+" VALUES(null,'"+title+"','"+contents+"','"+memo+"','"+tel+"','"+name+"','"+style+"'," +
                "'"+receipt+"','"+department+"','"+paper+"','"+isApplication+"','"+howApplication+"','"+howSupport+"','"+who+"','"+lifeCycle+"');");
        close();
    }

    //연락처 삽입
    public void insertContacts(String name, String tel){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CONTACTS VALUES(null,'"+name+"','"+tel+"');");
        close();
    }

    //해당 row를 삭제
    public void delete(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM FAVORITES WHERE title = '"+title+"';");
    }

    //해당 row를 삭제
    public void deleteContacts(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CONTACTS WHERE name = '"+title+"';");
    }

    //해당 row를 수정
    public void update(String contents, String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE FAVORITES SET contents='"+contents+"' WHERE title= '"+title+"';" );
    }


    //db조회
    public String getResult(){

        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        int i = 0;
        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM FAVORITES",null);


        while(cursor.moveToNext()){
            //result +=cursor.getString(0)+" | "+cursor.getString(1)+" | "+cursor.getString(2)+" | "+ cursor.getString(3)+" | "+ cursor.getString(4)+"\n";
            result += cursor.getString(2);
        }
        return result;

    }


    //----------------------------------------------------------------------------------강좌 db조회
    public ArrayList<LectureItem> getResultList_Lecture(){

        ArrayList<LectureItem> List = new ArrayList<LectureItem>();
        List.clear();

        SQLiteDatabase db = getReadableDatabase();

        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM LECTURES",null);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    LectureItem Item = new LectureItem();

                    Item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    Item.setStart(cursor.getString(cursor.getColumnIndex("startStudy")));
                    Item.setEnd(cursor.getString(cursor.getColumnIndex("endStudy")));

                    List.add(Item);

                } while (cursor.moveToNext());
            }
        }

        return List;
    }


    //------------------------------------------------------------------------------즐겨찾기 db조회
    public ArrayList<FavoritesItem> getResultList_Favorites(){

        ArrayList<FavoritesItem> favoritesList = new ArrayList<FavoritesItem>();
        favoritesList.clear();

        SQLiteDatabase db = getReadableDatabase();

        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM FAVORITES",null);

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    FavoritesItem favoritesItem = new FavoritesItem();

                    favoritesItem.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    favoritesItem.setStart(cursor.getString(cursor.getColumnIndex("startStudy")));
                    favoritesItem.setEnd(cursor.getString(cursor.getColumnIndex("endStudy")));

                    favoritesList.add(favoritesItem);

                } while (cursor.moveToNext());
            }
        }

        return favoritesList;
    }


    //----------------------------------------------------------------------------------복지 db조회
    public ArrayList<WelfareItem> getResultList_Welfare(){

        ArrayList<WelfareItem> List = new ArrayList<WelfareItem>();
        List.clear();

        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        int i = 0;
        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM WELFARE",null);


        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    WelfareItem Item = new WelfareItem();

                    Item.setTitle(cursor.getString(cursor
                            .getColumnIndex("title")));
                    Item.setContents(cursor.getString(cursor
                            .getColumnIndex("lifeCycle")));

                    List.add(Item);

                } while (cursor.moveToNext());
            }
        }

        return List;
    }


    //----------------------------------------------------------------------즐겨찾기 체크 여부 조회
    public int getFavorites(String title){
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor cursor = db.rawQuery("SELECT count(title) FROM FAVORITES WHERE title= '"+title+"';",null);
        cursor.moveToNext();
        result = cursor.getInt(0);

        return result;
    }

    //--------------------------------------------------------------------------------삭제
    public void deleteFavorites(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM FAVORITES WHERE title = '"+title+"';");
    }

    public void deleteAllLecture(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM LECTURES;");
    }

    public void deleteAllWelfare(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM WELFARE;");
    }

    //-------------------------------------------------------------------------강좌 상세정보 db조회
    public ArrayList<String> getResultList_Lecture2(String title){

        ArrayList<String> List = new ArrayList<String>();
        List.clear();

        SQLiteDatabase db = getReadableDatabase();

        int i = 0;
        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM LECTURES WHERE title= '"+title+"'",null);


        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    List.add(cursor.getString(cursor
                            .getColumnIndex("title")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("contents")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("addr")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("tel")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("startR")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("endR")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("startStudy")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("endStudy")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("day")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("startTime")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("endTime")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("who")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("center")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("money")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("teach")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("max")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("startTime")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("endTime")));


                } while (cursor.moveToNext());
            }
        }

        return List;
    }


    //---------------------------------------------------------복지 상세정보 db조회
    public ArrayList<String> getResultList_Welfare2(String title){

        ArrayList<String> List = new ArrayList<String>();
        List.clear();

        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        int i = 0;
        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM WELFARE WHERE title= '"+title+"'",null);


        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    List.add(cursor.getString(cursor
                            .getColumnIndex("title")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("contents")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("lifeCycle")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("who")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("name")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("tel")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("howApplication")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("howSupport")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("contents")));
                    List.add(cursor.getString(cursor
                            .getColumnIndex("department")));


                } while (cursor.moveToNext());
            }
        }

        return List;
    }


    //-------------------------------------------------------------------연락처 db조회
    public ArrayList<ListViewItem> getResultList_Contacts(){

        ArrayList<ListViewItem> List = new ArrayList<ListViewItem>();
        List.clear();

        SQLiteDatabase db = getReadableDatabase();

        //DB에 있는 데이터를 쉽게 처리하기 위해 Curser을 사용하여 테이블에 있는 모든 데이터를 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Contacts",null);


        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {

                    List.add(new  ListViewItem(cursor.getString(cursor
                            .getColumnIndex("name")),
                            cursor.getString(cursor
                                    .getColumnIndex("tel"))));

                } while (cursor.moveToNext());
            }
        }

        return List;
    }

}
