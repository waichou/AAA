package com.framworks.base.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.framworks.base.BaseFragment;
import com.framworks.base.main.fragments.childfragment.FirstTabFragment;
import com.framworks.base.main.view.BottomBar;
import com.framworks.base.main.view.BottomBarTab;
import com.utils_moudle.R;

public class MainFragment extends BaseFragment {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;

    private BaseFragment[] mFragments = new BaseFragment[3];

    private BottomBar mBottomBar;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.entry_fragment_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_launcher, "文本1"))
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_launcher, "文本2"))
                .addItem(new BottomBarTab(mActivity, R.drawable.ic_launcher, "文本3"));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {//当前选中tab的情况
                FragmentUtils.showHide(mFragments[position], mFragments[prePosition]);
                BottomBarTab tab = mBottomBar.getItem(FIRST);
            }

            @Override
            public void onTabUnselected(int position) {//上次tab执行的的情况
            }

            @Override
            public void onTabReselected(int position) {//上次tab和本次tab选择一致情况
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                //EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseFragment firstFragment = (BaseFragment) FragmentUtils.findFragment(getChildFragmentManager(),FirstTabFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstTabFragment.newInstance();
            mFragments[SECOND] = FirstTabFragment.newInstance();
            mFragments[THIRD] = FirstTabFragment.newInstance();

            FragmentUtils.add(getChildFragmentManager(),mFragments,R.id.fl_tab_container,0);//无需添加到回退栈中，如果添加了则会空白显示MainFragment

        } else {
            //重建恢复
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = (BaseFragment) FragmentUtils.findFragment(getChildFragmentManager(),FirstTabFragment.class);
            mFragments[THIRD] = (BaseFragment) FragmentUtils.findFragment(getChildFragmentManager(),FirstTabFragment.class);
        }
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(View view) {

    }
}
