//package com.yanxuwen.dragview;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//import androidx.viewpager.widget.ViewPager;
//
//public class DragViewPage extends ViewPager {
//    public DragViewLayout mDragViewLayout;
//
//    public DragViewPage(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public DragViewPage(Context context) {
//        super(context);
//
//    }
//
//    public void setDragViewLayout(DragViewLayout mDragViewLayout) {
//        this. mDragViewLayout = mDragViewLayout;
//
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                return false;
//            }
//        }
//        if(!mDragViewLayout.isOpen()||mDragViewLayout.isScrolling())return false;
//        return super.onTouchEvent(ev);
//
//    }
//
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//       super.onPageScrolled(position,positionOffset,positionOffsetPixels);
//        mDragViewLayout.setStop(positionOffset!=0);
//
//    }
//}