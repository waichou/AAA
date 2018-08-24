package com.framworks.base.main.fragments.childfragment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framworks.base.main.fragments.childfragment.beans.Msg;
import com.framworks.base.rv.adapter.LGRecycleViewAdapter;
import com.framworks.base.rv.adapter.LGViewHolder;
import com.utils_moudle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class MsgAdapter extends LGRecycleViewAdapter<Msg>{

    @Override
    public int getItemViewType(int position) {
        Msg msg = dataList.get(position);
        if (msg.isLeftOrRight){
            return 0;
        }
        return 1;
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == 0){
            return R.layout.first_2_adapter_left_item;
        }
        return R.layout.first_2_adapter_right_item;
    }

    @Override
    public void convert(LGViewHolder holder, Msg msg, int position) {
        holder.getTextView(R.id.tv_msg).setText(msg.message);
    }

    public void addMsg(Msg msg) {
        dataList.add(msg);
        notifyDataSetChanged();
    }

}
