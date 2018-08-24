package com.framworks.base.main.fragments.childfragment.adapters;

import android.support.v7.widget.RecyclerView;

import com.framworks.base.main.fragments.childfragment.beans.FirstBean;
import com.framworks.base.rv.adapter.LGRecycleViewAdapter;
import com.framworks.base.rv.adapter.LGViewHolder;
import com.utils_moudle.R;

import java.util.List;

/**
 * Created by zhouwei on 2018/7/29.
 */

public class FirstAdapter extends LGRecycleViewAdapter<FirstBean> {

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.adapter_first_item;
    }

    @Override
    public void convert(LGViewHolder holder, FirstBean firstBean, int position) {
        holder.getTextView(R.id.tv_name).setText(firstBean.name);
        holder.getTextView(R.id.add).setText(firstBean.message);
    }

}
