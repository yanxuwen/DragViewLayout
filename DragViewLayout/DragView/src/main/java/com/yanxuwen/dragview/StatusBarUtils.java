package com.yanxuwen.dragview;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by yanxw on 2018/8/9.
 */

public class StatusBarUtils {
    /**
     *  非全屏界面，由于跳转到DragViewActivity会隐藏状态栏，然后返回的时候要显示状态栏，就但是整体界面会下滑，所以要调用下面
     */
    public static void setStatusBar(Activity context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(context);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色（该颜色根据你的App主题自行更改）
            rectView.setBackgroundColor(ContextCompat.getColor(context, id));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }
}
