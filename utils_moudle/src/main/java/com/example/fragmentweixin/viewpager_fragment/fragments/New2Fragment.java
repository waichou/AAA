package com.example.fragmentweixin.viewpager_fragment.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.fragmentweixin.base.BaseFragment;
import com.utils_moudle.R;

public class New2Fragment extends BaseFragment {

	public static final String TAG = New2Fragment.class.getSimpleName();
	@Bind(R.id.viewpager_item_tv_id)
	TextView mTextView;
	
	public static New2Fragment NewInstance(Bundle bundle) {
		New2Fragment newFragment = new New2Fragment();
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	protected int setFragmentRootView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return R.layout.viewpager_fragment_item;
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		String type = getArguments().getString("type");
		mTextView.setText(type);
	}
	
	@Override
	protected void onFragmentFirstVisible() {
		System.out.println("-->当前显示的Fragment["+TAG+"]加载数据!");
	}
	
	@Override
	protected void onFragmentVisibleChange(boolean isVisible) {
		System.out.println("-->当前显示的Fragment["+TAG+"]的显隐状态为：" + isVisible);
	}
	
	@Override
	protected void registMouble() {
		ButterKnife.bind(this, mRootView);
	}

	@Override
	protected void unRegistMouble() {
		ButterKnife.unbind(this);
	}

}
