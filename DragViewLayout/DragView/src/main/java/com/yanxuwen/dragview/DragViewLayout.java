package com.yanxuwen.dragview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class DragViewLayout extends RelativeLayout {
    public static final int OPEN=0;
    public static final int CLOSE=1;
    public static final int DRAG=2;
    /**当前要关闭的View的的监听，由于启动的view不受控制，所以位置会随时发生变化，所以要实时监听*/
    public interface OnCurViewListener {
        public ImageBean getCurView();
    }
    OnCurViewListener mOnCurViewListener = null;
    public void setOnCurViewListener(OnCurViewListener l) {
        mOnCurViewListener = l;
    }
    public void removeOnCurViewListener() {
        mOnCurViewListener=null;
    }

    /**开关状态监听*/
    public interface OnDrawerStatusListener {
        public void onStatus(int status);
    }

    OnDrawerStatusListener mOnDrawerStatusListener = null;

    public void setOnDrawerStatusListener(OnDrawerStatusListener l) {
        mOnDrawerStatusListener = l;
    }
    public void removeOnDrawerStatusListener() {
        mOnDrawerStatusListener=null;
    }

    /**拖动偏移量*/
    public interface OnDrawerOffsetListener {
        /**拖动偏移量变化为1-0  1为显示状态，0为关闭*/
        public void onDrawerOffset(@FloatRange(from=0, to=1)float offset);
    }

    OnDrawerOffsetListener mOnDrawerOffsetListener = null;

    public void setOnDrawerOffsetListener(OnDrawerOffsetListener l) {
        mOnDrawerOffsetListener = l;
    }
    public void removeOnDrawerOffsetListener() {
        mOnDrawerOffsetListener=null;
    }


    private ViewDragHelper mDragHelper;
    private boolean staring=false;
    //停止所以的滚动
    private boolean isStop=false;
    private float mDragOffset;
    private float dragAlpha;
    private float dragScale;
    private boolean isCurView;
    private int velocity;
    private View dragview;
    /**是否透明*/
    private Boolean mAlpha=false;
    private Boolean drag=false;
    //主要用于判断手势是否up，up后才能drag才能false
    private Boolean isTouchUp=false;
    private boolean first=true;
    private int firstTop;
    private int firstLeft;
    private int closeTop;
    private int closeLeft;
    private int closeRight;
    private int closeBottom;

    private int closeHeight;
    private int closeWidth;
    float closeScaleY;
    float closeScaleX;
    private boolean closing=false;
    /**是否在滑动*/
    private boolean isScrolling=false;

    private float closeDistance=0;
    private float startDistance=0;

    public DragViewLayout(Context context) {
        super(context);
        init();
    }

    public DragViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public void maximize() {
        smoothSlideTo(0f);
    }

    public void minimize() {
        closing=true;
        smoothSlideTo(1f);
    }

    boolean smoothSlideTo(float slideOffset) {

        final int topBound = getDragView().getPaddingTop();
        int y =0;
        int x=0;
        //关闭
        if(isCurView&&closing){
            x = closeLeft;
            y = closeTop;
        }
        //打开
        else if(isCurView&&!closing){
            x= firstLeft;
            y = (int) (topBound + slideOffset * getDragView().getHeight())+firstTop;

        }
        //没有指定位置，关闭
        else if(!isCurView&&closing) {
            if((getDragView().getTop()-firstTop)>0){
                y=this.getHeight();
            }else{
                y=-getDragView().getHeight();
            }
        }
        //没有指定位置,打开
        else{
            y=firstTop;
        }
            if (mDragHelper.smoothSlideViewTo(getDragView(), x, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            postInvalidate();
            return true;
        }
        return false;
    }

    public void close(){
        getCurView();
        closing=true;
        //缩放到关闭的Scale
        closeScaleY=(float) closeHeight/getDragView().getHeight();
        closeScaleX=(float) closeWidth/getDragView().getWidth();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(getDragView(), "translationX", 0,closeLeft-getDragView().getLeft());
        ObjectAnimator translationY = ObjectAnimator.ofFloat(getDragView(), "translationY", 0,closeTop-getDragView().getTop());
        //创建透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(getBgView(), "alpha", 1f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(getDragView(), "scaleX", 1,closeScaleX);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(getDragView(), "scaleY", 1,closeScaleY);

        //动画集合
        final AnimatorSet set = new AnimatorSet();
        //添加动画
        if(isCurView){
            set.play(translationX).with(translationY).with(scaleX).with(scaleY).with(alpha);
        }else {
            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
            set.play(alpha2);
        }
        //设置时间等
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
        set.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScrolling=false;
                closing=false;
                if (mOnDrawerStatusListener != null)
                    mOnDrawerStatusListener.onStatus(CLOSE);
                set.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });    }
    private void init() {


    }
    public void setStartView(final ImageBean mImageBean){
        getDragView().setPivotX(0);
        getDragView().setPivotY(0);
        ViewGroup mParent=(ViewGroup) getDragView().getParent();
        if(mParent instanceof DragViewPage){
            mParent=this;
        }
        mDragHelper = ViewDragHelper.create(mParent, 1.0f, new ViewDragCallback());
        mDragHelper.setDuration(500);
        setVisibility(VISIBLE);
        firstTop=getDragView().getTop();
        firstLeft=getDragView().getLeft();
        setCurView(null);
        if(!first)return;
        first=false;
        setCurView(mImageBean);
        staring=true;
//        //缩放到关闭的Scale
        closeScaleY=(float) closeHeight/getDragView().getHeight();
        closeScaleX=(float) closeWidth/getDragView().getWidth();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(getDragView(), "translationX", closeLeft-getDragView().getLeft(),0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(getDragView(), "translationY", closeTop-getDragView().getTop(),0);
        //创建透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(getBgView(), "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(getDragView(), "scaleX", closeScaleX,1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(getDragView(), "scaleY", closeScaleY,1);

        //动画集合
        final AnimatorSet set = new AnimatorSet();
        //添加动画
        if(isCurView) {
            set.play(translationX).with(translationY).with(scaleX).with(scaleY).with(alpha);
        }else {
            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
            set.play(alpha2);
        }
        //设置时间等
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
        set.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                staring=false;
                set.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });


    }
    public void setCurView( ImageBean mImageBean){
        if(mImageBean==null){
            isCurView=false;
            closeTop=0;
            closeLeft=0;
            closeHeight=0;
            closeWidth=0;
            closeRight=0;
            closeBottom=0;
        }else{
            isCurView=true;
            closeTop=mImageBean.top;
            closeLeft=mImageBean.left;
            closeHeight=mImageBean.height;
            closeWidth=mImageBean.width;
            closeRight=closeLeft+closeWidth;
            closeBottom=closeTop+closeHeight;
        }
    }
    public void getCurView(){
        if(closeTop==0&&closeLeft==0&&closeHeight==0&&closeWidth==0&&closeRight==0&&closeBottom==0&&mOnCurViewListener!=null){
            setCurView(mOnCurViewListener.getCurView());
        }
    }

    /**
     * 设置拖曳的View
     */
    public void setDragView(final View dragview){
        this.dragview=dragview;

    }
    public View getDragView(){
        if(getScaleView()==null)return dragview;
        ViewGroup mParent=(ViewGroup) getScaleView().getParent();
        if(mParent instanceof DragViewPage){
            return dragview;
        }else {
            return getScaleView();
        }

    }
    public View getBgView(){
        if(getChildCount()>0&&getChildAt(0)!=null){
            return getChildAt(0);
        }
        return getDragView();
    }
    /**获取缩放的View，由于拖曳的是ViewPage，所以有时候他的子视图并不充满整个控件，所以缩放View最好为子视图，效果比较好*/
    private View getScaleView(){
        return scaleView;
    }
    public View scaleView;
    public void setScaleView(View scaleView){
        this.scaleView=scaleView;

    }

    public Boolean isAlpha() {
        return mAlpha;
    }

    public void setAlpha(Boolean mAlpha) {
        this.mAlpha = mAlpha;
    }
    /**是否是打开状态*/
    public boolean isOpen(){
        if(!closing&&!staring){
            return true;
        }
//        if(!startup&&!staring&&!closing){
//            return true;
//        }
        return false;
    }
    /**是否有在拖曳*/
    public boolean isScrolling() {
        return isScrolling;
    }
    /**是否停止所以的滚动*/
    public boolean isStop() {
        return isStop;
    }
    /**设置是否停止滚动*/
    public void setStop(boolean stop) {
        isStop = stop;
    }
    private class ViewDragCallback extends ViewDragHelper.Callback {
        /**
         * 尝试捕获子view，一定要返回true
         * @param  childview 尝试捕获的view
         * @param  pointerId 指示器id？
         * 这里可以决定哪个子view可以拖动
         */
        @Override
        public boolean tryCaptureView(View childview, int pointerId) {
            if(isOpen()){
                return getDragView() == childview;
            }
            //启动跟关闭的时候禁止拖曳
            return false;
        }
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            getCurView();
            /**没有指定位置，则不进行缩放***/
            if(!isCurView){
                //变化0->1
                float change=0;
                //向下
                if(top-firstTop>0){
                     change=((float) (Math.abs((top-firstTop)))/(getHeight()-firstTop));
                }
                //向上
                else{
                    change=((float)(Math.abs((top-firstTop)))/(firstTop+getDragView().getHeight()));
                }
                if (getBgView().getBackground()!=null) {
                    int alpha=(int) ((1-change) * 255);
                    if(alpha>255)alpha=255;
                    else if(alpha<0)alpha=0;
                    getBgView().getBackground().setAlpha(alpha);
                }
                if(mOnDrawerOffsetListener!=null)mOnDrawerOffsetListener.onDrawerOffset(1-change);
                //关闭
                if(change==1){
                    isScrolling=false;
                    closing=false;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(CLOSE);
                }
                //打开
                else if(change==0){
                    isScrolling=false;
                    drag=false;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(OPEN);
                }
                //拖曳
                else{
                    isScrolling=true;
                    drag=true;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(DRAG);
                }
                return;
            }
            /**指定位置，正在关闭***/
            if(closing&&!staring){
                drag=false;
                if(closeDistance==0){
                    closeDistance=top-closeTop;
                }

                //变化1->0
                float change=((top-closeTop)/closeDistance);

                //拖动到放手的相差多少Scale
                float diffscaleY=dragScale-closeScaleY;
                float diffscaleX=dragScale-closeScaleX;

                //公式
                float newscaleX=diffscaleX*change+closeScaleX;
                float newscaleY=diffscaleY*change+closeScaleY;

                getDragView().setPivotX((getDragView().getWidth()*change)/2);
                getDragView().setPivotY((getDragView().getHeight()*change)/2);
                getDragView().setScaleX(newscaleX);
                getDragView().setScaleY(newscaleY);
                if(isAlpha()){
                    getDragView().setAlpha(1-((1-dragAlpha) * change));
                }
                if (getBgView().getBackground()!=null) {
                    int alpha=(int) ((dragAlpha*change) * 255);
                    if(alpha>255)alpha=255;
                    else if(alpha<0)alpha=0;
                    getBgView().getBackground().setAlpha(alpha);
                }
                if(mOnDrawerOffsetListener!=null)mOnDrawerOffsetListener.onDrawerOffset(change);
                if(change==0){
                    isScrolling=false;
                    closing=false;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(CLOSE);
                }
            }
            /**指定位置，拖曳***/
            else if(isOpen()){
                mDragOffset = (float) (top-firstTop) /  (getHeight()-firstTop);
                velocity=dy;
                dragScale=(1-mDragOffset);
                if(dragScale>1)dragScale=1;

                getDragView().setPivotX((getDragView().getWidth())/2);
                getDragView().setPivotY((getDragView().getHeight())/2);
                getDragView().setScaleX(dragScale);
                getDragView().setScaleY(dragScale);
                if(dragScale<1){
                    isScrolling=true;
                    drag=true;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(DRAG);
                }else if(mDragOffset==0&&isTouchUp){
                    isScrolling=false;
                    drag=false;
                    if (mOnDrawerStatusListener != null)
                        mOnDrawerStatusListener.onStatus(OPEN);
                }
                dragAlpha=1 - mDragOffset;
                if(isAlpha()){
                    getDragView().setAlpha(dragAlpha);
                }
                if (getBgView().getBackground()!=null) {
                    int alpha=(int) (dragAlpha * 255);
                    if(alpha>255)alpha=255;
                    else if(alpha<0)alpha=0;
                    getBgView().getBackground().setAlpha(alpha);
                }
                if(mOnDrawerOffsetListener!=null)mOnDrawerOffsetListener.onDrawerOffset(dragAlpha);

            }

        }

        /**
         * 处理水平方向上的拖动
         * @param  child 被拖动到view
         * @param  left 移动到达的x轴的距离
         * @param  dx 建议的移动的x距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            System.out.println("left = " + left + ", dx = " + dx);
//
//            // 两个if主要是为了让viewViewGroup里
//            if(getPaddingLeft() > left) {
//                return getPaddingLeft();
//            }
//
//            if(getWidth() - child.getWidth() < left) {
//                return getWidth() - child.getWidth();
//            }
            if(isStop()||!isCurView){
                return getDragView().getPaddingLeft();
            }
            return drag?left:getPaddingLeft();
        }

        /**
         *  处理竖直方向上的拖动
         * @param  child 被拖动到view
         * @param  top 移动到达的y轴的距离
         * @param  dy 建议的移动的y距离
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(!isCurView){

                return isStop()?getDragView().getTop():top;
            }
            //不能上移
            if(getDragView().getPaddingTop() > top) {
                return getDragView().getPaddingTop();
            }
            //停止滚动，则不能下移动
            if(isStop()||(top- firstTop)<5){
                return getDragView().getTop();
            }

            return top;
        }

        /**
         * 当拖拽到状态改变时回调
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    }
    @Override
    public void computeScroll() {
        if (mDragHelper!=null&&mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }
    float downX=0;
    float downY=0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:{
                if(mDragHelper!=null){
                    mDragHelper.cancel(); // 相当于调用 process TouchEvent收到ACTION_CANCEL
                }
            }
        }
        /**
         * 检查是否可以拦截touch事件
         * 如果onInterceptTouchEvent可以return true 则这里return true
         */
        return drag?true:false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            isTouchUp=false;
            downX=ev.getX();
            downY=ev.getY();
        }else if(ev.getAction()==MotionEvent.ACTION_POINTER_DOWN){
            return false;
        } else if(ev.getAction()==MotionEvent.ACTION_MOVE){
            if((Math.abs(ev.getY()-downY)>(Math.abs(ev.getX()-downX)*2))||drag){
                isStop=false;
            }else {
                isStop=true;
            }

        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP: {
                isTouchUp=true;
                if(isOpen()){
                    if(isCurView){
                        if(mDragOffset>0.12||velocity>15){
                            minimize();
                        }else {
                            maximize();
                        }
                    }else {
                        if(Math.abs((getDragView().getTop()-firstTop))>250){
                            minimize();
                        }else{
                            maximize();
                        }
                    }

                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                isTouchUp=true;
            }
        }

        boolean isreturn=drag?true:false;
        if(!isreturn){
            for(int i=0;i<getChildCount();i++){
                try {
                    getChildAt(i).onTouchEvent(ev);
                }catch (Exception e){}
            }
        }
        return true;
    }

    public static class ImageBean implements Parcelable {
        int top;
        int left;
        int width;
        int height;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.top);
            dest.writeInt(this.left);
            dest.writeInt(this.width);
            dest.writeInt(this.height);
        }

        public ImageBean() {
        }

        private ImageBean(Parcel in) {
            this.top = in.readInt();
            this.left = in.readInt();
            this.width = in.readInt();
            this.height = in.readInt();
        }

        public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
            @Override
            public ImageBean createFromParcel(Parcel source) {
                return new ImageBean(source);
            }

            @Override
            public ImageBean[] newArray(int size) {
                return new ImageBean[size];
            }
        };
    }

}