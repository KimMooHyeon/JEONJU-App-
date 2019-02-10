package com.computer.inu.jeonjuapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class TabLectureActivity extends AppCompatActivity {

    final private String[] TAP_TITLE={"강좌","즐겨찾기"};
    static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        setTitle("평생 강좌");

        TestPagerAdapter tesAdapter = new TestPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(tesAdapter);

        TabLayout tab = findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);

        fm = getSupportFragmentManager();

    }
    public class TestPagerAdapter extends FragmentStatePagerAdapter {
        public TestPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i){
                case 0:
                    return FragmentLecture.newInstance();
                case 1:
                    return FragmentFavorites.newInstance();
                /*case 2:
                    return test3;*/
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return TAP_TITLE[position];
                case 1:
                    return TAP_TITLE[position];
               /* case 2:
                    return TAP_TITLE[position];*/
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static void fragmentReplace(int reqNewFragmentIndex)
    {
        Fragment newFragment = null;
        Log.d("TAG", "fragmentReplace " + reqNewFragmentIndex);

        //TestPagerAdapter tesAdapter = new TestPagerAdapter(getSupportFragmentManager());

        newFragment = FragmentLecture.newInstance();

        // replace fragment
        final FragmentTransaction transaction = fm
                .beginTransaction();

        //transaction.replace(R.id.lectureList, newFragment);



        switch (reqNewFragmentIndex){
            case 0: FragmentLecture.newInstance();
            case 1: FragmentFavorites.newInstance();
        }

        // Commit the transaction
        transaction.commit();
    }
}
