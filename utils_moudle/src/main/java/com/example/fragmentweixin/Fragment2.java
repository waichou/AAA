package com.example.fragmentweixin;

import com.example.fragmentweixin.base.BaseFragment;
import com.utils_moudle.R;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends BaseFragment {
	
	@Override
	protected int setFragmentRootView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return R.layout.fragment2;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment2 ---" + this.getClass().getSimpleName() + "-----" + hidden);
		System.out.println("fragment2 ---" + isHidden());
	}
	
	@Override
	protected void handleSaveInstanceState(Bundle savedInstanceState) {
		
	}

	@Override
	protected void handleRestoreSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
	}
	
}
