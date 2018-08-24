package com.example.fragmentweixin.recycleview.rv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author Wei.zhou
 * @date 2018年7月28日
 * @time 下午6:16:18
 * @desc 为RecyclerView添加EmptyView 
 *       参看：https://blog.csdn.net/LosingCarryJie/article/details/78070445
 *
 * 通过观察者模式来动态控制空布局和RecyclerView的显隐切换问题，注意的是empty view 仍然要定义和recyclerview同级
 */
public class CustomRecyclerView extends RecyclerView {

	private View emptyView;
	private static final String TAG = "LosingCarryJie";
	
	public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomRecyclerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	final private AdapterDataObserver observer = new AdapterDataObserver() {
		@Override
		public void onChanged() {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeInserted(int positionStart, int itemCount) {
			checkIfEmpty();
		}

		@Override
		public void onItemRangeRemoved(int positionStart, int itemCount) {
			checkIfEmpty();
		}
	};

	private void checkIfEmpty() {
		if ((emptyView != null && getAdapter() != null) || ( emptyView != null && getAdapter() == null)) {
			final boolean emptyViewVisible = getAdapter() == null? true:getAdapter().getItemCount() == 0;
			emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
			setVisibility(emptyViewVisible ? GONE : VISIBLE);
		}
	}

	@Override
	public void setAdapter(Adapter adapter) {
		final Adapter oldAdapter = getAdapter();
		if (oldAdapter != null) {
			oldAdapter.unregisterAdapterDataObserver(observer);
		}
		super.setAdapter(adapter);
		if (adapter != null) {
			adapter.registerAdapterDataObserver(observer);
		}
		checkIfEmpty();
	}

	public void setEmptyView(View view) {
		this.emptyView = view;
		checkIfEmpty();
	}

}
