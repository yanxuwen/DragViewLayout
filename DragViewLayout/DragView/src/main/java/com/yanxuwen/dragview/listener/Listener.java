package com.yanxuwen.dragview.listener;

import android.view.View;

import androidx.annotation.FloatRange;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Listener {
    /**
     * 拖动偏移量变化为1-0  1为显示状态，0为关闭
     */
    public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
    }

    public void onDragStatus(int status) {
    }

    public void onPageSelected(int position) {
    }

    /**
     * true 代表运行执行onBackPressed，false则禁止onBackPressed
     */
    public boolean onBackPressed() {
        return true;
    }

    /**
     * 处理一些初始化操作
     */
    public void init() {
    }


}
