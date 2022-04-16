package com.yanxuwen.dragview.listener;


import androidx.annotation.FloatRange;

/**
 * 拖动偏移量
 */
public interface OnDrawerOffsetListener {
    /**
     * 拖动偏移量变化为1-0  1为显示状态，0为关闭
     */
    void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset);

    void onDragStatus(int status);
}
