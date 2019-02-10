package com.computer.inu.jeonjuapp;

public class ListViewItem {
    private String name;
    private String tel;


    public ListViewItem(String name, String tel){
        this.name = name;
        this.tel = tel;
    }


    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

}