package com.example.fragmentweixin.recycleview;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.fragmentweixin.base.BaseActivity;
import com.example.fragmentweixin.recycleview.adapter.LGRecycleViewAdapter;
import com.example.fragmentweixin.recycleview.adapter.LGViewHolder;
import com.example.fragmentweixin.recycleview.bean.Bean1;
import com.example.fragmentweixin.recycleview.bean.Bean2;
import com.example.fragmentweixin.recycleview.rv.CustomRecyclerView;
import com.utils_moudle.R;

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
public class RecycleViewActivity2 extends BaseActivity {

    @Bind(R.id.id_recycle_view)
    CustomRecyclerView recyclerView;

    private MainAdapter mainAdapter;
    List<Object> datas = new ArrayList<>();

    @Override
    protected int setContentRootView() {
        return R.layout.main_recycleview_include_empty_view;
    }

    @Override
    public void registMouble() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        loadData();

        recyclerView.setHasFixedSize(true);
        recyclerView.setEmptyView(findViewById(R.id.empty_view_id));

        mainAdapter = new MainAdapter(datas);
        recyclerView.setAdapter(mainAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        mainAdapter.setOnItemClickListener(R.id.root1, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);

                Toast.makeText(getApplicationContext(), "root1 is called!", Toast.LENGTH_SHORT).show();
            }
        });
        mainAdapter.setOnItemClickListener(R.id.root2, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);

                Toast.makeText(getApplicationContext(), "root2 is called!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainAdapter.destroyAdapter();
    }

    private class MainAdapter extends LGRecycleViewAdapter<Object> {

        public MainAdapter(List<Object> dataList) {
            super(dataList);
        }

        //根据标签确定要显示的布局layout
        @Override
        public int getLayoutId(int viewType) {//当前position位置对应的类型int值来对应加载对应不同的布局layout
            if(viewType == 1)
                return R.layout.recycle_item_view_main1;
            else if (viewType == 2) {
                return R.layout.recycle_item_view_main2;//默认加载布局layout
            }
            return -1;
        }

        //支持不同viewType视图 ,形象说法贴标签
        @Override
        public int getItemViewType(int position) {
            System.out.println("adapter - getItemViewType");

            Object model = getItem(position);//通过item来确定当前position位置对应的类型int值
            if (model instanceof  Bean1){
                return 1;
            }else if (model instanceof Bean2) {
                return 2;
            }
            return -1;
        }

        @Override
        public void onViewAttachedToWindow(LGViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            System.out.println("adapter - onViewAttachedToWindow");


            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                int mPosition = holder.getPosition();
//	            if (mPosition % 2 == 0 ) {
//	            	p.setFullSpan(true);//如果参数为true，表示每个itemview占满全屏
//				}else {
//					p.setFullSpan(false);
//				}

                if (mPosition == 0) {
                    p.setFullSpan(true);
                }else {
                    p.setFullSpan(false);
                }
            }
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
//                holder.itemView.setBackgroundColor(Color.parseColor("#FAFAD2"));

            }else if (s instanceof Bean2){
                Bean2 bean2 = (Bean2) s;
                TextView textView = (TextView) holder.getView(R.id.id_text2);
                textView.setText("bean 2 -->>" + bean2.getBean2Name());

                //swapSelectBackground(holder, position,R.id.root2);
//                holder.itemView.setBackgroundColor(Color.parseColor("#FFE4B5"));
            }

            if (getItemViewType(position) == 1) {
                LinearLayout.LayoutParams params = (LayoutParams) holder.getView(R.id.icon1).getLayoutParams();
                params.height = 300;
                holder.getView(R.id.icon1).setLayoutParams(params);

            }else if (getItemViewType(position) == 2) {
                LinearLayout.LayoutParams params = (LayoutParams) holder.getView(R.id.icon2).getLayoutParams();
                params.height = 500;
                holder.getView(R.id.icon2).setLayoutParams(params);
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
//    public void testAttachForSetAdapter(View view){
//        recyclerView.setAdapter(null);
//    }

    private void loadData(){
        datas.add(new Bean1("bean 1 ----0"));
        datas.add(new Bean2("bean 2 ----0"));
        datas.add(new Bean2("bean 2 ----1"));
        datas.add(new Bean1("bean 1 ----1"));
        datas.add(new Bean1("bean 1 ----2"));
        datas.add(new Bean2("bean 2 ----2"));
        datas.add(new Bean2("bean 2 ----3"));
        datas.add(new Bean1("bean 1 ----3"));
        datas.add(new Bean2("bean 2 ----4"));
    }
    public void showEmptyClick(View view){
        datas.clear();
        mainAdapter.updateEmpty(datas);
    }

    public void showDataClick(View view){
        loadData();
        mainAdapter.updateEmpty(datas);
    }

}
