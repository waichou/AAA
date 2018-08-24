package com.framworks.base.example.event_dispatch;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhouwei on 2018/8/19.
 *
 * 采用外部拦截法：解决ListView 与 ViewPager 之间的事件冲突
 *
 */

public class MyViewPager extends ViewPager {

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float mLastX = 0,mLastY = 0;

    /**
     * 外部拦截需要重写该拦截方法
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //由于ListView完全的占据了ViewPager的空间，且两者的滑动方向不同，故利用外部拦截法处理更好一些
        //如果ListView仅仅是占据viewpager的某一部分，则在处理Listview的事件冲突上应该选择内部拦截法

        //开始处理事件分发时，不能拦截down事件，因为listview还有点击事件需要处理
        //1.获取当前事件
        boolean isIntercepter = false;
        float mCurX = 0,mCurY = 0;
        int eventType = ev.getActionMasked() & ev.getAction();
        switch (eventType){
            //不允许拦截down事件，子需要处理相应的点击事件
            case MotionEvent.ACTION_DOWN:
                mCurX = ev.getX();
                mCurY = ev.getY();
                //调用 ViewPager的 onInterceptTouchEvent 方法用于初始化 mActivePointerId
                //super.onInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                //此时要具体判断横纵向趋势后，决定当前谁来处理滑动事件
                mCurX = ev.getX();
                mCurY = ev.getY();
                float disX = ev.getX() - mLastX;
                float disY = ev.getY() - mLastY;
                //listView 处理滑动
                if (Math.abs(disX) >= Math.abs(disY)){
                    isIntercepter = true;
                }else{
                    //viwePager处理滑动
                    isIntercepter = false;
                }
                break;
        }
        mLastX = mCurX;
        mLastY = mCurY;
        return isIntercepter;
    }
}
