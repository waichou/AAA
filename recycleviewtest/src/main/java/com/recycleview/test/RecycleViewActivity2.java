package com.recycleview.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.recycleview.test.adapter.LGRecycleViewAdapter;
import com.recycleview.test.adapter.LGViewHolder;
import com.recycleview.test.bean.Bean1;
import com.recycleview.test.bean.Bean2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Wei.zhou
 * @date 2018年7月13日
 * @time 上午11:44:40
 * @desc Recycleview的封装集成，处理不同typelayout的展示实例
 * <p>
 * ##adapter的生命周期：
 * adapter - onAttachedToRecyclerView   新调用setAdapter的时候，会回调此方法进行关联关系
 * adapter - getItemCount
 * adapter - getItemViewType
 * adapter - onCreateViewHolder
 * adapter - onBindViewHolder
 * adapter - onViewAttachedToWindow
 * ------------------------------------
 * adapter - onDetachedFromRecyclerView  重新设置adapter的时候，会回调到recycleview之前的adapter-ondetached方法上
 * <p>
 * 针对瀑布流效果时使用：StaggeredGridLayoutManager.LayoutParams params;
 * params.setFullSpan(true);//如果参数为true，表示当前itemView占满整个屏幕宽度显示
 * [配合：onViewAttachedToWindow 结合使用]
 * <p>
 * 针对网格效果Grid使用：GridLayoutManager manager 中方法如下：用于控制当前itemView显示是占满整个列宽显示，还是独列显示自己
 * gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
 * @Override public int getSpanSize(int position) {
 * return 0;
 * }
 * });
 * 如：设定grid每行显示2列，如果position=0时，return 2，则表示当前itemview显示整个列宽 ；return 1 ，则表示itemview显示独列
 */
public class RecycleViewActivity2 extends FragmentActivity {

    @Bind(R.id.id_recycle_view)
    RecyclerView recyclerView;
    @Bind(R.id.recycle_attach_id)
    Button recycleAttachId;
    @Bind(R.id.recycle_add_data_id)
    Button recycleAddDataId;
    @Bind(R.id.recycle_delete_data_id)
    Button recycleDeleteDataId;
    @Bind(R.id.recycle_update_data_id)
    Button recycleUpdateDataId;

