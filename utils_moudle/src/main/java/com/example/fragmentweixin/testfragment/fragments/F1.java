package com.example.fragmentweixin.testfragment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fragmentweixin.base.BaseFragment;
import com.example.fragmentweixin.testfragment.activity2.SecondActivity;
import com.example.fragmentweixin.testfragment.constants.Constants;
import com.example.fragmentweixin.testfragment.eventbus.EventMsg;
import com.utils_moudle.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 
 * @author Wei.zhou
 * @date 2018年7月3日
 * @time 下午4:06:46
 * @desc
 * 
	07-03 16:06:22.836: I/System.out(13760): 生命周期：onCreateView
	07-03 16:06:22.846: I/System.out(13760): 生命周期：onCreateAnimation
	07-03 16:06:22.846: I/System.out(13760): 生命周期：onViewCreated
	07-03 16:06:22.846: I/System.out(13760): 生命周期：onActivityCreated（在此回调方法可以利用mActivity.findViewById获取view实例）

 */
public class F1 extends BaseFragment {
	
	private static final String ROOT_LAYOUT_ID = "root_layout_id";
	
	public static F1 getInstance(int rootLayoutId,boolean isRootLoad){ 
		F1 f1 = new F1();
		Bundle bundle= new Bundle();
		bundle.putInt(ROOT_LAYOUT_ID, rootLayoutId);
		bundle.putBoolean(IS_ROOT_LOAD, isRootLoad);
		f1.setArguments(bundle);
		return f1;
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		System.out.println("生命周期：onViewCreated");
		
		//f1切换到f2到相同布局id
		Button tButton = (Button) view.findViewById(R.id.text_f1_tv_id);
		tButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(mActivity, "f1 btn click !", 0).show();
				
				//第一种方式：fragment内部自身维护跳转任务
				self2Fragment();
				
				//第二种方法：activity负责维护跳转任务
//				mFragmentListener.commitFrament2Fragment();
			}
		});
		//f1传参到f2 (利用接口回调方式触发数据)
		Button tButton2 = (Button) view.findViewById(R.id.text_f2_tv_id);
		tButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				IF2Interface if2Interface = (IF2Interface) mActivity.getSupportFragmentManager().findFragmentByTag(F2.class.getSimpleName());
//				String receiveMsg = if2Interface.send2F2Fragment("f1发来的数据！");
//				Toast.makeText(mActivity, receiveMsg, Toast.LENGTH_SHORT).show();
			}
		});
		
		//f1传递参数到activity
		Button tButton3 = (Button) view.findViewById(R.id.text_f3_tv_id);
		tButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new EventMsg(Constants.FLAG_2, "你好activity!"));
			}
		});
		//f1传递参数到service
		Button tButton4 = (Button) view.findViewById(R.id.text_f4_tv_id);
		tButton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new EventMsg(Constants.FLAG_3, "来自fragment的停止命令!"));
			}
		});
		
		/**
		 * fragment.startActivityForResult
		 */
		Button tButton5 = (Button) view.findViewById(R.id.text_f5_tv_id);
		tButton5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				EventBus.getDefault().post(new EventMsg(Constants.FLAG_3, "来自fragment的停止命令!"));
				mFragment.startActivityForResult(new Intent(mActivity,SecondActivity.class), 0x111);
			}
		});
	}
	
	/**
	 * 可以在这个回调方法中回去到父Activity的内部对象，如view
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Button entryBtn = (Button) mActivity.findViewById(R.id.entry_anim_btn_id);
		
		entryBtn.setText("EntryClick");
//        entryBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(mActivity, "按钮被击中了！", 0).show();
//			}
//		});
		
		System.out.println("生命周期：onActivityCreated");
	}
	
	/**
	 * 利用fragment 2 fragment ，fragment内部处理事务提交
	 */
	private void self2Fragment(){
		Bundle bundle = getArguments();
		int rootViewId = 0;
		if (bundle != null) {
			rootViewId = bundle.getInt(ROOT_LAYOUT_ID, 0);
		}
		if (rootViewId == 0) {
			throw new IllegalArgumentException("请在实例化F1对象时将事务提交到的布局Id传入其中!");
		}
		
		mActivity.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).hide(this).add(rootViewId,F2.getInstance(rootViewId)).addToBackStack(null).commit();
	}

	@Override
	protected int setFragmentRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return R.layout.test_f1_layout;
	}

	/**
	 * 保存额外要恢复的数据（重建）
	 */
	@Override
	protected void handleSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 恢复保存的数据（重建）
	 */
	@Override
	protected void handleRestoreSaveInstanceState(Bundle savedInstanceState) {
		
	}
	
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveFromActiviytMsg(EventMsg eventMsg){
		if (eventMsg != null && eventMsg.tag ==  Constants.FLAG_1) {
			Toast.makeText(mActivity, "F1 收到来自于Activity信息,内容为："+ eventMsg.object.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Activity告知Service停止服务运行
	 */
	public void stopService(){
		EventBus.getDefault().post(new EventMsg(Constants.FLAG_3, "来自Fragment-停止运行吧！"));
	}
	
	@Subscribe(threadMode=ThreadMode.MainThread)
	public void receiveMsgFromService(EventMsg msg){
		if (msg!=null && msg.tag == Constants.FLAG_5) {
			Toast.makeText(mActivity, "接收来自于Service的信息："+ msg.object.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("F1-------------------onActivityResult");
		if (requestCode == 0x111 && resultCode == 0x111) {
			System.out.println("F1获取的参数=>" + data != null? data.getStringExtra("key"):"");
		}
	}

}
