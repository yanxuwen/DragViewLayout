package com.yanxuwen.dragviewlayout.Test3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yanxuwen.dragviewlayout.R;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public class MyFragment3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment, null);
        return parent;
    }
}
