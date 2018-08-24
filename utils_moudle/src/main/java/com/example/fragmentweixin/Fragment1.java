package com.example.fragmentweixin;

import java.util.ArrayList;
import java.util.List;

import com.example.fragmentweixin.base.BaseFragment;
import com.utils_moudle.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment1 extends BaseFragment {
	
	private TextView mTextView;
	private ListView listView;
	private ArrayAdapter<String> adapter;

	public static Fragment1 getInstance(String arguParams){
		Fragment1 fragment1 = new Fragment1();
		Bundle bundle = new Bundle();
		bundle.putString("arugment", arguParams);
		fragment1.setArguments(bundle);
		return fragment1;
	}
	
	@Override
	protected int setFragmentRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			String argu = arguments.getString("arugment");
			Toast.makeText(mActivity, "获取到的arguMent参数值："+ argu, Toast.LENGTH_SHORT).show();
		}
		return R.layout.fragment1;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTextView = (TextView) view.findViewById(R.id.text_f1_view_id);
		mTextView.setText("这是个New World！之" + mSaveState);
		
		listView = (ListView)view.findViewById(R.id.weixin_lv);
		List<String> list = new ArrayList<String>();
		list = getData();
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
	}
	
	/**
	 * 处理当前fragment显隐的回调函数
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		System.out.println("fragment1 ---" + this.getClass().getSimpleName() + "-----" + hidden);
		if (hidden) {
			System.out.println("被hidden");
		}else {
			System.out.println("被显示");
		}
	}
	
		
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("fragment1 ondestroy");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("fragment1 ondetach");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		System.out.println("fragement1 ondestroyview");
	}

	private String mSaveState;
	
	@Override
	protected void handleSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString("saveState", "好好Study");
	}
	
	@Override
	protected void handleRestoreSaveInstanceState(Bundle outState) {
		mSaveState = outState.getString("saveState");
		System.out.println("savestate="+ mSaveState);
	}
	
	private List<String> getData() {
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 12; i++)
			list.add("测试" + i);
		return list;
	}
	
	/**
	 * 更新刷新
	 * @param updateDataList
	 */
	public void updateDataList(){
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 12; i++){
			list.add("测试-新数据" + i);
		}
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		return super.onCreateAnimation(transit, enter, nextAnim);
	}
	
}
