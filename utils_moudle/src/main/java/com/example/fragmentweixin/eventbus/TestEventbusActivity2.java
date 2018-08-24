package com.example.fragmentweixin.eventbus;

import android.os.Looper;
import android.view.View;

import com.example.fragmentweixin.base.BaseActivity;
import com.utils_moudle.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class TestEventbusActivity2 extends BaseActivity {

	@Override
	protected int setContentRootView() {
		return R.layout.main_test_eventbus_layout;
	}

	
	public void sendMsgFromMainThread(View view){
		printCurThreadInfo();
		EventBus.getDefault().post("来自222主线程的消息！");
		
//		EventBus.getDefault().postSticky("来自主线程的STICKY消息！");
	}
	
	public void sendMsgFromChildThread(View view){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				printCurThreadInfo();
				EventBus.getDefault().post("来自222子线程的消息！");
			}
		}).start();
		
//		EventBus.getDefault().register(this);
	}
	
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true,priority=3) 
	public void abb(String msg){ 
		System.out.println("2222-------------------abb-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true,priority=3) 
	public void abc(String msg){ 
		System.out.println("222-------------------abc-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=100*/) 
	public void receiveMsg1(String msg){ 
		System.out.println("222-------------------1-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void receiveMsg2(String msg){ 
		System.out.println("222-------------------2-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void acc(String msg){ 
		System.out.println("222-------------------aaa-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void wwc(String msg){ 
		System.out.println("222-------------------wwc-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	public boolean isRunMainThread(Looper myLooper){
		return Looper.getMainLooper() == myLooper;
	}
	
	//输出当前线程name，线程id
	public void printCurThreadInfo(){
//		String curThreadInfo = Thread.currentThread().getName() + "---" + Thread.currentThread().getId();
//		System.out.println("thread info=" + curThreadInfo);
	}
	
	@Override
	public void registMouble() {
		EventBus.getDefault().register(this);
	}
	
	@Override
	public void unRegistMouble() {
		EventBus.getDefault().unregister(this);
	}
}
