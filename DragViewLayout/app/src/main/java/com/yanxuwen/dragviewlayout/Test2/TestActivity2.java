package com.yanxuwen.dragviewlayout.Test2;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.R;

import java.util.ArrayList;

public class TestActivity2 extends FragmentActivity {
    public View v1;
    final ArrayList<View> views = new ArrayList<>();
    final ArrayList<Object> listdata = new ArrayList<>();
    final ArrayList<Class<? extends Fragment>> listfragemnt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        v1 = findViewById(R.id.text);
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

    DragViewDialog dialogFragment;

    public void open() {
        new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt, views)
                .setViewPage2(true)
                .show();
    }
}
