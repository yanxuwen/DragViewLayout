package com.yanxuwen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class DragViewPager extends ViewPager {

    public DragViewPager(Context context) {
        this(context, null);
    }

    public DragViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 解决嵌套PhotoView，会闪退问题
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
