package com.yanxuwen.dragviewlayout.Test1;


import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.R;

import java.util.ArrayList;

public class TestActivity extends FragmentActivity {
    public View v1;
    public View v2;
    final ArrayList<View> views = new ArrayList<>();
    final ArrayList<Object> listdata = new ArrayList<>();
    final ArrayList<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
    private DragViewDialog dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        v1 = findViewById(R.id.text);
        v2 = findViewById(R.id.text2);
        views.add(v1);
        views.add(v2);
        listfragemnt.add(MyFragment.class);
        listfragemnt.add(MyFragment.class);

        listdata.add("sdsds1");
        listdata.add("sdsds2");
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
        new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt, views)
                .setViewPage2(true)
                .setDefaultPosition(position)
                .setListener(new Listener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                    }
                }).show();
    }

}
