package com.test.event_dispatch.outer_method;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zhouwei.aaa.R;

import java.util.ArrayList;
import java.util.List;


public class OutDispatchEventActivity extends AppCompatActivity {

    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_event);

        ViewPager viewPager = findViewById(R.id.viewPager_id);
        viewList = new ArrayList<View>();
        initData();
        viewPager.setAdapter(new MyPagerAdapter(viewList));

    }

    private void initData() {
        for (int j = 0; j < 4; j++) {
            View view = null;
            ListView listView = new OuterListView(this);
            List<String> dataList = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                dataList.add("leavesC " + i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("item click is called!--->" + position);
                }
            });
            view = listView;
            viewList.add(view);
        }
    }


    private class MyPagerAdapter extends PagerAdapter{

        List<View> viewList;
        public MyPagerAdapter(List<View> viewList){
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
}
