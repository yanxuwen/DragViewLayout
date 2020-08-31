package com.yanxuwen.dragview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanxuwen on 2018/6/8.
 */

public class DragViewActivity extends FragmentActivity implements DragViewLayout.OnDrawerStatusListener, DragViewLayout.OnCurViewListener, ViewPager.OnPageChangeListener {
    private Context currentKey;

    public interface OnDataListener {

        public View getCurView(int position);

        /**
         * 数据列表
         */
        public ArrayList<Object> getListData();

        /**
         * Frament列表，注意是Frament.class列表
         */
        public ArrayList<Class> getListFragmentClass();

        public void onPageSelected(int position);

        /**
         * true 代表运行执行onBackPressed，false则禁止onBackPressed
         */
        public boolean onBackPressed();

        /**
         * 处理一些初始化操作
         */
        public void init();

    }

    static Map<Context, OnDataListener> map_OnDataListener;
    OnDataListener mOnDataListener = null;

    /**
     * 拖动偏移量
     */
    public interface OnDrawerOffsetListener {
        /**
         * 拖动偏移量变化为1-0  1为显示状态，0为关闭
         */
        public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset);
    }

    OnDrawerOffsetListener mOnDrawerOffsetListener = null;

    public void setOnDrawerOffsetListener(OnDrawerOffsetListener l) {
        mOnDrawerOffsetListener = l;
    }

    public void removeOnDrawerOffsetListener() {
        mOnDrawerOffsetListener = null;
    }

    private DragViewLayout dragViewLayout;
    public ImageView bgimg;

    public DragViewPage viewPager;
    public DragStatePagerAdapter mMPagerAdapter;
    public int currentPosition;
    private static Map<Context, DragViewActivity> map_instance;
    private Handler mHandler;

    public static DragViewActivity getInstance(Context context) {
        if (map_instance != null && map_instance.containsKey(context)) {
            return map_instance.get(context);
        }
        return null;
    }

    public static void startActivity(Activity context, int position, OnDataListener l) {
        if (map_instance == null) map_instance = new HashMap<>();
        map_instance.put(context, null);
        if (map_OnDataListener == null) map_OnDataListener = new HashMap<>();
        map_OnDataListener.put(context, l);
        Intent intent = new Intent(context, DragViewActivity.class);
        DragViewActivity.getExtra(intent, position);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    public void notifyDataSetChanged() {
        mMPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DragStatusBarUtils.setTranslucentForImageViewInFragment(this, 0, null);//状态栏透明
        mHandler = new Handler();
        setContentView(R.layout.dragview_);
        dragViewLayout = (DragViewLayout) findViewById(R.id.dragLayout);
        bgimg = (ImageView) findViewById(R.id.bgimg);

        dragViewLayout.setOnDrawerStatusListener(this);
        dragViewLayout.setOnCurViewListener(this);
        dragViewLayout.setOnDrawerOffsetListener(new DragViewLayout.OnDrawerOffsetListener() {
            @Override
            public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                if (mOnDrawerOffsetListener != null) mOnDrawerOffsetListener.onDrawerOffset(offset);
            }
        });

        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        viewPager = (DragViewPage) findViewById(R.id.viewPager);
        viewPager.setDragViewLayout(dragViewLayout);
        setDragView(viewPager);
        setBackgroundColor(android.R.color.black);
        init();

    }

    public void init() {
        if (map_instance != null && map_instance.containsValue(null)) {
            for (Context getKey : map_instance.keySet()) {
                if (map_instance.get(getKey) == null) {
                    map_instance.put(getKey, this);
                    currentKey = getKey;
                    break;
                }
            }
        }
        if (map_OnDataListener == null) {
            finish();
            return;
        }
        mOnDataListener = map_OnDataListener.get(currentKey);
        if (mOnDataListener == null) {
            finish();
            return;
        }
        mMPagerAdapter = new DragStatePagerAdapter(getSupportFragmentManager(), mOnDataListener != null ? mOnDataListener.getListFragmentClass() : null, mOnDataListener != null ? mOnDataListener.getListData() : null);
        viewPager.setAdapter(mMPagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                onPageSelected(currentPosition);
            }
        });
        if (mOnDataListener != null) mOnDataListener.init();

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (layoutResID == R.layout.dragview_) {
            super.setContentView(layoutResID);
        } else {
            LayoutInflater.from(this).inflate(layoutResID, dragViewLayout, true);
        }
    }

    public void setDragView(View dragview) {
        dragViewLayout.setDragView(dragview);
    }


    protected static Intent getExtra(Intent intent, int currentPosition) {
        intent.putExtra("currentPosition", currentPosition);
        return intent;
    }

    public int getCount() {
        return mMPagerAdapter.getCount();
    }

    public ArrayList<DragViewLayout.ImageBean> getImageBeans(View[] views) {
        ArrayList<DragViewLayout.ImageBean> imageBeans = new ArrayList<>();
        for (View view : views) {
            DragViewLayout.ImageBean imageBean = new DragViewLayout.ImageBean();
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            imageBean.left = location[0];
            imageBean.top = location[1];
            imageBean.width = view.getWidth();
            imageBean.height = view.getHeight();
//            imageBeans[i] = imageBean;
            imageBeans.add(imageBean);
        }
        return imageBeans;
    }

    public DragViewLayout.ImageBean getImageBean(View view) {
        if (view == null) return null;
        DragViewLayout.ImageBean imageBean = new DragViewLayout.ImageBean();
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        imageBean.left = location[0];
        imageBean.top = location[1];
        imageBean.width = view.getWidth();
        imageBean.height = view.getHeight();
        return imageBean;
    }

    @Override
    public void onStatus(int status) {
        if (status == DragViewLayout.CLOSE) {
            onFinish();
        }
        DragFragment mFragment = (DragFragment) mMPagerAdapter.getItem(currentPosition);
        mFragment.onDragStatus(status);

    }

    public void setBackgroundColor(int color) {
        bgimg.setBackgroundColor(getResources().getColor(color));
    }

    @Override
    public void onBackPressed() {
        if (mOnDataListener != null && !mOnDataListener.onBackPressed()) {
            return;
        }
        dragViewLayout.close();
    }

    public void onFinish() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dragViewLayout.removeOnDrawerStatusListener();
        dragViewLayout.removeOnCurViewListener();
        dragViewLayout.removeOnDrawerOffsetListener();
        removeOnDrawerOffsetListener();
        mHandler = null;
        mOnDataListener = null;
        if (map_instance != null && map_instance.containsValue(this)) {
            for (Context getKey : map_instance.keySet()) {
                if (map_instance.get(getKey) == this) {
                    map_instance.remove(getKey);
                    if (map_instance.size() == 0) map_instance = null;
                    break;
                }
            }
        }
        if (map_OnDataListener != null && map_OnDataListener.containsKey(currentKey)) {
            map_OnDataListener.remove(currentKey);
            if (map_OnDataListener.size() == 0) map_OnDataListener = null;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) {
            dragViewLayout.setStop(false);
            currentPosition = position;
            DragFragment mFragment = (DragFragment) mMPagerAdapter.getItem(position);
            dragViewLayout.setScaleView(mFragment.getDragView());
            mFragment.init();
            dragViewLayout.setStartView(getCurView());
            if (mFragment.getDragView() instanceof PinchImageView) {
                ((PinchImageView) mFragment.getDragView()).setDragViewLayout(dragViewLayout);
            }
        } else {
            dragViewLayout.setStop(true);
        }
    }

    @Override
    public void onPageSelected(final int position) {
        if (mOnDataListener != null) mOnDataListener.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public DragViewLayout.ImageBean getCurView() {
        if (mOnDataListener != null) {
            return getImageBean(mOnDataListener != null ? mOnDataListener.getCurView(currentPosition) : null);
        }
        return null;
    }

    public DragViewLayout getDragViewLayout() {
        return dragViewLayout;
    }
}
