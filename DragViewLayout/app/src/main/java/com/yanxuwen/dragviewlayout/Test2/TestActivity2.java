package com.yanxuwen.dragviewlayout.Test2;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.DragViewDialog;
import com.yanxuwen.dragview.listener.OnDataListener;
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
        dialogFragment = DragViewDialog.show(this, 0, new OnDataListener() {

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
                    listfragemnt.add(MyFragment2.class);
                    listfragemnt.add(MyFragment2.class);
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
