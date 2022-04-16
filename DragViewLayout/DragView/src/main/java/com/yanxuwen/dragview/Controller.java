package com.yanxuwen.dragview;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.listener.Listener;

import java.util.List;

public class Controller {
    public FragmentActivity fragmentActivity;
    public boolean isViewPage2;//是否使用ViewPage
    public int defaultPosition;//默认项
    public List<Object> listData;//数据列表
    public List<Class<? extends Fragment>> fragmentClassList;//Fragment.class列表，
    public List<View> listView;//View 列表，用于展示启动动画跟关闭动画
    public Listener listener;//监听器
}
