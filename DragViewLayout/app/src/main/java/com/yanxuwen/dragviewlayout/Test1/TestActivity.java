package com.yanxuwen.dragviewlayout.Test1;


import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialogFragment;
import com.yanxuwen.dragviewlayout.R;

import java.util.ArrayList;

public class TestActivity extends FragmentActivity {
    public View v1;
    public View v2;
    final ArrayList<View> views = new ArrayList<>();
    final ArrayList<Object> listdata = new ArrayList<>();
    final ArrayList<Class<? extends Fragment>> listfragemnt = new ArrayList<>();
    private DragViewDialogFragment dialogFragment;

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
        dialogFragment = DragViewDialogFragment.show(this, position, new DragViewDialogFragment.OnDataListener() {
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
                if (position == 1 && listdata.size() < 4) {
                    //更加数据
                    listdata.add("sdsds3");
                    listdata.add("sdsds4");
                    views.add(v1);
                    views.add(v2);
                    listfragemnt.add(MyFragment.class);
                    listfragemnt.add(MyFragment.class);
                    notifyDataSetChanged();
                }
            }

            @Override
            public boolean onBackPressed() {
                return true;
            }

            @Override
            public void init() {
            }
        });
    }

    public void notifyDataSetChanged() {
        dialogFragment.notifyDataSetChanged();
    }

}
