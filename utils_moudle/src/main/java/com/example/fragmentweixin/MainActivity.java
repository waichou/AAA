package com.example.fragmentweixin;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentweixin.base.BaseActivity;
import com.utils_moudle.R;

/**
 * @author Wei.zhou
 * @date 2018年6月30日
 * @time 下午3:18:04
 * @desc 单个Activity + 多个Fragment
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private final String CURRENT_PAPE_INDEX = "curPageIndex";
	private final String CURRENT_PAPE_FRAGMENT = "curPageFragmentTag";
	private String mCurrentFragmentTag = "";
	
	private View weixinLayout, tongxunluLayout, faxianLayout, woLayout;
	private TextView weixinTv, tongxunluTv, faxianTv, woTv;
	private ImageView weixinIv, tongxunluIv, faxianIv, woIv;
	private Fragment1 fragment1;
	private Fragment2 fragment2;
	private Fragment3 fragment3;
	private Fragment4 fragment4;
	
	private int mCurPageIndex = 1;//标记当前显示的fragment页面

	@Override
	protected int setContentRootView() {
		return R.layout.activity_main;
	}
	
	@Override
	protected void initViews() {
		// 注册各IamgeView
		weixinIv = (ImageView) findViewById(R.id.weixin_iv);
		tongxunluIv = (ImageView) findViewById(R.id.tongxunlu_iv);
		faxianIv = (ImageView) findViewById(R.id.faxian_iv);
		woIv = (ImageView) findViewById(R.id.wo_iv);

		// 注册各TextView
		weixinTv = (TextView) findViewById(R.id.weixin_tv);
		tongxunluTv = (TextView) findViewById(R.id.tongxunlu_tv);
		faxianTv = (TextView) findViewById(R.id.faxian_tv);
		woTv = (TextView) findViewById(R.id.wo_tv);

		// 注册各Layout
		weixinLayout = (View) findViewById(R.id.weixin_layout);
		tongxunluLayout = (View) findViewById(R.id.tongxunlu_layout);
		faxianLayout = (View) findViewById(R.id.faxian_layout);
		woLayout = (View) findViewById(R.id.wo_layout);

		// 各Layout注册监听器
		weixinLayout.setOnClickListener(this);
		tongxunluLayout.setOnClickListener(this);
		faxianLayout.setOnClickListener(this);
		woLayout.setOnClickListener(this);

		// 初识状态是显示微信
		weixinIv.setBackgroundResource(R.drawable.weixin2);
		weixinTv.setTextColor(getResources().getColor(R.color.green));
	}
	
	@Override
	protected void handleRestoreInstanceState(Bundle savedInstanceState) {
		/**
		 * fragment的重叠问题的说明：在单一activity + 多个fragment的情况下，使用add，hide，show的方式来控制fragment页面的加载与
		 * 显示，但是系统在内存紧张的情况，可能会造成当前应用的activity与fragment的实例回收，待到用户重新回到应用程序前台时，
		 * 会重建恢复回收之前的数据状态。Activity在执行onSaveInstanceState方法的时候，会保存fragment的实例，以及自身的实例，
		 * 重建过程中，由于fragment没有维护自身的之前的显隐状态，但是在最终重建的时候，会发生fragment重叠现象。
		 * 
		 * Activity要做的处理：在oncreate的方法中，做好fragment的空判断处理，如果当前默认加载的fragment没有实例，则创建自身实例，继续事务提交。
		 *                   即：保证重建的时候，不重复提交事务！
		 * Fragment要做的处理：在执行onSaveInstanceState方法时候，把fragment的显隐状态缓存起来。在重建fragment的时候，取出做事务显隐处理。
		 * 
		 * 由于在重建的时候，系统会自动创建已经销毁的fragment实例（即：会执行onDestroyView onDestroy onDetach） + Activity实例（即：会执行onDestroy）
		 */ 
		
		//activity要做的事情：
		if (savedInstanceState == null) {//默认加载首页fragment1
			fragment1 = (Fragment1) mFragmentManager.findFragmentByTag("f1");
			if (fragment1 == null) {
				fragment1 = Fragment1.getInstance("Fragment1参数为：微信");
				mFragmentManager.beginTransaction().add(R.id.content, fragment1,"f1").commit();
			}
		}else {//重建activity
			//重置底部菜单按钮的背景状态
			mCurPageIndex = savedInstanceState.getInt(CURRENT_PAPE_INDEX);
			clearState(mCurPageIndex);
			//通过事务获取到所有的fragment栈试图中fragment实例
			List<Fragment> fragments = mFragmentManager.getFragments();
			for (int i = 0; i < fragments.size(); i++) {
				Fragment fragment = fragments.get(i);
				if (fragment instanceof Fragment1) {
					fragment1 = (Fragment1) fragment;
				}else if (fragment instanceof Fragment2) {
					fragment2 = (Fragment2) fragment;
				}else if (fragment instanceof Fragment3) {
					fragment3 = (Fragment3) fragment;
				}else if (fragment instanceof Fragment4) {
					fragment4 = (Fragment4) fragment;
				}
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);//该super方法的内部已经保存了Fragment的实例
		
		outState.putInt(CURRENT_PAPE_INDEX, mCurPageIndex);
		outState.putString(CURRENT_PAPE_FRAGMENT, mCurrentFragmentTag);
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		FrameLayout contentLayout = (FrameLayout) findViewById(android.R.id.content);
		Button clickButton = new Button(this);
		clickButton.setText("click");
		clickButton.setTextSize(10);
		clickButton.setBackgroundColor(Color.RED);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200,100);
		params.topMargin = 150;
		params.rightMargin = 50;
		params.gravity = Gravity.END;
		
		clickButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "这是针对Fragment的实例！",Toast.LENGTH_SHORT).show();
				
				mFragmentManager.getBackStackEntryCount();
			}
		});
		
		clickButton.setLayoutParams(params);
		
		contentLayout.addView(clickButton);
	}
	

	@Override
	public void onClick(View arg0) {
		// 当发生点击时，先清除状态，这里的状态指的是布局里面的图片和文字
		clearState(-1);
		switch (arg0.getId()) {
		case R.id.weixin_layout:
			// 如果点的是微信，将微信布局的图片和文字的颜色变为绿色
			weixinIv.setBackgroundResource(R.drawable.weixin2);
			weixinTv.setTextColor(getResources().getColor(R.color.green));
			// 显示微信的fragment
			showFragment(1);
			break;
		case R.id.tongxunlu_layout:
			tongxunluIv.setBackgroundResource(R.drawable.tongxunlu2);
			tongxunluTv.setTextColor(getResources().getColor(R.color.green));
			showFragment(2);
			break;
		case R.id.faxian_layout:
			faxianIv.setBackgroundResource(R.drawable.faxian2);
			faxianTv.setTextColor(getResources().getColor(R.color.green));
			showFragment(3);
			break;
		case R.id.wo_layout:
			woIv.setBackgroundResource(R.drawable.wo2);
			woTv.setTextColor(getResources().getColor(R.color.green));
			showFragment(4);
			break;
		}
	}

	public void clearState(int showImage) {
		// 未选中时的图片
		weixinIv.setBackgroundResource(R.drawable.weixin1);
		tongxunluIv.setBackgroundResource(R.drawable.tongxunlu1);
		faxianIv.setBackgroundResource(R.drawable.faxian1);
		woIv.setBackgroundResource(R.drawable.wo1);
		// 未选中时字体颜色
		weixinTv.setTextColor(getResources().getColor(R.color.black));
		tongxunluTv.setTextColor(getResources().getColor(R.color.black));
		faxianTv.setTextColor(getResources().getColor(R.color.black));
		woTv.setTextColor(getResources().getColor(R.color.black));
		
		if (showImage == -1) {
			return;
		}
		switch (showImage) {
		case 1:
			// 如果点的是微信，将微信布局的图片和文字的颜色变为绿色
			weixinIv.setBackgroundResource(R.drawable.weixin2);
			weixinTv.setTextColor(getResources().getColor(R.color.green));
			// 显示微信的fragment
			break;
		case 2:
			tongxunluIv.setBackgroundResource(R.drawable.tongxunlu2);
			tongxunluTv.setTextColor(getResources().getColor(R.color.green));
			break;
		case 3:
			faxianIv.setBackgroundResource(R.drawable.faxian2);
			faxianTv.setTextColor(getResources().getColor(R.color.green));
			break;
		case 4:
			woIv.setBackgroundResource(R.drawable.wo2);
			woTv.setTextColor(getResources().getColor(R.color.green));
			break;
		}
	}

	/**
	 * 多次切换到自己不做处理，切换到其他，则隐藏Hide其他Fragment，show自己
	 * @param index
	 */
	public void showFragment(int index) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		
		//显示点击当前按钮时不再触发事务提交
		if (index == mCurPageIndex) {
			System.out.println("当前界面被重建时，不重复提交！");
			if (index == 1) {//第一个界面刷新
				fragment1.updateDataList();
			}
			return ;
		}
		
		switch (index) {
		case 1:
			// 如果fragment1已经存在则将其显示出来
			fragment1 = (Fragment1) mFragmentManager.findFragmentByTag("f1");
			if (fragment1 != null){//从其他界面切换回来的时候执行
				System.out.println("fragement1 重建时直接show...");
				ft.show(fragment1);
			}
			// 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
			else {//第一次执行此处
				fragment1 = new Fragment1();
				mFragmentManager.beginTransaction().add(R.id.content, fragment1,"f1").commit();
			}
			break;
		case 2:
			fragment2 = (Fragment2) mFragmentManager.findFragmentByTag("f2");
			if (fragment2 != null){
				ft.show(fragment2);
			}
			else {
				fragment2 = new Fragment2();
				mFragmentManager.beginTransaction().add(R.id.content, fragment2,"f2").commit();
			}
			break;
		case 3:
			if (fragment3 != null){
				ft.show(fragment3);
			}
			else {
				fragment3 = new Fragment3();
				ft.add(R.id.content, fragment3);
			}
			break;
		case 4:
			if (fragment4 != null){
				ft.show(fragment4);
			}
			else {
				fragment4 = new Fragment4();
				ft.add(R.id.content, fragment4);
			}
			break;
		}
		
		hideFragments(ft, index);
		
		mCurPageIndex = index;
		mCurrentFragmentTag = index==1?"f1":index==2?"f2":index==3?"f3":"f4";
		
		supportCommit(mFragmentManager, ft);
	}

	// 当fragment已被实例化，相当于发生过切换，就隐藏起来
	public void hideFragments(FragmentTransaction ft,int index) {
		switch (index) {
			case 1:
				if (fragment2 != null)
					ft.hide(fragment2);
				if (fragment3 != null)
					ft.hide(fragment3);
				if (fragment4 != null)
					ft.hide(fragment4);
				break;
			case 2:
				if (fragment1 != null)
					ft.hide(fragment1);
				if (fragment3 != null)
					ft.hide(fragment3);
				if (fragment4 != null)
					ft.hide(fragment4);
				break;
			case 3:
				if (fragment1 != null)
					ft.hide(fragment1);
				if (fragment2 != null)
					ft.hide(fragment2);
				if (fragment4 != null)
					ft.hide(fragment4);
				break;
			case 4:
				if (fragment1 != null)
					ft.hide(fragment1);
				if (fragment2 != null)
					ft.hide(fragment2);
				if (fragment3 != null)
					ft.hide(fragment3);
				break;
		}
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("activity ondestroy");
	}

	@Override
	public void commitFrament2Fragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registMouble() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unRegistMouble() {
		// TODO Auto-generated method stub
		
	}
	
}
