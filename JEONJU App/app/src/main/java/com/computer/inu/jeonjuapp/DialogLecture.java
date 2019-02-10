package com.computer.inu.jeonjuapp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogLecture {
    private Context context;

    public DialogLecture(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String title, String who, ArrayList<String> content) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_lecture);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView title_text = (TextView) dlg.findViewById(R.id.title_text);
        final TextView who_text = (TextView) dlg.findViewById(R.id.who_text);
        final TextView content_text1 = (TextView) dlg.findViewById(R.id.content_text1);
        final TextView content_text2 = (TextView) dlg.findViewById(R.id.content_text2);
        final TextView content_text3 = (TextView) dlg.findViewById(R.id.content_text3);
        final TextView content_text4 = (TextView) dlg.findViewById(R.id.content_text4);
        final TextView content_text5 = (TextView) dlg.findViewById(R.id.content_text5);
        final TextView content_text6 = (TextView) dlg.findViewById(R.id.content_text6);
        final TextView content_text7 = (TextView) dlg.findViewById(R.id.content_text7);
        final TextView content_text8 = (TextView) dlg.findViewById(R.id.content_text8);
        final TextView content_text9 = (TextView) dlg.findViewById(R.id.content_text9);

        final Button okButton = (Button) dlg.findViewById(R.id.okButton);

        title_text.setText(title);
        who_text.setText(who);

        content_text1.setText(content.get(0));
        content_text2.setText(content.get(1));
        content_text3.setText(content.get(2));
        content_text4.setText(content.get(3));
        content_text5.setText(content.get(4));
        content_text6.setText(content.get(5));
        content_text7.setText(content.get(6));
        content_text8.setText(content.get(7));
        content_text9.setText(content.get(8));


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();

            }
        });

    }
}