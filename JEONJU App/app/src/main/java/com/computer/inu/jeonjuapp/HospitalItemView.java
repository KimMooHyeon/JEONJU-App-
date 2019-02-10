package com.computer.inu.jeonjuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HospitalItemView extends LinearLayout {
    TextView nameText;
    TextView addressText;
    TextView sortText;
    TextView telText;

    public HospitalItemView(Context context) {
        super(context);
        inflation_init(context);
        nameText=(TextView)findViewById(R.id.nameText);
        addressText=(TextView)findViewById(R.id.addressText);
        sortText=(TextView)findViewById(R.id.sortText);
        telText=(TextView)findViewById(R.id.telText);
    }
    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_element_layout, this,true);
    }
    public void setAddress(String address){
        addressText.setText(address);
        addressText.setSelected(true);
    }
    public void setName(String name){
        nameText.setText(name);
        nameText.setSelected(true);
    }
    public void setSort(String sort){
        sortText.setText(sort);
    }
    public void setTel(String tel){
        telText.setText(tel);
    }
}