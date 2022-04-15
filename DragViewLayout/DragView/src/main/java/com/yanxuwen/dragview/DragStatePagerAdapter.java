package com.yanxuwen.dragview;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public List<Class<? extends Fragment>> list_fragment;
    public List<Object> list_data;
    public List<Fragment> listFragment;

    public DragStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public DragStatePagerAdapter(FragmentManager fm, List<Class<? extends Fragment>> list_fragment, List<Object> list_data) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_data = list_data;
        listFragment = new ArrayList<>();
    }

    public Fragment getItem(int position) {
        if (listFragment != null && listFragment.size() > position && listFragment.get(position) != null) {
            return listFragment.get(position);
        }
        try {
            if (listFragment.size() == position) {
                Fragment fragment = (Fragment) (list_fragment.get(position)).newInstance();
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putSerializable("data", (Serializable) list_data.get(position));
                fragment.setArguments(b);
                listFragment.add(fragment);
                return listFragment.get(position);

            } else if (listFragment.size() > position) {

                if (listFragment.get(position) == null) {
                    listFragment.set(position, (Fragment) (list_fragment.get(position)).newInstance());
                    Bundle b = new Bundle();
                    b.putInt("position", position);
                    b.putSerializable("data", (Serializable) list_data.get(position));
                    listFragment.get(position).setArguments(b);
                }
                return listFragment.get(position);
            } else {
                for (int i = listFragment.size(); i < position; i++) {
                    listFragment.add(null);
                }
                Fragment fragment = (Fragment) (list_fragment.get(position)).newInstance();
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putSerializable("data", (Serializable) list_data.get(position));
                fragment.setArguments(b);
                listFragment.add(fragment);
                return listFragment.get(position);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        listFragment.set(position, null);
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
}
