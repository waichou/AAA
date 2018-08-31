package com.test.event_dispatch.inner_method;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private ListView mParentView;

    public ViewPagerForListView(Context context, ListView parentListView) {
        super(context);
        this.mContext = context;
        this.mActivity = (Activity) context;
        this.mParentView = parentListView;
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


    public void setListView(ListView parentListView){
        this.mParentView = parentListView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //解决ListView内容显示不全的问题
//        super.onMeasure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST));
//        super.onMeasure(widthMeasureSpec,500);

        setMeasuredDimension(widthMeasureSpec,200);
    }

    public boolean isAbsTop = false;
    public boolean isAbsBottom = false;

//    private int lastX;

    private int lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//先拦截到父ListView
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                int top = getTop();
//                System.out.println("top="+ top);
//                int bottom = getBottom();
////                if (top == 0 || ) {
//                ViewParent parent = getParent().getParent();
//                }

                int curY = (int) ev.getY();


                //需要如果滑动的对象是当前ListView，则控制滑动处理权给予当前ListView
                //处理权得到之后，可以判断如下几种情况：
                //1.如果滑动到ListView的顶部，即：adapger.getView()--->>>child index == 0 ，处理权交给父parentListView
                //2.如果滑动到ListView的底部，即：adapter.getView --->>> child(length-1)==listview.getHeight()

            /*    Object childFirst = getAdapter().getView(0,null,null);
                Object lastChild = getAdapter().getView(getAdapter().getCount()-1,null,null);

//                if (childFirst instanceof TextView) {
                    //position == 0
                    TextView childTv = (TextView) childFirst;
                    String childText = childTv.getText().toString();
                    System.out.println("获取到的tv值：" + childText);

                    TextView lastChildTv = (TextView) lastChild;

                    //
                    int childTop = childTv.getTop();
                    //
                    int childBottom = lastChildTv.getBottom();*/

                    int desY = curY - lastY;
                    lastY = curY;

                    //针对的条件就是处于垂直滑动的情况，其他不处理交给父ListView
//                    if (Math.abs(x - lastX) < Math.abs(y - lastY)) {
                        //向下滑动
                        if (desY > 0){
                            if (isAbsTop){
                                mParentView.requestDisallowInterceptTouchEvent(false);
                            }else{
                                mParentView.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                        //向上滑动
                        else{
                            if (isAbsBottom){
                                mParentView.requestDisallowInterceptTouchEvent(false);
                            }else{
                                mParentView.requestDisallowInterceptTouchEvent(true);
                            }
                        }


//                    }
//                }
                break;
            case MotionEvent.ACTION_UP:
                mParentView.requestDisallowInterceptTouchEvent(false);
                lastY = 0;
//                lastX = 0;

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
