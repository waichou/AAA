package com.test.event_dispatch.inner_method;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by zhouwei on 2018/8/19.
 *
 * 内部拦截法：由于内部的Listview仅占据父ListView的一部分所以，采用内部拦截法的方式。
 *
 * 既然指定的touch事件能进来，则表示就可以处理当前事件
 */

public class ViewPagerForListView extends ListView {
    private Context mContext;
    private Activity mActivity;
    public ViewPagerForListView(Context context) {
        super(context);
        this.mContext = context;
        this.mActivity = (Activity) context;
    }

    public ViewPagerForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mActivity = (Activity) context;
    }

    public ViewPagerForListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mActivity = (Activity) context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //解决ListView内容显示不全的问题
        super.onMeasure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST));
    }

    private int lastX;

    private int lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int top = getTop();
                System.out.println("top="+ top);
                int bottom = getBottom();
//                if (top == 0 || ) {
                ViewParent parent = getParent().getParent();

                getParent().requestDisallowInterceptTouchEvent(true);
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
//        lastX = x;
//        lastY = y;
        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        super.onTouchEvent(ev);
//        int eventType = ev.getAction() & ev.getActionMasked();
//        switch (eventType){
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                return true;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
}
