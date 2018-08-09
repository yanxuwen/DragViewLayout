package com.yanxuwen.dragview;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public abstract class DragFragment extends Fragment {
    public Object data;
    private String ARG_POSITION = "position";
    public int position = 0;
    int layoutResID;
    public View view;

    public void setBundlePosition(int position) {
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        setArguments(b);
    }
    /**强力建议设置指定的拖曳View,如果不设置，设置为空的话，则默认为整个控件。*/
    public abstract View getDragView();

    public abstract void initView();
    public abstract void init();
    /**当前的拖曳状态*/
    public abstract void onDragStatus(int status);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            position = getArguments().getInt(ARG_POSITION);
        } catch (Exception e) {
        }
    }

    public void setContentView(@LayoutRes int layoutResID) {
        this.layoutResID = layoutResID;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(layoutResID, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initView();
            }
        });
        return view;
    }
}
