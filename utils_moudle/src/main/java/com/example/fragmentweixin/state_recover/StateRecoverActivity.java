package com.example.fragmentweixin.state_recover;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.fragmentweixin.base.BaseActivity;
import com.example.fragmentweixin.state_recover.fragment.FragmentStateRecvoer;
import com.utils_moudle.R;

/**
 * 
 * @author Wei.zhou
 * @date 2018年7月8日
 * @time 下午1:37:47
 * @desc Android，如何保存Fragment的状态 
 *       参考：https://www.jianshu.com/p/580ec2951c39
 *              
 */
public class StateRecoverActivity extends BaseActivity {

	private FragmentStateRecvoer f1;

	@Override
	protected int setContentRootView() {
		return R.layout.test_state_recover_main;
	}

	@Override
	protected void handleRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			f1 = (FragmentStateRecvoer) mFragmentManager.getFragment(savedInstanceState, FragmentStateRecvoer.class.getSimpleName());
		}else {
			if (f1 == null) {
				f1 = FragmentStateRecvoer.getInstance(R.id.recover_content_id,true);
				mFragmentManager.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.add(R.id.recover_content_id, f1,FragmentStateRecvoer.class.getSimpleName()).commit();
			}
		}
	}

	@Override
	protected void handlerSaveInstanceState(Bundle outState) {
		super.handlerSaveInstanceState(outState);
		mFragmentManager.putFragment(outState, FragmentStateRecvoer.class.getSimpleName(),f1);
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	public void sendValue2F1(View view){
		f1.changeValue("女孩漂亮！");
	}
}
