package com.computer.inu.jeonjuapp;

public class FavoritesItem {
    String title;
    String start;
    String end;
    int resId;

    /*
    public LectureItem(String title, String start, String end){
        this.title=title;
        this.start=start;
        this.end=end;
    }
    */

    public String getTitle() {
        return title;
    }
    public String getStart(){
        return start;
    }
    public String getEnd(){
        return end;
    }
    public int getResId() {
        return resId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public void setEnd(String end){
        this.end = end;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }

}
