package com.example.yuekao;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private RadioGroup radiogroup;
    private List<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

        list = new ArrayList<View>();
        View view1 = View.inflate(MainActivity.this, R.layout.image1, null);
        View view2 = View.inflate(MainActivity.this, R.layout.image2, null);
        View view3 = View.inflate(MainActivity.this, R.layout.image3, null);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        ImageView tiaozhaun = (ImageView) view3.findViewById(R.id.tiaozhuan);
        tiaozhaun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

        viewpager.setAdapter(new adapter());

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            for (int i=0;i<list.size();i++){
                RadioButton button = (RadioButton) radiogroup.getChildAt(position);
                if (i==position){
                    button.setChecked(true);
                }
            }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i=0;i<list.size();i++){
                    RadioButton button = (RadioButton) radiogroup.getChildAt(i);
                    if (button.isChecked()){
                        viewpager.setCurrentItem(i);
                    }
                }
            }
        });


    }
    class adapter extends PagerAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = list.get(position);
            container.addView(view);
            return view;
        }
    }
}
