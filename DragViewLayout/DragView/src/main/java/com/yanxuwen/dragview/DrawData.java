package com.yanxuwen.dragview;


import android.os.Bundle;

import java.io.Serializable;

/**
 * 拖拽数据
 */
public class DrawData<T> implements Serializable {
    public static final String DATA = "data";
    public static final String POSITION = "position";


    private int position;
    private T data;

    public DrawData(Bundle bundle) {
        try {
            position = bundle.getInt(POSITION);
            data = (T) bundle.getSerializable(DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
