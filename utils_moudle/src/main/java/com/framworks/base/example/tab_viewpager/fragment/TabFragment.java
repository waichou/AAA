package com.framworks.base.example.tab_viewpager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.framworks.base.BaseFragment;
import com.utils_moudle.R;

/**
 * Created by zhouwei on 2018/7/30.
 */

public class TabFragment extends BaseFragment {
    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.example_tab_fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(View view) {

    }

    public static Fragment newInstance() {
        TabFragment  tabfragment = new TabFragment();
        return tabfragment;
    }
}
