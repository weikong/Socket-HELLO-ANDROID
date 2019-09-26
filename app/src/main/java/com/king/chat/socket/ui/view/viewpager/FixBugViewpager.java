package com.king.chat.socket.ui.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.king.chat.socket.util.DisplayUtil;

import java.lang.reflect.Field;

/**
 * Created by maesinfo on 2019/7/18.
 */

public class FixBugViewpager extends ViewPager {

    private Context mContext;
    public FixBugViewpager(Context context) {
        super(context);
        this.mContext = context;
        fixTouchSlop();
    }
    public FixBugViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        fixTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 这个方法是通过反射，修改viewpager的触发切换的最小滑动速度（还是距离？姑
     * 且是速度吧！滑了10dp就给它切换）
     **/
    private void fixTouchSlop() {
        Field field = null;
        try {
            field = ViewPager.class.getDeclaredField("mMinimumVelocity");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            field.setInt(this, DisplayUtil.dp2px(10));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /****
     * 滑动距离及坐标 归还父控件焦点
     ****/
    private float xDistance, yDistance, xLast, yLast;
    /**
     * 是否是左右滑动
     **/
    private boolean mIsBeingDragged = true;

    /**
     *重写这个方法纯属是为了告诉Recyclerview，什么时候不要拦截viewpager的滑动
     *事件
     **/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if (!mIsBeingDragged) {
                    if (yDistance < xDistance * 0.5) {//小于30度都左右滑
                        mIsBeingDragged = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        mIsBeingDragged = false;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }}
