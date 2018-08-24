package com.example.fragmentweixin.testfragment.service;

import com.example.fragmentweixin.testfragment.constants.Constants;
import com.example.fragmentweixin.testfragment.eventbus.EventMsg;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @author Wei.zhou
 * @date 2018年7月7日
 * @time 下午3:44:52
 * @desc 用于测试Eventbus传递任务完成消息
 */
public class MyIntentService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("-------myintentservice oncreate->");
		
		EventBus.getDefault().register(this);
	}

	private boolean isStop = false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!isStop) {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
					}
					System.out.println("service running !");
				}
				System.out.println("service stop !");
				stopSelf();
				//将此消息发送到activity | fragment
//				EventBus.getDefault().post(new EventMsg(Constants.FLAG_4, "服务已停止！"));
				EventBus.getDefault().post(new EventMsg(Constants.FLAG_5, "服务已停止！"));
			}
		}).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
   
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveMsg2StopService(EventMsg msg){
		if (msg != null && msg.tag == Constants.FLAG_3) {
			isStop = true;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("-------myintentservice ondestroy->");
		
		EventBus.getDefault().unregister(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
