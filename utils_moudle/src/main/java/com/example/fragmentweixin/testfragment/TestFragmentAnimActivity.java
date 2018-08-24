package com.example.fragmentweixin.testfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.example.fragmentweixin.base.BaseActivity;
import com.example.fragmentweixin.base.BaseFragment;
import com.example.fragmentweixin.testfragment.activity2.SecondActivity;
import com.example.fragmentweixin.testfragment.constants.Constants;
import com.example.fragmentweixin.testfragment.eventbus.EventMsg;
import com.example.fragmentweixin.testfragment.fragments.F1;
import com.example.fragmentweixin.testfragment.fragments.F2;
import com.example.fragmentweixin.testfragment.service.MyIntentService;
import com.utils_moudle.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class TestFragmentAnimActivity extends BaseActivity{

	private BaseFragment f1;

	@Override
	public int setContentRootView() {
		return R.layout.test_fragemt_anim_main;
	}

	@Override
	public void registMouble() {
		EventBus.getDefault().register(this);  //注册 
	}
	
	@Override
	public void unRegistMouble() {
		EventBus.getDefault().unregister(this);//取消注册
	}
	
	@Override
	protected void handleRestoreInstanceState(Bundle savedInstanceState) {
		f1 = (BaseFragment) mFragmentManager.findFragmentByTag(F1.class.getSimpleName());
		if (f1 == null) {
			f1 = F1.getInstance(R.id.content,true);
			mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.content, f1,F1.class.getSimpleName()).commit();
		}
	}

	@Override
	protected void initViews() {
	}
 
	
	public void entryAnimClick(View view) {
		commitFrament2Fragment();
	}
	  
	public void exitAnimClick(View view) {
		 
		setIsDispatchBoo(true);
		
		//维护回退栈时调用此方法
		mFragmentManager.popBackStackImmediate();
		//不维护回退栈时调用下面的方法
//		mFragmentManager.beginTransaction().remove(f2).show(f1).commit();
		
		
		System.out.println("last time=" + mLastAnimationTime + ", pre time=" + mPreAnimationTiem);
		/**
		 * 下面的代码为了解决：如果在fragment之间切换过程中，存在切换动画，则弹栈需要在动画结束时间后释放activity的事件分发
		 */
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				setIsDispatchBoo(false);
				Toast.makeText(TestFragmentAnimActivity.this, "弹栈动画结束后，恢复Activity的分发操作！", 0).show();
			}
		}, mLastAnimationTime>=mPreAnimationTiem? mLastAnimationTime:mPreAnimationTiem);
	}

	/**
	 * Activity负责fragment的切换
	 */
	@Override
	public void commitFrament2Fragment() {
		setIsDispatchBoo(true);
		activity2fragmentForDiffLayout();
	}
	
	private void customAnimationFor2(){
		mFragmentManager.beginTransaction()
	       //第一个参数目标进入动画，第二个参数原退出动画
	       .setCustomAnimations(R.anim.h_fragment_enter, R.anim.h_fragment_pop_exit)
	       .add(R.id.content, F2.getInstance(R.id.content),F2.class.getSimpleName())
	       .hide(f1)
	       .addToBackStack(null)
	       .commitAllowingStateLoss();
	}
	
	/**
	 * 同布局id下切换fragment
	 */
	private void activity2fragmentForSameLayout(){
        mFragmentManager
        .beginTransaction()
//        .setCustomAnimations(R.anim.h_fragment_enter,R.anim.h_fragment_pop_exit,
//        		             R.anim.h_fragment_pop_enter,R.anim.h_fragment_exit)		
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//设置了默认动画 
        .add(R.id.content, F2.getInstance(R.id.content),F2.class.getSimpleName())
        .hide(f1)//
        .addToBackStack(null)
		.commit();
	}
	/**
	 * 不同布局id下切换fragment
	 */
	private void activity2fragmentForDiffLayout(){
		mFragmentManager
		.beginTransaction()
//        .setCustomAnimations(R.anim.h_fragment_enter,R.anim.h_fragment_pop_exit,
//        		             R.anim.h_fragment_pop_enter,R.anim.h_fragment_exit)		
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)//设置了默认动画 
		.add(R.id.content2, F2.getInstance(R.id.content2),F2.class.getSimpleName())
		 //.hide(f1)//注意：不要执行它，因为已经不在统一布局中显示了
		.addToBackStack(null)//根据情况处理是否添加
		.commit();
	}
	
	//Activity 传递数据给fragment
	public void sendData2Fragment(View view){
		EventBus.getDefault().post(new EventMsg(Constants.FLAG_1, "helle fragment 1 !"));
	}
	
	/**
	 * 接收来自fragment的数据
	 * @param msg
	 */
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveEventMsg(EventMsg msg){
		if (msg != null && msg.tag == Constants.FLAG_2) {//因为多处使用的是同一个类，使用tag是为了区分隔出属于自己的数据
			Toast.makeText(this, "接收来自于Fragment的信息："+ msg.object.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 启动后台服务
	 * @param view
	 */
	public void startService(View view){
		Intent startIntent = new Intent(this,MyIntentService.class);
		startIntent.setAction("myservice.test.service");
		startService(startIntent);
	}
	/**
	 * Activity告知Service停止服务运行
	 * @param msg
	 */
	public void stopService(View view){
		EventBus.getDefault().post(new EventMsg(Constants.FLAG_3, "来自Activity-停止运行吧！"));
	}
	
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveMsgFromService(EventMsg msg){
		if (msg!=null && msg.tag == Constants.FLAG_4) {
			Toast.makeText(this, "接收来自于Service的信息："+ msg.object.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * jump 2 secondactivity
	 * @param view
	 */
	public void jump2SecondActivity(View view){
		startActivity(new Intent(this,SecondActivity.class));
	}
	
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveMsgFromSecondActivity(EventMsg msg){
		if (msg!=null && msg.tag == Constants.FLAG_6) {
			Toast.makeText(this, "接收来自于SecondActivity的信息："+ msg.object.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		System.out.println("FActivity------------------------>");
		System.out.println("父Activity获取的参数=>" + arg2 != null? arg2.getStringExtra("key"):"");
	}
}
