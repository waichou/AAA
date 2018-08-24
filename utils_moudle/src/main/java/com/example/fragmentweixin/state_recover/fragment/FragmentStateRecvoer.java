package com.example.fragmentweixin.state_recover.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragmentweixin.base.BaseFragment;
import com.example.fragmentweixin.state_recover.resultrecord.ResultRecord;
import com.utils_moudle.R;


public class FragmentStateRecvoer extends BaseFragment {
	private final int REQUEST_CODE = 0x111;
	private static final String ROOT_LAYOUT_ID = "root_layout_id";
	
	public static FragmentStateRecvoer getInstance(int rootLayoutId,boolean isRootLoad){ 
		FragmentStateRecvoer f1 = new FragmentStateRecvoer();
		Bundle bundle= new Bundle();
		bundle.putInt(ROOT_LAYOUT_ID, rootLayoutId);
		bundle.putBoolean(IS_ROOT_LOAD, isRootLoad);
		f1.setArguments(bundle);
		return f1;
	}
	
	EditText tEditText ;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		System.out.println("生命周期：onViewCreated");
		
		//f1切换到f2到相同布局id
		tEditText = (EditText) view.findViewById(R.id.state_recover_ed_id);
		
		view.findViewById(R.id.state_recover_btn_id).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * 小技巧：fragment 和 fragment 数据交换
				 * 利用setArgument的方法将源fragment的引用对象设置bundle其中，
				 * 在目标fragment弹栈的生命周期方法onDestroy方法中，调用getFragment获取源fragment对象，
				 * 在利用此对象调用自身方法接收返回值。
				 */
				
				System.out.println("源fragment实例：" + mFragment.toString());
				
//				mActivity.getSupportFragmentManager().putFragment(arg0, arg1, arg2);
				
				
				ResultRecord record = new ResultRecord();
				record.requestCode = REQUEST_CODE;
				
				Fragment instance = FragmentStateRecvoer2.getInstance(record);
				
				Bundle targetArgus = instance.getArguments();
				mActivity.getSupportFragmentManager().putFragment(targetArgus, mFragment.getClass().getSimpleName(),mFragment);
				//
				mActivity
				.getSupportFragmentManager()
				.beginTransaction()
				.hide(mFragment)
				.add(R.id.recover_content_id, instance)
				.addToBackStack(null)
				.commitAllowingStateLoss();
			}
		});
	}
	
	@Override
	protected void handleRestoreSaveInstanceState(Bundle savedInstanceState) {
		
	}
	

	//临时测试用的
	public void changeValue(String btnContent){
		tEditText.setText("傻瓜是你，" + btnContent);
	}

	@Override
	protected int setFragmentRootView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return R.layout.state_recover_fragment;
	}

	@Override
	protected void handleSaveInstanceState(Bundle outState) {
		
	}
	
	/**
	 * 自定义方法接收来自目标fragment传递的数据
	 */
	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			String resultData = data.getString("result_data","没有获取到值!");
			Toast.makeText(mActivity, "result data is ["+resultData+"]", Toast.LENGTH_SHORT).show();
		}
	}
}
