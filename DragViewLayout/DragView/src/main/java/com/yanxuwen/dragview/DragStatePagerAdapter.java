package com.yanxuwen.dragview;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该 PagerAdapter 的实现将只保留当前页面，当页面离开视线后，就会被消除，释放其资源；而在页面需要显示时，
 * 生成新的页面(就像 ListView 的实现一样)。这么实现的好处就是当拥有大量的页面时，不必在内存中占用大量的内存。
 * <p>
 * 如果重写destroyItem去掉super.destroyItem(container, position, object);就会跟FragmentPagerAdapter效果一样
 * 当然也可以设置哪些要销毁，哪些不用销毁
 * <p>
 * 如果设置了setOffscreenPageLimit就会缓存几页，不会被销毁
 */
public class DragStatePagerAdapter extends FragmentStatePagerAdapter {
    public List<? extends Serializable> listData;
    public List<Class<? extends Fragment>> fragmentClassList;
    private Map<Long, Fragment> fragmentMap = new HashMap();
    private List<Long> fragmentIds = new ArrayList<>();//用于存储更新fragment的特定标识

    public DragStatePagerAdapter(FragmentManager fm, List<Class<? extends Fragment>> fragmentClassList, List<? extends Serializable> listData) {
        super(fm);
        this.fragmentClassList = fragmentClassList;
        this.listData = listData;
        updateIds(listData);
    }

    @Override
    public Fragment getItem(int position) {
        Long ids = fragmentIds.get(position);

        if (fragmentMap != null && fragmentMap.containsKey(ids)) {
            return fragmentMap.get(ids);
        }
        try {
            Fragment fragment = (Fragment) (fragmentClassList.get(position)).newInstance();
            Bundle b = new Bundle();
            b.putInt("position", position);
            b.putSerializable("data", (Serializable) listData.get(position));
            fragment.setArguments(b);
            fragmentMap.put(ids, fragment);
            return fragment;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Parcelable saveState() {
        return null;

    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
        }
    }

    public void updateIds(List<? extends Serializable> listData) {
        fragmentIds.clear();
        for (int i = 0; i < listData.size(); i++) {
            fragmentIds.add(System.currentTimeMillis() + i);
        }
    }

    public void remove(int position) {
        Long ids = fragmentIds.get(position);
        fragmentMap.remove(ids);
        listData.remove(position);
        fragmentClassList.remove(position);
        fragmentIds.remove(position);
        notifyDataSetChanged();
    }
}
