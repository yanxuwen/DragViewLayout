package com.yanxuwen.dragviewlayout.Test1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yanxuwen.dragview.DragFragment;
import com.yanxuwen.dragview.PinchImageView;
import com.yanxuwen.dragviewlayout.R;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public class MyFragment extends DragFragment{
    PinchImageView dragview;
    @Override
    public View getDragView() {
        return (view==null)?null:view.findViewById(R.id.dragview);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.fragment);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        dragview= (PinchImageView) view.findViewById(R.id.dragview);
        RequestOptions options3 = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH);
        Glide.with(getContext()).load("http://p3.pstatp.com/large/pgc-image/1530136376452729ffef687").transition(DrawableTransitionOptions.withCrossFade()).apply(options3).into(dragview);
    }

    @Override
    public void init() {

    }

    @Override
    public void onDragStatus(int status) {

    }
}
