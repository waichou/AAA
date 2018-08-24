package com.framworks.base.rv.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.framworks.base.rv.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guizhigang on 16/8/8.
 */
public abstract class LGRecycleViewAdapter<T> extends RecyclerView.Adapter<LGViewHolder> {
    //存储监听回调
    private SparseArray<ItemClickListener> onClickListeners;

    protected List<T> dataList;

    public interface ItemClickListener {
        void onItemClicked(View view, int position);
    }

    public LGRecycleViewAdapter(List<T> dataList) {
        this.dataList = dataList;
        onClickListeners = new SparseArray<>();
    }

    public LGRecycleViewAdapter() {
        onClickListeners = new SparseArray<>();
    }

    /**
     * 存储viewId对应的回调监听实例listener
     * @param viewId
     * @param listener
     */
    public void setOnItemClickListener(int viewId,ItemClickListener listener) {
        ItemClickListener listener_ = onClickListeners.get(viewId);
        if(listener_ == null){
            onClickListeners.put(viewId,listener);
        }
    }

    /**
     * 获取列表控件的视图id(由子类负责完成)
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    //更新itemView视图(由子类负责完成)
    public abstract void convert(LGViewHolder holder, T t, int position);

    public T getItem(final int position){
        if(dataList == null)
            return null;
        return dataList.get(position);
    }

    @Override
    public LGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	System.out.println("adapter - onCreateViewHolder");
    	
        int layoutId = getLayoutId(viewType);
        LGViewHolder viewHolder = LGViewHolder.getViewHolder(parent, layoutId);//对应不同的layout类型实例化对应的viewhodler对象
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LGViewHolder holder, final int position) {
    	System.out.println("adapter - onBindViewHolder");
    	
        T itemModel = dataList.get(position);

        convert(holder, itemModel, position);//更新itemView视图

        //设置点击监听
        for (int i = 0; i < onClickListeners.size(); ++i){
            int id = onClickListeners.keyAt(i);//查找index下标中对应存储的键值，这里就是获取item id
            View view = holder.getView(id);//根据item id 获取对应的view实例
            if(view == null)//没有获取到，则跳过
                continue;
            final ItemClickListener listener = onClickListeners.get(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClicked(v,position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
    	System.out.println("adapter - getItemCount");
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    /**
     * 获取数据源
     * @return
     */
    public List<T> getDataSet() {
        return this.dataList;
    }

    /**
     * 设置数据源
     * @param list
     */
    public void setDataSet(List<T> list) {
        if (list == null)
            return;
        this.dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 追加数据源
     * @param list
     */
    public void addDataSet(List<T> list) {
        if (list != null) {
            this.dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 更新数据源
     * @param list
     */
    public void updateDataSet(List<T> list) {
        if (list != null) {
            setDataSet(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 销毁资源
     */
    public void destroyAdapter(){
        if(onClickListeners != null)
            onClickListeners.clear();
        onClickListeners = null;

        if(dataList != null)
            dataList.clear();
        dataList = null;
    }
}