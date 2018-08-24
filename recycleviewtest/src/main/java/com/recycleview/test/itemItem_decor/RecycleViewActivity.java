package com.recycleview.test.itemItem_decor;

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
import android.widget.TextView;

import com.recycleview.test.R;
import com.recycleview.test.adapter.LGRecycleViewAdapter;
import com.recycleview.test.adapter.LGViewHolder;
import com.recycleview.test.bean.Bean1;
import com.recycleview.test.bean.Bean2;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.main_recycleview);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        datas.add(new Bean1("bean 1 ----0"));
        datas.add(new Bean1("bean 1 ----1"));

        datas.add(new Bean2("bean 2 ----0"));
        datas.add(new Bean2("bean 2 ----1"));

        datas.add(new Bean1("bean 1 ----2"));

        datas.add(new Bean2("bean 2 ----2"));

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
    }
    
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
                textView.setText("bean 1 -->>" + bean1.getBean1Name());
                
                //swapSelectBackground(holder, position,R.id.root1);
                
            }else if (s instanceof Bean2){
                Bean2 bean2 = (Bean2) s;
                TextView textView = (TextView) holder.getView(R.id.id_text2);
                textView.setText("bean 2 -->>" + bean2.getBean2Name());
                
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
