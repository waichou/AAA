package com.example.fragmentweixin.testfragment.activity2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.fragmentweixin.testfragment.constants.Constants;
import com.example.fragmentweixin.testfragment.eventbus.EventMsg;
import com.utils_moudle.R;

import de.greenrobot.event.EventBus;

public class SecondFragment extends Fragment {

	
	public static SecondFragment getInstance(){
		SecondFragment secondFragment = new SecondFragment();
		return secondFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.second_fragment_layout, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		view.findViewById(R.id.send_msg_2_preActiviy_id).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new EventMsg(Constants.FLAG_6,"来自于其他SecondActivity中的SecondFragment传来的数据！"));
			}
		});
		view.findViewById(R.id.send_msg_2_preActiviy_fragment_id).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnDataIntent = new Intent();
				returnDataIntent.putExtra("key", "hello data!");
				getActivity().setResult(0x111, returnDataIntent);
				getActivity().finish();
			}
		});
	}
}
