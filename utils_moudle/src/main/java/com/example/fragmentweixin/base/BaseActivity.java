package com.example.fragmentweixin.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.fragmentweixin.testfragment.eventbus.EventMsg;
import com.example.fragmentweixin.testfragment.interfaces.IFragmentListener;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseActivity extends FragmentActivity implements IFragmentListener{
	
	public boolean isDispatchBoolean = false;//默认不拦截
	
	protected long mPreAnimationTiem = 0;
	protected long mLastAnimationTime = 0;
	
	protected FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (setContentRootView() == 0) {
			throw new IllegalArgumentException("Please instantiate the root layout of the Activity");
		}
		
		//百度地图SDK的加载是在setContentView之间定义的，暂时没有提供此种方法
		
		//设置根布局
		setContentView(setContentRootView());
		
		mFragmentManager = getSupportFragmentManager();
		
		registMouble();
		
		initViews();
		
		initDatas();
		
		
		handleRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * 注册组件
	 */
	public void registMouble() {//空实现
	}
	/**
	 * 注销组件
	 */
	public void unRegistMouble() {//空实现
	}

	protected abstract int setContentRootView();
	
	/**
	 * 
	 * @param savedInstanceState
	 */
	protected void handleRestoreInstanceState(Bundle savedInstanceState){}
	
	protected void initDatas() {}
	
	protected void initViews(){}
	
    protected void supportCommit(FragmentManager fm, FragmentTransaction transaction) {
        //保存事务异常之后在执行事务提交
        //handleAfterSaveInStateTransactionException(fm, "commit()");
        transaction.commitAllowingStateLoss();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	
    	handlerSaveInstanceState(outState);
    }
    
    protected void handlerSaveInstanceState(Bundle outState){}
    
    @Override
    public void setCostAnimDuration(long time) {
    	mLastAnimationTime = time;
    }
    
    @Override
	public void setLastCostAnimDuration(long time) {
    	mPreAnimationTiem = time;
	}
    
    @Override
	public void setIsDispatchBoo(boolean isDispatch) {
		isDispatchBoolean = isDispatch;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isDispatchBoolean) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void commitFrament2Fragment() {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegistMouble();
	}
	
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveEventMsg(int tag,EventMsg msg){
		if (msg != null && tag == 1) {
			Toast.makeText(this, "接收来自于Activity的信息："+ msg.object.toString(), 0).show();
		}
	}
}
