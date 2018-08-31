package com.test.event_dispatch.inner_method;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 2018/8/19.
 */

public class InnerListViewAdapter2 extends BaseAdapter {

    private Context mContext;
    private ListView mParentView;
    public InnerListViewAdapter2(Context context, ListView parentListView){
        this.mContext = context;
        this.mParentView = parentListView;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position %2 ==  0){
            LinearLayout layout1 = new LinearLayout(mContext);
            //--------------------
            LinearLayout.LayoutParams tvP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            TextView tv = new TextView(mContext);
            tv.setText("tv " + position);
            layout1.addView(tv,tvP);
            //--------------------
            LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
            layout1.setLayoutParams(layoutp);
            layout1.setBackgroundColor(Color.GRAY);
            convertView = layout1;
        }else{/* if (position == 1){*/
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.event_listview, parent, false);
//            ListView listView = convertView.findViewById(R.id.event_listview_id);

            ViewPagerForListView listView = new ViewPagerForListView(mContext,mParentView);
//            ListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,200);
//
//            listView.setLayoutParams(layoutParams);

            initListView(listView);

//            LinearLayout layout1 = new LinearLayout(mContext);
//            LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
//            layout1.setLayoutParams(layoutp);
//            layout1.setBackgroundColor(Color.RED);
//            convertView = layout1;
            convertView = listView;

        }
//        else /*if (position == 2)*/{
//            LinearLayout layout1 = new LinearLayout(mContext);
//            //--------------------
//            LinearLayout.LayoutParams tvP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            TextView tv = new TextView(mContext);
//            tv.setText("tv " + position);
//            layout1.addView(tv,tvP);
//            //--------------------
//            LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
//            layout1.setLayoutParams(layoutp);
//            layout1.setBackgroundColor(Color.BLUE);
//            convertView = layout1;
//        }
        return convertView;
    }

    private void initListView(final ViewPagerForListView listView) {//实现出一个局部的listview，在父listview中滑动

        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("leavesC " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("item click is called!--->" + position);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        //Log.d("ListView", "##### 滚动到顶部 #####");
                        listView.isAbsTop = true;
                        listView.isAbsBottom = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        //Log.d("ListView", "##### 滚动到底部 ######");
                        listView.isAbsBottom = true;
                        listView.isAbsTop = false;
                    }
                }

            }
        });

        adapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }
}
