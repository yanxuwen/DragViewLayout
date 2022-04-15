package com.yanxuwen.dragview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class DragViewDialogFragment extends DialogFragment implements DragViewLayout.OnDrawerStatusListener, DragViewLayout.OnCurViewListener, ViewPager.OnPageChangeListener {

    public interface OnDataListener {

        public View getCurView(int position);

        /**
         * 数据列表
         */
        public ArrayList<Object> getListData();

        /**
         * Frament列表，注意是Frament.class列表
         */
        public ArrayList<Class<? extends Fragment>> getListFragmentClass();

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

    DragViewDialogFragment.OnDataListener mOnDataListener = null;

    public void setOnDataListener(DragViewDialogFragment.OnDataListener l) {
        mOnDataListener = l;
    }

    public void removeOnDataListener() {
        mOnDataListener = null;
    }

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

    DragViewDialogFragment.OnDrawerOffsetListener mOnDrawerOffsetListener = null;

    public void setOnDrawerOffsetListener(DragViewDialogFragment.OnDrawerOffsetListener l) {
        mOnDrawerOffsetListener = l;
    }

    public void removeOnDrawerOffsetListener() {
        mOnDrawerOffsetListener = null;
    }

    private DragViewLayout dragViewLayout;

    public DragViewPage viewPager;
    public DragStatePagerAdapter mMPagerAdapter;
    public int currentPosition;
    private static Map<Context, DragViewDialogFragment> map_instance;
    private Handler mHandler;
    private ViewGroup parent;

    public static DragViewDialogFragment show(FragmentActivity fragmentActivity, int position, DragViewDialogFragment.OnDataListener listener) {
        DragViewDialogFragment dialogFragment = DragViewDialogFragment.newInstance();
        dialogFragment.setOnDataListener(listener);
        dialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "tag");
        return dialogFragment;
    }

    public static DragViewDialogFragment newInstance() {
        DragViewDialogFragment fragment = new DragViewDialogFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    public void notifyDataSetChanged() {
        mMPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        //主题设置动画无效，暂时不清楚，请用下面
        setStyle(STYLE_NO_FRAME, R.style.DragViewDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false); //点击周边不隐藏对话框
        parent = (ViewGroup) inflater.inflate(R.layout.dragview_, null);
        Window window = this.getDialog().getWindow();
        window.setDimAmount(0f);// 解决了无法去除遮罩问题
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = 0;
        window.setAttributes(lp);
        initView();
        return parent;
    }

    private void initView() {
        dragViewLayout = parent.findViewById(R.id.dragLayout);
        viewPager = parent.findViewById(R.id.viewPager);

        dragViewLayout.setOnDrawerStatusListener(this);
        dragViewLayout.setOnCurViewListener(this);
        dragViewLayout.setOnDrawerOffsetListener(new DragViewLayout.OnDrawerOffsetListener() {
            @Override
            public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                if (mOnDrawerOffsetListener != null) mOnDrawerOffsetListener.onDrawerOffset(offset);
            }
        });
        currentPosition = getArguments().getInt("currentPosition", 0);
        viewPager.setDragViewLayout(dragViewLayout);
        setDragView(viewPager);

        mMPagerAdapter = new DragStatePagerAdapter(getChildFragmentManager(), mOnDataListener != null ? mOnDataListener.getListFragmentClass() : null, mOnDataListener != null ? mOnDataListener.getListData() : null);
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

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!dragViewLayout.isOpen()) {
                        return true;
                    }
                    if (mOnDataListener != null && !mOnDataListener.onBackPressed()) {
                        return true;
                    }
                    dragViewLayout.close();
                    return true;
                }
                return false;
            }
        });
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
        if (mOnDrawerOffsetListener != null) mOnDrawerOffsetListener.onDragStatus(status);

    }

    public void onFinish() {
        dismiss();
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
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) {
            dragViewLayout.setStop(false);
            currentPosition = position;
            dragViewLayout.setStartView(getCurView());
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

    public ViewGroup getParent() {
        return parent;
    }

    /**
     * 用于替代show ，因为show会抛异常Can not perform this action after onSaveInstanceState
     * 主要是改 ft.commit(); 改成  ft.commitAllowingStateLoss
     */
    public void showAllowingStateLoss(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
