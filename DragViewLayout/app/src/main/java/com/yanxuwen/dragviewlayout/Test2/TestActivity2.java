package com.yanxuwen.dragviewlayout.Test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.yanxuwen.dragview.DragViewActivity;
import com.yanxuwen.dragview.StatusBarUtils;
import com.yanxuwen.dragviewlayout.R;

import java.util.ArrayList;

public class TestActivity2 extends Activity {
    public View v1;
    final ArrayList<View> views=new ArrayList<>();
    final ArrayList<Object> listdata=new ArrayList<>();
    final ArrayList<Class> listfragemnt=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        //非全屏界面，由于跳转到DragViewActivity会隐藏状态栏，然后返回的时候要显示状态栏，就但是整体界面会下滑，所以要调用下面
        StatusBarUtils.setStatusBar(this,R.color.colorPrimary);
        v1=findViewById(R.id.text);
        views.add(v1);
        listfragemnt.add(MyFragment2.class);
        listdata.add("sdsds1");
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }
    public void open(){
        DragViewActivity.startActivity(this,0,new DragViewActivity.OnDataListener() {
            @Override
            public ArrayList<View> getListView() {
                return views;
            }
            @Override
            public ArrayList<Object> getListData() {
                return listdata;
            }
            @Override
            public ArrayList<Class> getListFragmentClass() {
                return listfragemnt;
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0&&listdata.size()<3) {
                    //更加数据
                    listdata.add("sdsds3");
                    listdata.add("sdsds4");
                    views.add(v1);
                    views.add(v1);
                    listfragemnt.add(MyFragment2.class);
                    listfragemnt.add(MyFragment2.class);
                    DragViewActivity.getInstance(TestActivity2.this).notifyDataSetChanged();
                }
            }
            @Override
            public boolean onBackPressed() {
                return true;
            }

            @Override
            public void init() {}
        });
    }
}
