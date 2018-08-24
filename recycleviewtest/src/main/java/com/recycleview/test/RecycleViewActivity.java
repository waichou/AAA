package com.recycleview.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.recycleview.test.adapter.LGRecycleViewAdapter;
import com.recycleview.test.adapter.LGViewHolder;
import com.recycleview.test.bean.Bean1;
import com.recycleview.test.bean.Bean2;
import com.recycleview.test.itemItem_decor.DividerDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author Wei.zhou
 * @date 2018年7月13日
 * @time 上午11:44:40
 * @desc Recycleview的封装集成，处理不同typelayout的展示实例 
 * 
 * ##adapter的生命周期：
     adapter - onAttachedToRecyclerView   新调用setAdapter的时候，会回调此方法进行关联关系
	 adapter - getItemCount 
	 adapter - getItemViewType 
	 adapter - onCreateViewHolder
	 adapter - onBindViewHolder
	 adapter - onViewAttachedToWindow
     ------------------------------------
     adapter - onDetachedFromRecyclerView  重新设置adapter的时候，会回调到recycleview之前的adapter-ondetached方法上
 */
public class RecycleViewActivity extends Activity {

    @Bind(R.id.id_recycle_view)
    RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private MainAdapter mainAdapter;
    List<Object> datas = new ArrayList<>();

    @Bind(R.id.scroll_ed_id)
    EditText scorllEd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_recycleview);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        datas.add(new Bean1("bean 1-0"));
        datas.add(new Bean1("bean 1-1"));

        datas.add(new Bean2("bean 2-2"));
        datas.add(new Bean2("bean 2-3"));

        datas.add(new Bean1("bean 1-4"));

        datas.add(new Bean2("bean 2-5"));

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerDecoration());
        
        mainAdapter = new MainAdapter(datas);
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(layoutManager);
        
        mainAdapter.setOnItemClickListener(R.id.root1, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);
            }
        });
        mainAdapter.setOnItemClickListener(R.id.root2, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);
            }
        });

        recyclerView.addOnScrollListener(new RecyScorllListener());
    }

    //------------------------------------ 滚动监听 目的为了要回到指定position上去 -------------------------------------start
    //参考：RecyclerView跳转到指定位置的三种方式 https://blog.csdn.net/huangxiaoguo1/article/details/53706971
    private class RecyScorllListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            System.out.println("onScrollStateChanged is called!");
        }
    }

    /**
     * 记录目标项位置
     */
    private int mToPosition;

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    //
    public void scrollToPositionClick(View view) {
        String inputPosition = scorllEd.getText().toString();
        smoothMoveToPosition(recyclerView,Integer.parseInt(inputPosition));
    }
//------------------------------------ 滚动监听 目的为了要回到指定position上去 -------------------------------------end
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainAdapter.destroyAdapter();
    }

    private static class MainAdapter extends LGRecycleViewAdapter<Object> {

        public MainAdapter(List<Object> dataList) {
            super(dataList);
        }

        //根据标签确定要显示的布局layout
        @Override
        public int getLayoutId(int viewType) {//当前position位置对应的类型int值来对应加载对应不同的布局layout
            if(viewType == 1)
                return R.layout.recycle_item_view_main1;
            return R.layout.recycle_item_view_main2;//默认加载布局layout
        }

        //支持不同viewType视图 ,形象说法贴标签
        @Override
        public int getItemViewType(int position) {
        	System.out.println("adapter - getItemViewType");
        	
            Object model = getItem(position);//通过item来确定当前position位置对应的类型int值
            if (model instanceof  Bean1){
                return 1;
            }
            return 2;
        }

        /**
         * //需要通过当前item所要展示的layout决定当前数据源要赋值到哪个layout上面
         * @param holder
         * @param s
         * @param position
         */
        @Override
        public void convert(LGViewHolder holder, Object s, final int position) {

            if (s instanceof Bean1){
                Bean1 bean1 = (Bean1) s;
                TextView textView = (TextView) holder.getView(R.id.id_text1);
                textView.setText("bean 1->" + bean1.getBean1Name());
                
                //swapSelectBackground(holder, position,R.id.root1);
                
            }else if (s instanceof Bean2){
                Bean2 bean2 = (Bean2) s;
                TextView textView = (TextView) holder.getView(R.id.id_text2);
                textView.setText("bean 2->" + bean2.getBean2Name());
                
                //swapSelectBackground(holder, position,R.id.root2);
            }
           
        }
        
        //RecyclerView 调用了 setAdapter() 时会触发，
        //旧的 adapter 回调 onDetached，新的 adapter 回调 onAttached
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        	super.onAttachedToRecyclerView(recyclerView);
        	
        	System.out.println("adapter - onAttachedToRecyclerView");
        }
        
        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        	super.onDetachedFromRecyclerView(recyclerView);
        	
        	System.out.println("adapter - onDetachedFromRecyclerView");
        }

        /************************************* 设置选中item时背景抬起不变 start  ***********************************************/
		private void swapSelectBackground(LGViewHolder holder, final int position,int viewId) {
			if (mPosition == position) {
				holder.getView(viewId).setBackgroundColor(Color.RED);
			}else {
				holder.getView(viewId).setBackgroundColor(Color.WHITE);
			}
		}
        
        private int mPosition;
        public void setChosedItemPosition(int position){
        	this.mPosition = position;
        	notifyDataSetChanged();
        }
        /************************************* 设置选中item时背景抬起不变 end  ***********************************************/
    }

    
    /*
	    onAttachedToRecyclerView() 
	    onDetachedFromRecyclerView()：
	
	        这两个回调则是当 RecyclerView 调用了 setAdapter() 时会触发，
	        旧的 adapter 回调 onDetached，新的 adapter 回调 onAttached。
    */
    public void testAttachForSetAdapter(View view){
        recyclerView.setAdapter(null);
    }
    
}
