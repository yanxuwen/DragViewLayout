package com.yanxuwen.dragviewlayout.Test3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yanxuwen.dragview.DragViewDialogFragment;
import com.yanxuwen.dragview.DragViewLayout;
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
    private DragViewDialogFragment dialogFragment;

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
        dialogFragment = DragViewDialogFragment.show(this, 0, new DragViewDialogFragment.OnDataListener() {
            TextView text_abstract = null;

            @Override
            public View getCurView(int position) {
                return views.get(position);
            }

            @Override
            public ArrayList<Object> getListData() {
                return listdata;
            }

            @Override
            public ArrayList<Class<? extends Fragment>> getListFragmentClass() {
                return listfragemnt;
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && listdata.size() < 3) {
                    //更加数据
                    listdata.add("sdsds3");
                    listdata.add("sdsds4");
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

            @Override
            public boolean onBackPressed() {
                return true;
            }

            @Override
            public void init() {
                if (view_test3 == null) {
                    view_test3 = getLayoutInflater().inflate(R.layout.view_test3, null);
                }
                dialogFragment.getParent().addView(view_test3);
                text_abstract = (TextView) view_test3.findViewById(R.id.text);
                dialogFragment.setOnDrawerOffsetListener(new DragViewDialogFragment.OnDrawerOffsetListener() {
                    @Override
                    public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                        if (view_test3 != null) {
                            view_test3.setAlpha(offset - 0.3f);
                        }
                    }

                    @Override
                    public void onDragStatus(int status) {
                        if (status == DragViewLayout.CLOSE) {
                            dialogFragment.getDragViewLayout().removeView(view_test3);
                            view_test3 = null;
                        }
                    }
                });
            }
        });
    }


    public void notifyDataSetChanged() {
        dialogFragment.notifyDataSetChanged();
    }
}
