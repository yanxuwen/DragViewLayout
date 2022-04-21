package com.yanxuwen.dragviewlayout.Test2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.DragViewLayout;
import com.yanxuwen.dragview.listener.Listener;
import com.yanxuwen.dragviewlayout.ColorData;
import com.yanxuwen.dragviewlayout.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestActivity2 extends FragmentActivity {
    public View v1;
    final List<View> views = new ArrayList<>();
    final List<ColorData> listdata = new ArrayList<>();
    final List<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
    private View view_test3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        v1 = findViewById(R.id.text);
        views.add(v1);
        listfragemnt.add(MyFragment2.class);
        listdata.add(new ColorData(R.color.colorAccent, "粉红色"));
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
            }
        });
    }

    DragViewDialog dialog;

    public void open() {
        dialog = new DragViewDialog.Builder(this)
                .setData(listdata, listfragemnt)
                .setViewPage2(true)
                .setBackgroundColor(Color.parseColor("#333333"))
                .setListener(new Listener<ColorData>() {
                    TextView text_abstract = null;

                    @Override
                    public void init() {
                        if (view_test3 == null) {
                            super.init();
                            view_test3 = getLayoutInflater().inflate(R.layout.view_test3, null);
                            dialog.getParent().addView(view_test3);
                            text_abstract = (TextView) view_test3.findViewById(R.id.text);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        if (position == 0 && listdata.size() < 3) {
                            //动态更加数据
                            listdata.add(new ColorData(R.color.colorPrimary, "蓝色"));
                            listdata.add(new ColorData(R.color.colorPrimaryDark, "深蓝色"));
                            views.add(v1);
                            views.add(v1);
                            listfragemnt.add(MyFragment2.class);
                            listfragemnt.add(MyFragment2.class);
                            dialog.notifyDataSetChanged();
                        }
                        text_abstract.setText((position + 1) + "/" + 3 + " 照片XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    }

                    @Override
                    public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                        super.onDrawerOffset(offset);
                        if (text_abstract != null) {
                            text_abstract.setAlpha(offset - 0.3f);
                        }
                    }

                    @Override
                    public void onDragStatus(int status) {
                        super.onDragStatus(status);
                        if (status == DragViewLayout.CLOSE) {
                            view_test3 = null;

                        }
                    }
                })
                .show();
    }
}
