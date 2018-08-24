package com.example.fragmentweixin.testfragment.activity2;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.utils_moudle.R;

public class SecondActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.second_2_activity_layout);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.second_root_layout_id, SecondFragment.getInstance()).commit();
	}
	
}
