package com.yanxuwen.dragviewlayout.Test1;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.R;
import com.yanxuwen.dragviewlayout.Test3.PictureData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends FragmentActivity {
    public ImageView image1;
    public View layout1;
    public View layout2;
    final List<View> views = new ArrayList<>();
    final List<PictureData> listdata = new ArrayList<>();
    final List<Class<? extends Fragment>> listfragemnt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        layout1 = findViewById(R.id.layout1);
        image1 = findViewById(R.id.image1);
        layout2 = findViewById(R.id.layout2);
        views.add(layout1);
        views.add(layout2);
        listfragemnt.add(MyFragment.class);
        listfragemnt.add(MyFragment.class);

        PictureData pictureData1 = new PictureData(R.mipmap.test, "第一张");
        PictureData pictureData2 = new PictureData(R.mipmap.test, "第二张");

        listdata.add(pictureData1);
        listdata.add(pictureData2);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(0);
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(1);
            }
        });

        Glide.with(this).load(pictureData1.getIdRes()).into(image1);

    }

    public void open(int position) {
        final DragViewDialog dialog = new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt)
                .setViewPage2(position == 1 ? true : false)
                .setDefaultPosition(position)
                .setBackgroundResource(android.R.color.black)
                .setListener(new Listener<PictureData>() {
                    public View getCurView(int position, PictureData data) {
                        return views.get(position);
                    }
                }).show();
    }

}
