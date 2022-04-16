package com.yanxuwen.dragviewlayout.Test1;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
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
    private SubsamplingScaleImageView longImageView;
    private boolean eqLongImage;
    private float longScale;//长图缩放之
    private boolean isLongTop;//长图是否在顶部

    @Override
    public void onResume() {
        super.onResume();
        Log.e("yxw","onResume" + position);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("yxw","onPause" + position);
    }

    @Override
    public void setUserVisibleHint(boolean hidden) {
        super.setUserVisibleHint(hidden);
        Log.e("yxw",position + "_setUserVisibleHint_" + hidden);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment, null);
        init();
        return parent;
    }

    private void init() {
        text = parent.findViewById(R.id.text);
        photoView = parent.findViewById(R.id.photoView);
        longImageView = parent.findViewById(R.id.imageView);
        position = getArguments().getInt("position");
        data = getArguments().getSerializable("data");
        text.setText(position + "???" + data);
        Glide.with(getContext())
                .asBitmap()
                .load(R.mipmap.ic_launcher)
                .into(new ImageViewTarget<Bitmap>(photoView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {

                        if (resource != null) {
                            eqLongImage = isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            photoView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                                isLongTop = longImageView.getCenter().y - (longImageView.getHeight() / 2) == 0;
                                longScale = longImageView.getScale();
                            } else {
                                // 普通图片
                                photoView.setImageBitmap(resource);
                            }
                        }
                    }
                });
        longImageView.setOnStateChangedListener(new SubsamplingScaleImageView.OnStateChangedListener() {
            @Override
            public void onScaleChanged(float newScale, int origin) {
                longScale = newScale;
            }

            @Override
            public void onCenterChanged(PointF newCenter, int origin) {
                isLongTop = false;
                if (newCenter != null && newCenter.y - (longImageView.getHeight() / 2) == 0) {
                    isLongTop = true;
                }

            }
        });
    }


    /**
     * 是否是长图
     *
     * @param width  宽
     * @param height 高
     * @return true 是 or false 不是
     */
    public boolean isLongImg(int width, int height) {
        if (width <= 0 || height <= 0) {
            return false;
        }
        int newHeight = width * 3;
        return height > newHeight;
    }

    @Override
    public boolean isAllowDrag() {
        if (eqLongImage && (longScale > 1 || !isLongTop)) {
            //长图，判断缩放大于1 或  不在顶部，
            //则不允许拖拽
            return false;
        } else if (!eqLongImage && photoView.getScale() > 1) {
            //正常图片，判断只要缩放大于1则不允许拖拽
            return false;
        }

        return true;
    }
}
