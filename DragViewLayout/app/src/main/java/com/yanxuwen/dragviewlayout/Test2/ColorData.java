package com.yanxuwen.dragviewlayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import java.io.Serializable;

public class ColorData implements Serializable {
    private @ColorRes
    int idRes;
    private String name;

    public ColorData(@ColorRes int idRes, String name) {
        this.idRes = idRes;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @DrawableRes int getIdRes() {
        return idRes;
    }

    public void setIdRes(@DrawableRes int idRes) {
        this.idRes = idRes;
    }
}
