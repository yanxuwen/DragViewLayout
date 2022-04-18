package com.yanxuwen.dragviewlayout.Test1;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends FragmentActivity {
    public View v1;
    public View v2;
    final List<View> views = new ArrayList<>();
    final List<Serializable> listdata = new ArrayList<>();
    final List<Class<? extends Fragment>> listfragemnt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        v1 = findViewById(R.id.text);
        v2 = findViewById(R.id.text2);
        views.add(v1);
        views.add(v2);
        views.add(v1);
        views.add(v2);
        listfragemnt.add(MyFragment.class);
        listfragemnt.add(MyFragment.class);
        listfragemnt.add(MyFragment.class);
        listfragemnt.add(MyFragment.class);


        listdata.add("第一张");
        listdata.add("第二张");
        listdata.add("第三张");
        listdata.add("第四张");
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(0);
            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(1);
            }
        });

    }

    public void open(int position) {
        final DragViewDialog dialog = new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt)
                .setViewPage2(position == 1 ? true : false)
                .setDefaultPosition(position)
                .setBackgroundResource(android.R.color.black)
                .setListener(new Listener<String>() {
                    public View getCurView(int position, String str) {
                        return views.get(position);
                    }
                }).show();
    }

}
