package com.computer.inu.jeonjuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelfareItemView extends LinearLayout {
    TextView titleText;
    TextView contentsText;

    public WelfareItemView(Context context) {
        super(context);
        inflation_init(context);

        titleText=(TextView)findViewById(R.id.tv_title);
        contentsText=(TextView)findViewById(R.id.tv_contents);
    }

    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_welfare, this,true);
    }
    public void setTitle(String name){
        titleText.setText(name);
        titleText.setSelected(true);
    }
    public void setLifeCycle(String contents){
        contentsText.setText(contents);
        contentsText.setSelected(true);
    }
}
