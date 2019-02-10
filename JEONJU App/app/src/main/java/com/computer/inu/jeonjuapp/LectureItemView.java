package com.computer.inu.jeonjuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LectureItemView extends LinearLayout {
    TextView title_tv;
    TextView start_tv;
    TextView end_tv;
    ToggleButton btn_star;

    public LectureItemView(Context context) {
        super(context);
        inflation_init(context);

        title_tv=(TextView)findViewById(R.id.title_text);
        start_tv=(TextView)findViewById(R.id.start_text);
        end_tv=(TextView)findViewById(R.id.end_text);
        btn_star=(ToggleButton)findViewById(R.id.button_favorite);
    }
    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_lecture, this,true);
    }
    public void setTitle_tv(String title_text){
        title_tv.setText(title_text);
        title_tv.setSelected(true);
    }
    public void setStart_tv(String start_text){
        start_tv.setText(start_text);
        start_tv.setSelected(true);
    }
    public void setEnd_tv(String end_text){
        end_tv.setText(end_text);
        end_tv.setSelected(true);
    }
}
