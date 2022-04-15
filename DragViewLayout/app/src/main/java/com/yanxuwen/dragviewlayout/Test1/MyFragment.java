package com.yanxuwen.dragviewlayout.Test1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.chrisbanes.photoview.PhotoView;
import com.yanxuwen.dragview.AllowDragListener;
import com.yanxuwen.dragviewlayout.R;

/**
 * Created by yanxuwen on 2018/6/15.
 */

public class MyFragment extends Fragment implements AllowDragListener {
    View parent;
    TextView text;
    private int position;
    private Object data;
    private PhotoView photoView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment, null);
        init();
        return parent;
    }

    private void init(){
        text = parent.findViewById(R.id.text);
        photoView = parent.findViewById(R.id.photoView);
        position = getArguments().getInt("position");
        data = getArguments().getSerializable("data");
        text.setText(position + "???" + data);
    }

    @Override
    public boolean isAllowDrag() {
        if (photoView.getScale() > 1){
            return false;
        }
        return true;
    }
}
