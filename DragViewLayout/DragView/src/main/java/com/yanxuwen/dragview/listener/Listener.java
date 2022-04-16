package com.yanxuwen.dragview.listener;

import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;

public class Listener {
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

    @CallSuper
    public void onDragStatus(int status) {
    }

    @CallSuper
    public void onPageSelected(int position) {
    }

    /**
     * true 代表运行执行onBackPressed，false则禁止onBackPressed
     */
    public boolean onBackPressed() {
        return true;
    }


}
