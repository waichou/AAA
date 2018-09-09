package com.test.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zhouwei.aaa.R;
import com.test.event_dispatch.inner_method.InnerListViewAdapter;


/**
 * ListView嵌套ViewPager
 *
 */
public class ListViewActivity extends AppCompatActivity {

    private ListView mListView;
    private InnerListViewAdapter myListViewAdapter;
    private Button bottomBtn;

    private int distanceY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        bottomBtn = findViewById(R.id.list_btn_id);
        bottomBtn.setVisibility(View.GONE);
        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("click is called!");

                //实现滑动到第一个item的top=0的位置上去，即listview的顶部
                
                //利用向上滑动过的偏移量滑动回去
                //mListView.smoothScrollByOffset(-distanceY);

                //滑动到position  距离top的偏移量  滑动所用的时间
                mListView.smoothScrollToPositionFromTop(0,20,100);

            }
        });

        mListView = findViewById(R.id.listView_id);

        myListViewAdapter = new InnerListViewAdapter(this,mListView);

        mListView.setAdapter(myListViewAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //这里获取的是Listview与父容器之间的距离
                int top = view.getTop();
                int bottom  = view.getBottom();
                System.out.println("top=" + top + ",botttom="+ bottom);

                int height = view.getHeight();
                System.out.println("listview height="+ height);

                int actionBarHeight = BarUtils.getActionBarHeight();
                System.out.println("bar height="+ actionBarHeight);

                mCurrentfirstVisibleItem = firstVisibleItem;
                System.out.println("当前head显示的position=" + mCurrentfirstVisibleItem);

                ViewGroup group = (ViewGroup) view.getChildAt(0);
                if (group != null){
                    TextView childBtn = (TextView) group.getChildAt(0);
                    if (childBtn != null) {
                        System.out.println("child-0->" + childBtn.getText().toString());
                    }
                }
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    distanceY = getScrollY();//滚动距离

                    System.out.println("向上滚动的高度为：" + distanceY  + ",当前显示的firstView偏移量top:" + itemRecord.top);

                    if (distanceY > 30){
                        bottomBtn.setVisibility(View.VISIBLE);
                    }else{
                        bottomBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;

    public int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    //注意：ListView.getChilcAt(int index) 表示获取的是当前界面上的第一条item，而不是position=0的第一条！
    //如果想通过position获取对应的item，则需要通过onScrollListener来获取当前显示的
    public void getChildViewClick(View view) {
        ViewGroup child0View = (ViewGroup) mListView.getChildAt(0);
        TextView child0Tv = (TextView) child0View.getChildAt(0);
        ToastUtils.showShort("第index个item的内容：" + child0Tv.getText().toString());
    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }
}
