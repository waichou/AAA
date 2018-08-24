package com.framworks.base.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.FragmentUtils;
import com.framworks.base.BaseActivity;
import com.framworks.base.main.fragments.MainFragment;
import com.utils_moudle.R;


/**
 * 仿微信交互方式Demoo
 * Created by YoKeyword on 16/6/30.
 */
public class MainEntryActivity extends BaseActivity {

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.main_entry_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (FragmentUtils.findFragment(getSupportFragmentManager(),MainFragment.class) == null){
//            FragmentUtils.add(getSupportFragmentManager(),MainFragment.newInstance(), R.id.fl_container);
        }


//        //第一检索为null
//        if (findFragment(MainFragment.class) == null) {
//            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
//        }

    }

    @Override
    public void doBusiness() {
    }

    @Override
    public void onWidgetClick(View view) {

    }
}
