package com.example.fragmentweixin.eventbus;

import android.content.Intent;
import android.os.Looper;
import android.view.View;

import com.example.fragmentweixin.base.BaseActivity;
import com.utils_moudle.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 
 * @author Wei.zhou
 * @date 2018年7月13日
 * @time 上午8:23:08
 * @desc 该工程模拟测试出四种类别的不同以及优先级问题，发送sticky粘性事件
 *  
	订阅者模式分为四类：
	
	ThreadMode.MAIN          不管从哪个线程发出的事件，MAIN模式都会在UI（主线程）线程执行
	ThreadMode.POSTING       事件从哪个线程发布出来的就会在该线程中运行
	
	ThreadMode.BACKGROUND    如果发送事件的线程是UI线程，则重新创建新的子线程执行，因此不能执行更新UI的操作
	                                                  如果发送事件的线程是子线程，则直接在和当前发送方同一线程[表示线程是同一个]中接收消息
	                                                  
	ThreadMode.ASYNC         不管从哪个线程发出的事件，ASYNC模式都会创建一个新的子线程来执行。
	
	粘性事件:
	1.执行postSticky
	2. 默认就是非粘性事件，粘性事件，只需要在事件的注解上面加上 sticky = true
	
	如果接收类型相同且有多个接收者情况下：
	<1>如果设定了优先级，按照优先级从大到小依次执行接收消息
	<2>如果优先级按照顺序接收完毕后，则剩余接收方法按照字典从小到大的顺序接收消息
	<3>如果优先级相同的情况下：同样按照字典从小到大的顺序接收消息
	
 */
public class TestEventbusActivity extends BaseActivity {

	@Override
	protected int setContentRootView() {
		return R.layout.main_test_eventbus_layout;
	}

	
	public void sendMsgFromMainThread(View view){
		printCurThreadInfo();
		EventBus.getDefault().post("来自111主线程的消息！");
		
//		EventBus.getDefault().postSticky("来自主线程的STICKY消息！");
	}
	
	public void sendMsgFromChildThread(View view){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				printCurThreadInfo();
				EventBus.getDefault().post("来自111子线程的消息！");
			}
		}).start();
		
//		EventBus.getDefault().register(this);
	}
	
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true,priority=3) 
	public void abb(String msg){ 
		System.out.println("111-------------------abb-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true,priority=3) 
	public void abc(String msg){ 
		System.out.println("111-------------------abc-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=100*/) 
	public void receiveMsg1(String msg){ 
		System.out.println("111-------------------1-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void receiveMsg2(String msg){ 
		System.out.println("111-------------------2-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void acc(String msg){ 
		System.out.println("111-------------------aaa-------------------");
		printCurThreadInfo();
		if (isRunMainThread(Looper.myLooper())) {//注：Looper.myLooper表示当前线程的looper
			System.out.println("main thread receive msg:" + msg);
		}else {
			System.out.println("child thread receive msg:" + msg);
		}
	}
	
	@Subscribe(threadMode=ThreadMode.PostThread,sticky=true/*,priority=200*/) 
	public void www(String msg){ 
		System.out.println("111-------------------www-------------------");
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
	
	public void jump2Activity(View view){
		startActivity(new Intent(this,TestEventbusActivity2.class));
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
