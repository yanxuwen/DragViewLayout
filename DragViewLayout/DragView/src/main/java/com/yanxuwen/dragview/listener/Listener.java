package com.yanxuwen.dragview.listener;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;

public class Listener<T> {
    /**
     * 处理一些初始化操作
     */
    @CallSuper
    public void init() {
    }

    /**
     * 拖动偏移量变化为1-0  1为显示状态，0为关闭
     */
    @CallSuper
    public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
    }

    /**
     * 拖动状态
     *
     * @param status 参考
     *               DragViewLayout.OPEN
     *               DragViewLayout.CLOSE
     *               DragViewLayout.DRAG
     */
    @CallSuper
    public void onDragStatus(int status) {
    }

    @CallSuper
    public void onPageSelected(int position) {
    }

    /**
     * 联动View,默认为null，则不能随意拖拽效果，只能上下滑动关闭（类似与今日头条效果）
     * 因为找不可以联动的View,
     */
    public View getCurView(int position, T t) {
        return null;
    }


}
