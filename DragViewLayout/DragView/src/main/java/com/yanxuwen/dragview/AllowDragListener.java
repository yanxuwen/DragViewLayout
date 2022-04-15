package com.yanxuwen.dragview;

/**
 * 允许拖拽，当拖拽跟其他控件起冲突的时候，特别常用的PhotoView,
 * 由于PhotoView不触发父类，导致无法拖拽，这时候就需要这个监听。
 */
public interface AllowDragListener {
     boolean isAllowDrag();
}
