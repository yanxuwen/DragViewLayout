package com.yanxuwen.dragviewlayout.Test3;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.DragViewLayout;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragview.listener.OnDrawerOffsetListener;
import com.yanxuwen.dragviewlayout.R;
import com.yanxuwen.dragviewlayout.Test1.MyFragment;

import java.util.ArrayList;

public class TestActivity3 extends FragmentActivity {
    public ImageView v1;
    final ArrayList<View> views = new ArrayList<>();
    final ArrayList<Object> listdata = new ArrayList<>();
    final ArrayList<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
    private View view_test3;
    private Context context;
    private DragViewDialog dragViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        context = this;
        v1 = (ImageView) findViewById(R.id.image);
        views.add(v1);
        listfragemnt.add(MyFragment.class);
        listdata.add("sdsds1");
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
        RequestOptions options3 = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH);
        Glide.with(context).load("http://aiyuxm.com/619744a7a1710f3117f4187f_1641631229437_2").into(v1);

    }

    public void open() {
        dragViewDialog = new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt, views)
                .setViewPage2(true)
                .setListener(new Listener() {
                    TextView text_abstract = null;

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        if (position == 0 && listdata.size() < 3) {
                            //更加数据
                            listdata.add("sdsds2");
                            listdata.add("sdsds3");
                            views.add(v1);
                            views.add(v1);
                            listfragemnt.add(MyFragment.class);
                            listfragemnt.add(MyFragment.class);
                            notifyDataSetChanged();
                        }
                        if (text_abstract != null) {
                            text_abstract.setText((position + 1) + "/" + 3 + " 从第一张照片XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                        }

                    }
                }).show();
    }


    public void notifyDataSetChanged() {
        dragViewDialog.notifyDataSetChanged();
    }
}