    private MainAdapter mainAdapter;
    List<Object> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_recycleview);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        datas.add(new Bean1("bean 1 ----0"));
        datas.add(new Bean2("bean 2 ----0"));
        datas.add(new Bean2("bean 2 ----1"));
        datas.add(new Bean1("bean 1 ----1"));
        datas.add(new Bean1("bean 1 ----2"));
        datas.add(new Bean2("bean 2 ----2"));
        datas.add(new Bean2("bean 2 ----3"));
        datas.add(new Bean1("bean 1 ----3"));
        datas.add(new Bean2("bean 2 ----4"));

        recyclerView.setHasFixedSize(true);

        mainAdapter = new MainAdapter(datas);
        recyclerView.setAdapter(mainAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mainAdapter.setOnItemClickListener(R.id.root1, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);

                Toast.makeText(getApplicationContext(), "root1 is called!--->" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mainAdapter.setOnItemClickListener(R.id.root2, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                mainAdapter.setChosedItemPosition(position);

                Toast.makeText(getApplicationContext(), "root2 is called!--->" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainAdapter.destroyAdapter();
    }

    @OnClick({R.id.recycle_attach_id, R.id.recycle_add_data_id, R.id.recycle_delete_data_id, R.id.recycle_update_data_id,
            R.id.recycle_pl_update_data_id, R.id.recycle_pl_add_data_id, R.id.recycle_pl_delete_data_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recycle_attach_id:
                /*
                    onAttachedToRecyclerView()
                    onDetachedFromRecyclerView()：

                        这两个回调则是当 RecyclerView 调用了 setAdapter() 时会触发，
                        旧的 adapter 回调 onDetached，新的 adapter 回调 onAttached。
                */

                recyclerView.setAdapter(null);

                break;

            case R.id.recycle_add_data_id:
//                datas.add("new data ");
                datas.add(0, new Bean1("new bean1" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
//                mainAdapter.notifyItemInserted(0);//针对瀑布流的样式布局会影响整体布局的刷新，建议使用下面的方法notifyDataSetChanged
                mainAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
//                mainAdapter.notifyItemChanged(datas.size()-1,"zw");

                break;

            case R.id.recycle_delete_data_id:
                datas.remove(0);
//                mainAdapter.notifyItemRemoved(0);////针对瀑布流的样式布局会影响整体布局的刷新，建议使用下面的方法notifyDataSetChanged
                mainAdapter.notifyDataSetChanged();
                break;
            case R.id.recycle_update_data_id:

                datas.set(0, new Bean1("new update bean" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
                mainAdapter.notifyItemChanged(0);
                break;

            //---------------------------------------批量操作部分-------------------------------
            case R.id.recycle_pl_update_data_id:
                datas.set(0, new Bean1("new pl bean1" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
                datas.set(1, new Bean1("new pl bean1" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
                mainAdapter.notifyItemRangeChanged(0, 2);//更新指定位置上的item
                break;

            case R.id.recycle_pl_add_data_id:
                datas.add(0, new Bean1("new pl bean1" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
                datas.add(1, new Bean1("new pl bean1" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));

                mainAdapter.notifyDataSetChanged();
                break;
            case R.id.recycle_pl_delete_data_id:
                Iterator<Object> iterator = datas.iterator();

                int removeCount = 0;
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    iterator.remove();
                    removeCount++;
                    if (removeCount == 2) {
                        break;
                    }
                }
                mainAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 刷新带有图片的itemView中的其他子view
     */
    @OnClick(R.id.recycle_notify_include_img_id)
    public void onViewClicked() {
        datas.set(0, new Bean1("new update bean" + new SimpleDateFormat("HH:mm:ss:sss").format(new Date())));
//        mainAdapter.notifyItemChanged(0,"zw_tag");
        mainAdapter.notifyDataSetChanged();//
    }

    private class MainAdapter extends LGRecycleViewAdapter<Object> {

        public MainAdapter(List<Object> dataList) {
            super(dataList);
        }

        //根据标签确定要显示的布局layout
        @Override
        public int getLayoutId(int viewType) {//当前position位置对应的类型int值来对应加载对应不同的布局layout
            if (viewType == 1)
                return R.layout.recycle_item_view_main1;
            return R.layout.recycle_item_view_main2;//默认加载布局layout
        }

        //支持不同viewType视图 ,形象说法贴标签
        @Override
        public int getItemViewType(int position) {
            System.out.println("adapter - getItemViewType");

            Object model = getItem(position);//通过item来确定当前position位置对应的类型int值
            if (model instanceof Bean1) {
                return 1;
            }
            return 2;
        }

        @Override
        public void onBindViewHolder(LGViewHolder holder, int position, List<Object> payloads) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);//默认不处理
            }else{//自定义刷新文本数据
                  // 参看：https://blog.csdn.net/qq402164452/article/details/53464091
                  //https://blog.csdn.net/jdsjlzx/article/details/52893469
                String tag = (String) payloads.get(0);
                Object obj = datas.get(0);
                if (obj instanceof Bean1){
                    Bean1 bean= (Bean1) obj;
                    TextView tv = (TextView) holder.getView(R.id.id_text1);
                    tv.setText("" + bean.getBean1Name());
                }else if (obj instanceof  Bean2){
                    Bean2 bean = (Bean2) obj;
                    TextView tv = (TextView) holder.getView(R.id.id_text2);
                    tv.setText("" + bean.getBean2Name());
                }
            }
        }

        /**
         * 如果对于瀑布流效果显示时，对于add data情况，如果调用
         *
         * @param holder
         */
        @Override
        public void onViewAttachedToWindow(LGViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            System.out.println("adapter - onViewAttachedToWindow");


            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                int mPosition = holder.getPosition();
//	            if (mPosition % 2 == 0 ) {
//	            	p.setFullSpan(true);//如果参数为true，表示每个itemview占满全屏
//				}else {
//					p.setFullSpan(false);
//				}

//                if (mPosition == 0) {
//                    p.setFullSpan(true);
//                } else {
//                    p.setFullSpan(false);
//                }
            }
        }

        /**
         * //需要通过当前item所要展示的layout决定当前数据源要赋值到哪个layout上面
         *
         * @param holder
         * @param s
         * @param position
         */
        @Override
        public void convert(LGViewHolder holder, Object s, final int position) {

            if (s instanceof Bean1) {
                Bean1 bean1 = (Bean1) s;
                TextView textView = (TextView) holder.getView(R.id.id_text1);
                textView.setText("bean 1 -->>" + bean1.getBean1Name());

                //swapSelectBackground(holder, position,R.id.root1);
//                holder.itemView.setBackgroundColor(Color.parseColor("#FAFAD2"));

            } else if (s instanceof Bean2) {
                Bean2 bean2 = (Bean2) s;
                TextView textView = (TextView) holder.getView(R.id.id_text2);
                textView.setText("bean 2 -->>" + bean2.getBean2Name());

                //swapSelectBackground(holder, position,R.id.root2);
//                holder.itemView.setBackgroundColor(Color.parseColor("#FFE4B5"));
            }

            if (getItemViewType(position) == 1) {
                LayoutParams params = (LayoutParams) holder.getView(R.id.icon1).getLayoutParams();
                params.width = 340;
                params.height = 350;
                holder.getView(R.id.icon1).setLayoutParams(params);

            } else if (getItemViewType(position) == 2) {
                LayoutParams params = (LayoutParams) holder.getView(R.id.icon2).getLayoutParams();
                params.width = 340;
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
        private void swapSelectBackground(LGViewHolder holder, final int position, int viewId) {
            if (mPosition == position) {
                holder.getView(viewId).setBackgroundColor(Color.RED);
            } else {
                holder.getView(viewId).setBackgroundColor(Color.WHITE);
            }
        }

        private int mPosition;

        public void setChosedItemPosition(int position) {
            this.mPosition = position;
            notifyDataSetChanged();
        }
        /************************************* 设置选中item时背景抬起不变 end  ***********************************************/
    }

}
