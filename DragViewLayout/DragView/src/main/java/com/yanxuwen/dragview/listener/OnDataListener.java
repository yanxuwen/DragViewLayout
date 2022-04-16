package com.yanxuwen.dragview.listener;

import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public interface OnDataListener {

    public View getCurView(int position);

    /**
     * 数据列表
     */
    public ArrayList<Object> getListData();

    /**
     * Frament列表，注意是Frament.class列表
     */
    public ArrayList<Class<? extends Fragment>> getListFragmentClass();

    public void onPageSelected(int position);

    /**
     * true 代表运行执行onBackPressed，false则禁止onBackPressed
     */
    public boolean onBackPressed();

    /**
     * 处理一些初始化操作
     */
    public void init();


}
