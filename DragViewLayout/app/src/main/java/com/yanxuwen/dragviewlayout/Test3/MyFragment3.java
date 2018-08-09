package com.yanxuwen.dragviewlayout.Test3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yanxuwen.dragview.DragFragment;
import com.yanxuwen.dragviewlayout.R;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public class MyFragment3 extends DragFragment{
    TextView text;
    @Override
    public View getDragView() {
        return (view==null)?null:view.findViewById(R.id.text);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.fragment2);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        text= (TextView) view.findViewById(R.id.text);
        text.setText((String)data);
    }

    @Override
    public void init() {

    }

    @Override
    public void onDragStatus(int status) {

    }
}
