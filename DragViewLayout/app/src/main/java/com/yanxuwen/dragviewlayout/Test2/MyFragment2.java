package com.yanxuwen.dragviewlayout.Test2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yanxuwen.dragview.DrawData;
import com.yanxuwen.dragviewlayout.ColorData;
import com.yanxuwen.dragviewlayout.R;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public class MyFragment2 extends Fragment {
    ColorData data;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment2, null);
        text = parent.findViewById(R.id.text);
        init();
        return parent;
    }

    private void init() {
        DrawData<ColorData> drawData = new DrawData(getArguments());
        data = drawData.getData();
        text.setBackgroundResource(data.getIdRes());
    }
}
