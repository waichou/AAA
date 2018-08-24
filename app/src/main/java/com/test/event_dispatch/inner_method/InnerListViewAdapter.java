package com.test.event_dispatch.inner_method;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhouwei.aaa.R;
import com.test.event_dispatch.outer_method.OuterListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 2018/8/19.
 */

public class InnerListViewAdapter extends BaseAdapter {

    private Context mContext;
    public InnerListViewAdapter(Context context){
        this.mContext = context;
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
        }/*else if (position == 1){
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.event_listview, parent, false);
//            ListView listView = convertView.findViewById(R.id.event_listview_id);
//            initListView(listView);

            LinearLayout layout1 = new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
            layout1.setLayoutParams(layoutp);
            layout1.setBackgroundColor(Color.RED);
            convertView = layout1;

        }*/else /*if (position == 2)*/{
            LinearLayout layout1 = new LinearLayout(mContext);
            //--------------------
            LinearLayout.LayoutParams tvP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            TextView tv = new TextView(mContext);
            tv.setText("tv " + position);
            layout1.addView(tv,tvP);
            //--------------------
            LinearLayout.LayoutParams layoutp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
            layout1.setLayoutParams(layoutp);
            layout1.setBackgroundColor(Color.BLUE);
            convertView = layout1;
        }
        return convertView;
    }

    private void initListView(ListView listView) {//实现出一个局部的listview，在父listview中滑动

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
        adapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }
}
