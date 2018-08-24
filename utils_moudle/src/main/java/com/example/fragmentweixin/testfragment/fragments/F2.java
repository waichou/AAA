package com.example.fragmentweixin.testfragment.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fragmentweixin.base.BaseFragment;
import com.example.fragmentweixin.testfragment.interfaces.IF2Interface;
import com.utils_moudle.R;

public class F2 extends BaseFragment implements IF2Interface{

	public static F2 getInstance(int rootLayoutId){ 
		F2 f2 = new F2();
		Bundle bundle= new Bundle();
		bundle.putInt("root_layout_id", rootLayoutId);
		f2.setArguments(bundle);
		return f2;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Button button = (Button) view.findViewById(R.id.f2_btn_id);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "fragment2 is click !", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	protected int setFragmentRootView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		return R.layout.test_f2_layout;
	}

	@Override
	protected void handleSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleRestoreSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String send2F2Fragment(String s) {
	     
		return "f2接收到数据了，内容为：[" + s +"]";
	}  
}
