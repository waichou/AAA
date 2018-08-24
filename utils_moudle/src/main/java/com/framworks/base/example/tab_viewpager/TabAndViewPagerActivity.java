package com.framworks.base.example.tab_viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framworks.base.BaseActivity;
import com.framworks.base.example.tab_viewpager.adpater.TabPagerFragmentAdapter;
import com.utils_moudle.R;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class TabAndViewPagerActivity extends BaseActivity {
    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mViewPager;

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.example_tab_viewpager;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mToolbar.setTitle("头部标题");

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

        mViewPager.setAdapter(new TabPagerFragmentAdapter(getSupportFragmentManager(),"标签一","标签二"));
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(View view) {

    }
}
