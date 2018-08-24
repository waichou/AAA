package com.example.fragmentweixin.state_recover.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fragmentweixin.base.BaseFragment;
import com.example.fragmentweixin.state_recover.intefaces.ISupportFragment;
import com.example.fragmentweixin.state_recover.resultrecord.ResultRecord;
import com.utils_moudle.R;


public class FragmentStateRecvoer2 extends BaseFragment implements ISupportFragment{
	
	private static final String REQUET_PARMAS_CODE = "requet_parmas_CODE";
	
	public static FragmentStateRecvoer2 getInstance(ResultRecord record){ 
		FragmentStateRecvoer2 f1 = new FragmentStateRecvoer2();
		Bundle bundle= new Bundle();
		bundle.putParcelable(REQUET_PARMAS_CODE, record);
		f1.setArguments(bundle);
		return f1;
	}
	
	EditText tButton;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		System.out.println("生命周期：onViewCreated");
		
		//f1切换到f2到相同布局id
		tButton = (EditText) view.findViewById(R.id.state_recover_ed2_id);
		
		view.findViewById(R.id.pop_btn2_id).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle arguments = getArguments();
				ResultRecord fRecord = arguments.getParcelable(REQUET_PARMAS_CODE);
				fRecord.resultCode = RESULT_OK;
				
				Bundle resultBundle = new Bundle();
				resultBundle.putString("result_data", "你好猴子！");
				fRecord.resultBundle = resultBundle;
				
				mActivity.getSupportFragmentManager().popBackStack();
			}
		});
	
	}
	
	@Override
	protected void handleRestoreSaveInstanceState(Bundle savedInstanceState) {
		
	}

	//临时测试用的
	public void changeValue(String btnContent){
		tButton.setText("傻瓜是你，" + btnContent);
	}

	@Override
	protected int setFragmentRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return R.layout.state_recover_fragment2;
	}

	@Override
	protected void handleSaveInstanceState(Bundle outState) {
		 
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();//调用父类完成必要操作
		//调用父类方法handleResultRecord(fragment)来完成源fragment接收数据
		handleResultRecord(this,REQUET_PARMAS_CODE, FragmentStateRecvoer.class.getSimpleName());
		
	}
}
