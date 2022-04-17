package com.yanxuwen.dragview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.viewpager2.widget.ViewPager2;

/**
 * ViewPager2 冲突问题处理
 */
public class ViewPager2Container extends RelativeLayout {
    private ViewPager2 mViewPager2;
    private boolean disallowParentInterceptDownEvent = true;
    private int startX = 0;
    private int startY = 0;

    private float touchX, touchY;
    private boolean doublePointerCount;

    public ViewPager2Container(Context context) {
        this(context, null);
    }

    public ViewPager2Container(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPager2Container(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView instanceof ViewPager2) {
                mViewPager2 = (ViewPager2) childView;
                break;
            }
        }
        if (mViewPager2 == null) {
            throw new IllegalStateException("The root child of ViewPager2Container must contains a ViewPager2");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = ev.getX();
                    touchY = ev.getY();
                    if (ev.getPointerCount() > 1) {
                        doublePointerCount = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ev.getPointerCount() > 1) {
                        doublePointerCount = true;
                    }
                    float dx = ev.getX() - touchX;
                    float dy = ev.getY() - touchY;
                    touchX = ev.getX();
                    touchY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //只有最后一只手放开，才能false,如果本来2只手指，突然变化一只手指，这时候也认定为2只手指，避免一些冲突
                    if (ev.getPointerCount() == 1) {
                        doublePointerCount = false;
                    }
                    touchX = 0;
                    touchY = 0;
                    break;
            }
            //双手禁止滚动，由于ViewPager2跟PhotoView双手指很容易冲突，所以禁止双手指滚动
            mViewPager2.setUserInputEnabled(doublePointerCount ? false : true);
        } catch (Exception e) {
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean doNotNeedIntercept = (!mViewPager2.isUserInputEnabled()
                || (mViewPager2.getAdapter() != null
                && mViewPager2.getAdapter().getItemCount() <= 1));
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(!disallowParentInterceptDownEvent);
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = Math.abs(endX - startX);
                int disY = Math.abs(endY - startY);
                if (mViewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY);
                } else if (mViewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY);
                }
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void onHorizontalActionMove(int endX, int disX, int disY) {
        if (mViewPager2.getAdapter() == null) {
            return;
        }
        if (disX > disY) {
            int currentItem = mViewPager2.getCurrentItem();
            int itemCount = mViewPager2.getAdapter().getItemCount();
            if (currentItem == 0 && endX - startX > 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(currentItem != itemCount - 1
                        || endX - startX >= 0);
            }
        } else if (disY > disX) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    private void onVerticalActionMove(int endY, int disX, int disY) {
        if (mViewPager2.getAdapter() == null) {
            return;
        }
        int currentItem = mViewPager2.getCurrentItem();
        int itemCount = mViewPager2.getAdapter().getItemCount();
        if (disY > disX) {
            if (currentItem == 0 && endY - startY > 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
            } else {
                getParent().requestDisallowInterceptTouchEvent(currentItem != itemCount - 1
                        || endY - startY >= 0);
            }
        } else if (disX > disY) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    /**
     * 设置是否允许在当前View的{@link MotionEvent#ACTION_DOWN}事件中禁止父View对事件的拦截，该方法
     * 用于解决CoordinatorLayout+CollapsingToolbarLayout在嵌套ViewPager2Container时引起的滑动冲突问题。
     * <p>
     * 设置是否允许在ViewPager2Container的{@link MotionEvent#ACTION_DOWN}事件中禁止父View对事件的拦截，该方法
     * 用于解决CoordinatorLayout+CollapsingToolbarLayout在嵌套ViewPager2Container时引起的滑动冲突问题。
     *
     * @param disallowParentInterceptDownEvent 是否允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}事件中禁止父View拦截事件，默认值为false
     *                                         true 不允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}时间中禁止父View的时间拦截，
     *                                         设置disallowIntercept为true可以解决CoordinatorLayout+CollapsingToolbarLayout的滑动冲突
     *                                         false 允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}时间中禁止父View的时间拦截，
     */
    void disallowParentInterceptDownEvent(Boolean disallowParentInterceptDownEvent) {
        this.disallowParentInterceptDownEvent = disallowParentInterceptDownEvent;
    }
}
