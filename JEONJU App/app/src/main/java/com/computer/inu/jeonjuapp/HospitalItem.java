package com.computer.inu.jeonjuapp;

public class HospitalItem {
    private String name;
    private String address;
    private String sort;
    private String tel;
    private String posx;
    private String posy;
    private int id;

    public HospitalItem(String name, String address, String sort, String tel, String posx, String posy, int id) {
        this.name = name;
        this.address = address;
        this.sort = sort;
        this.tel = tel;
        this.posx = posx;
        this.posy = posy;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
    public String getSort(){
        return sort;
    }
    public int getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSort(String sort){
        this.sort=sort;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPosx() {
        return posx;
    }

    public void setPosx(String posx) {
        this.posx = posx;
    }

    public String getPosy() {
        return posy;
    }

    public void setPosy(String posy) {
        this.posy = posy;
    }
}
