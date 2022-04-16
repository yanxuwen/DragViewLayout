package com.yanxuwen.dragview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author bsnl_yanxuwen
 * @date 2021/4/9 15:37
 * Description :
 * ViewPage2使用
 */
public class DragStatePagerAdapter2 extends FragmentStateAdapter {
    public List<Object> listData;
    private List<Fragment> fragmentList = new ArrayList<>();
    public List<Class<? extends Fragment>> fragmentClassList;
    private List<Long> fragmentIds = new ArrayList<>();//用于存储更新fragment的特定标识
    private HashSet<Long> creatIds = new HashSet<>();//得用hashset防重，用于存储adapter内的顺序


    public DragStatePagerAdapter2(@NonNull FragmentActivity fragmentActivity, List<Class<? extends Fragment>> fragmentClassList, List<Object> listData) {
        super(fragmentActivity);
        this.fragmentClassList = fragmentClassList;
        this.listData = listData;
        updateIds(listData);
    }

    public DragStatePagerAdapter2(@NonNull FragmentManager fragmentManager, List<Class<? extends Fragment>> fragmentClassList, List<Object> listData) {
        super(fragmentManager, new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return null;
            }
        });
        this.fragmentClassList = fragmentClassList;
        this.listData = listData;
        updateIds(listData);
    }

    public DragStatePagerAdapter2(@NonNull Fragment fragment, List<Class<? extends Fragment>> fragmentClassList, List<Object> listData) {
        super(fragment);
        this.fragmentClassList = fragmentClassList;
        this.listData = listData;
        updateIds(listData);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Long ids = fragmentIds.get(position);
        creatIds.add(ids);//创建的时候将未添加的fragment添加进来，每次刷新都会调用这里，其次调用containsItem

        if (fragmentList != null && fragmentList.size() > position && fragmentList.get(position) != null) {
            return fragmentList.get(position);
        }
        try {
            if (fragmentList.size() == position) {
                Fragment fragment = (Fragment) (fragmentClassList.get(position)).newInstance();
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putSerializable("data", (Serializable) listData.get(position));
                fragment.setArguments(b);
                fragmentList.add(fragment);
                return fragmentList.get(position);

            } else if (fragmentList.size() > position) {

                if (fragmentList.get(position) == null) {
                    fragmentList.set(position, (Fragment) (fragmentClassList.get(position)).newInstance());
                    Bundle b = new Bundle();
                    b.putInt("position", position);
                    b.putSerializable("data", (Serializable) listData.get(position));
                    fragmentList.get(position).setArguments(b);
                }
                return fragmentList.get(position);
            } else {
                Fragment fragment = (Fragment) (fragmentClassList.get(position)).newInstance();
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putSerializable("data", (Serializable) listData.get(position));
                fragment.setArguments(b);
                fragmentList.add(fragment);
                return fragmentList.get(position);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Fragment getItem(int position) {
        if (fragmentList != null && fragmentList.size() > position && fragmentList.get(position) != null) {
            return fragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragmentClassList.size();
    }

    /**
     * 这两个方法必须重写，作为数据改变刷新检测的工具
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return fragmentIds.get(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        return creatIds.contains(itemId);
    }


    public void update() {
        updateIds(listData);
        notifyDataSetChanged();
    }


    public void updateIds(List<Object> listData) {
        fragmentIds.clear();
        for (int i = 0; i < listData.size(); i++) {
            fragmentIds.add(Long.parseLong(i + ""));
        }
    }
}