package com.yanxuwen.dragviewlayout.Test3;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class PictureData implements Serializable {
    private @DrawableRes
    int idRes;
    private String name;

    public PictureData(@DrawableRes int idRes, String name) {
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
