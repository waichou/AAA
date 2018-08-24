package com.test.event_dispatch.inner_method;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhouwei on 2018/8/19.
 */

public class InnerViewPagerAdapter extends PagerAdapter{

    List<View> viewList;
    public InnerViewPagerAdapter(List<View> viewList){
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList == null || viewList.isEmpty() ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view  == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.viewList.get(position);
        container.addView(view);
        return view;
    }

    //注意：此方法必须重写，且不能再调用super.destroyItem方法，否则报错！
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}