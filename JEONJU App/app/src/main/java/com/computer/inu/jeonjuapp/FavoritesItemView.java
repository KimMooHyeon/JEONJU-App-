package com.computer.inu.jeonjuapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoritesItemView extends LinearLayout {
    TextView title_tv;
    TextView start_tv;
    TextView end_tv;
    Button btn_del;


    public FavoritesItemView(Context context) {
        super(context);
        inflation_init(context);

        title_tv=(TextView)findViewById(R.id.title_text);
        start_tv=(TextView)findViewById(R.id.start_text);
        end_tv=(TextView)findViewById(R.id.end_text);
        btn_del=(Button) findViewById(R.id.button_delete);
    }
    private void inflation_init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_favorites, this,true);
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
