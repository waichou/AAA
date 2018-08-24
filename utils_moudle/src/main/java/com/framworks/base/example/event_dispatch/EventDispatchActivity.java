package com.framworks.base.example.event_dispatch;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.utils_moudle.R;

import butterknife.Bind;

public class EventDispatchActivity extends AppCompatActivity {

    @Bind(R.id.viewpager_id)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dispatch);
    }
}
