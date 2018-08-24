package com.recycleview.test.scrollview_listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.recycleview.test.R;
import com.recycleview.test.adapter.LGRecycleViewAdapter;
import com.recycleview.test.adapter.LGViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScrollViewListViewActivity extends AppCompatActivity {

    @Bind(R.id.list_view_id)
    ListView listViewId;
    ListViewAdapter listViewAdapter;

    List<String> datas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_list_view);
        ButterKnife.bind(this);
        datas.add("aaa");
        datas.add("bbb");
        datas.add("ccc");
        datas.add("ddd");

        listViewAdapter = new ListViewAdapter();
        listViewId.setAdapter(listViewAdapter);
    }

    private class ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);

            TextView tv = new TextView(ScrollViewListViewActivity.this);
            tv.setLayoutParams(layoutParams);
            tv.setText(datas.get(position));
            return tv;
        }
    }

}
