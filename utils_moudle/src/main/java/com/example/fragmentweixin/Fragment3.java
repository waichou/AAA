package com.example.fragmentweixin;

import com.example.fragmentweixin.base.BaseFragment;
import com.utils_moudle.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment3 extends BaseFragment {
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment3 ---" + this.getClass().getSimpleName() + "-----" + hidden);
	}

	@Override
	protected void handleSaveInstanceState(Bundle savedInstanceState) {
		
	}

	@Override
	protected int setFragmentRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return R.layout.fragment3;
	}

	@Override
	protected void handleRestoreSaveInstanceState(Bundle outState) {
		
	}
}
