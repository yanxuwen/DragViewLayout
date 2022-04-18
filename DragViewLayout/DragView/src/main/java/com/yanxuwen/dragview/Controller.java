package com.yanxuwen.dragview;

import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yanxuwen.dragview.listener.Listener;

import java.io.Serializable;
import java.util.List;

public class Controller {
    public FragmentActivity fragmentActivity;
    public boolean isViewPage2;//是否使用ViewPage
    public int defaultPosition;//默认项
    public boolean isTransparentView;//是否设置透明View, 也就是当前启动View透明，使得效果更好，更符合拖拽，建议开启
    public List<? extends Serializable> listData;//数据列表
    public List<Class<? extends Fragment>> fragmentClassList;//Fragment.class列表，
    public Listener listener;//监听器
    public @DrawableRes int backgroundResId;//背景资源
    public int bgColor;//背景颜色
    public boolean mCancelable = true;
    public DialogInterface.OnDismissListener mOnDismissListener;
}
