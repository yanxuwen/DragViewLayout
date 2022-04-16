package com.yanxuwen.dragview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.yanxuwen.dragview.listener.Listener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DragViewDialog extends DialogFragment implements DragViewLayout.OnDrawerStatusListener, DragViewLayout.OnCurViewListener, ViewPager.OnPageChangeListener {

    private DragViewLayout dragViewLayout;
    private ViewPager viewPager;
    private DragStatePagerAdapter mMPagerAdapter;
    private ViewPager2 viewPager2;
    private DragStatePagerAdapter2 mMPagerAdapter2;
    private ViewPager2.OnPageChangeCallback pageChangeCallback2;
    private int currentPosition;
    private ViewGroup parent;
    private boolean isViewPage2;//是否使用ViewPage2
    protected Controller mController;

    public DragViewDialog(Controller mController) {
        this.mController = mController;
    }

    public static class Builder {
        protected Controller mController;

        public Builder(FragmentActivity fragmentActivity) {
            mController = new Controller();
            mController.fragmentActivity = fragmentActivity;

        }

        /**
         * 是否使用ViewPage2
         *
         * @param isViewPage2 true使用ViewPage2，false 使用ViewPage
         */
        public Builder setViewPage2(boolean isViewPage2) {
            mController.isViewPage2 = isViewPage2;
            return this;
        }

        /**
         * 建议开启
         * 启动的View是否透明化，
         * 使得效果更好
         */
        public Builder setTransparentView(boolean isTransparentView) {
            mController.isTransparentView = isTransparentView;
            return this;
        }

        public Builder setDefaultPosition(int defaultPosition) {
            mController.defaultPosition = defaultPosition;
            return this;
        }

        /**
         * 设置数据，必须设置（多个数据）
         *
         * @param listData          数据列表 不能为空
         * @param fragmentClassList Fragment.class列表 不能为空
         * @param listView          View 列表，用于启动的View  允许为空，为空的画，则没有拖拽动画
         */
        public Builder setData(@NonNull List<? extends Serializable> listData, @NonNull List<Class<? extends Fragment>> fragmentClassList, List<View> listView) {
            mController.listData = listData;
            mController.fragmentClassList = fragmentClassList;
            mController.listView = listView;
            return this;
        }

        /**
         * 设置数据，（单个数据）
         *
         * @param object        数据 不能为空
         * @param fragmentClass Fragment.class 不能为空
         * @param view          View 用于启动的View  允许为空，为空的画，则没有拖拽动画
         */
        public Builder setData(@NonNull Serializable object, @NonNull Class<? extends Fragment> fragmentClass, View view) {
            List<Serializable> listData = new ArrayList<>();
            List<Class<? extends Fragment>> fragmentClassList = new ArrayList<>();
            List<View> listView = new ArrayList<>();
            listData.add(object);
            fragmentClassList.add(fragmentClass);
            listView.add(view);
            mController.listData = listData;
            mController.fragmentClassList = fragmentClassList;
            mController.listView = listView;
            return this;
        }

        public Builder setListener(Listener listener) {
            mController.listener = listener;
            return this;
        }

        public DragViewDialog create() {
            DragViewDialog dialogFragment = new DragViewDialog(mController);
            return dialogFragment;
        }

        public DragViewDialog show() {
            DragViewDialog dialog = create();
            dialog.showAllowingStateLoss(mController.fragmentActivity.getSupportFragmentManager(), "tag");
            return dialog;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //主题设置动画无效，暂时不清楚，请用下面
        setStyle(STYLE_NO_FRAME, R.style.DragViewDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentPosition = mController != null ? mController.defaultPosition : 0;
        isViewPage2 = mController != null ? mController.isViewPage2 : false;
        getDialog().setCanceledOnTouchOutside(false); //点击周边不隐藏对话框
        parent = (ViewGroup) inflater.inflate(isViewPage2 ? R.layout.dragview2_ : R.layout.dragview_, null);
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
        viewPager2 = parent.findViewById(R.id.viewPager2);
        dragViewLayout.setOnDrawerStatusListener(this);
        dragViewLayout.setOnCurViewListener(this);
        dragViewLayout.setOnDrawerOffsetListener(new DragViewLayout.OnDrawerOffsetListener() {
            @Override
            public void onDrawerOffset(@FloatRange(from = 0, to = 1) float offset) {
                if (mController != null && mController.listener != null) {
                    mController.listener.onDrawerOffset(offset);
                }
            }
        });
        if (isViewPage2) {
            setDragView(viewPager2);
            mMPagerAdapter2 = new DragStatePagerAdapter2(getChildFragmentManager(), mController.fragmentClassList, mController.listData);
            viewPager2.setAdapter(mMPagerAdapter2);
            viewPager2.registerOnPageChangeCallback(pageChangeCallback2 = new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    DragViewDialog.this.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    DragViewDialog.this.onPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    DragViewDialog.this.onPageScrollStateChanged(state);
                }
            });
            viewPager2.setCurrentItem(currentPosition);
        } else {
            setDragView(viewPager);
            mMPagerAdapter = new DragStatePagerAdapter(getChildFragmentManager(), mController.fragmentClassList, mController.listData);
            viewPager.setAdapter(mMPagerAdapter);
            viewPager.addOnPageChangeListener(this);
            viewPager.setCurrentItem(currentPosition);
            if (currentPosition == 0) {
                onPageSelected(currentPosition);
            }
        }

        if (mController.listener != null) mController.listener.init();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!dragViewLayout.isOpen()) {
                        return true;
                    }
                    if (mController.listener != null && !mController.listener.onBackPressed()) {
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
        if (status == DragViewLayout.OPEN) {
            if (mController != null && mController.isTransparentView) {
                //隐藏透明View
                if (mController.listView != null && mController.listView.size() > currentPosition) {
                    mController.listView.get(currentPosition).setVisibility(View.INVISIBLE);
                }
            }
        } else if (status == DragViewLayout.CLOSE) {
            if (mController != null && mController.isTransparentView) {
                //显示当前View
                if (mController.listView != null && mController.listView.size() > currentPosition) {
                    mController.listView.get(currentPosition).setVisibility(View.VISIBLE);
                }
            }
            onFinish();
        }
        if (mController != null && mController.listener != null) {
            mController.listener.onDragStatus(status);
        }

    }

    public void onFinish() {
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
        }
        if (viewPager2 != null) {
            viewPager2.unregisterOnPageChangeCallback(pageChangeCallback2);
        }
        dragViewLayout.removeOnDrawerStatusListener();
        dragViewLayout.removeOnCurViewListener();
        dragViewLayout.removeOnDrawerOffsetListener();
        mController = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) {
            dragViewLayout.setStop(false);
            dragViewLayout.setStartView(getCurView());
        } else {
            dragViewLayout.setStop(true);
        }
    }

    @Override
    public void onPageSelected(final int position) {
        currentPosition = position;
        if (mController.listener != null) {
            mController.listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public DragViewLayout.ImageBean getCurView() {
        if (mController.listView != null && mController.listView.size() > currentPosition) {
            return getImageBean(mController.listView.get(currentPosition));
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

    public void notifyDataSetChanged() {
        if (isViewPage2) {
            mMPagerAdapter2.update();
        } else {
            mMPagerAdapter.notifyDataSetChanged();
        }
    }
}
