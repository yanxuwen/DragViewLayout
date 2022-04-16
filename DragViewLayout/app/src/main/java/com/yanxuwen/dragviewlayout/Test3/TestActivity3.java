package com.yanxuwen.dragviewlayout.Test3;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.R;
import com.yanxuwen.dragviewlayout.Test1.MyFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestActivity3 extends FragmentActivity {
    public ImageView v1;
    final List<View> views = new ArrayList<>();
    final List<Serializable> listdata = new ArrayList<>();
    final List<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
    private Context context;
    private DragViewDialog dragViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        context = this;
        v1 = (ImageView) findViewById(R.id.image);
        views.add(v1);
        listfragemnt.add(MyFragment3.class);
        PictureData pictureData = new PictureData(R.mipmap.longphoto, "长图");
        listdata.add(pictureData);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });

        Glide.with(context).load(pictureData.getIdRes()).into(v1);

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
