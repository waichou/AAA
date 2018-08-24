package com.test.event_dispatch.outer_method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by zhouwei on 2018/8/19.
 */

public class OuterListView extends ListView {

    public OuterListView(Context context) {
        super(context);
    }

    public OuterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OuterListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println("outer listview--dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int eventType = ev.getAction() & ev.getActionMasked();
        switch (eventType){
            case MotionEvent.ACTION_DOWN:
                System.out.println("outer listview--onTouchEvent-down");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("outer listview--onTouchEvent-move");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("outer listview--onTouchEvent-up");
                break;
        }
        return super.onTouchEvent(ev);
    }
}
