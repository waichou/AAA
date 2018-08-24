package com.framworks.base.main.fragments.childfragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.framworks.base.BaseFragment;
import com.framworks.base.main.fragments.MainFragment;
import com.framworks.base.main.fragments.childfragment.adapters.FirstAdapter;
import com.framworks.base.main.fragments.childfragment.beans.FirstBean;
import com.framworks.base.rv.adapter.LGRecycleViewAdapter;
import com.framworks.base.rv.listener.OnItemClickListener;
import com.utils_moudle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class FirstTabFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    private FirstAdapter mAdapter;
    private List<FirstBean> firstBeenList = new ArrayList<>();

    public static FirstTabFragment newInstance() {
        Bundle args = new Bundle();
        FirstTabFragment fragment = new FirstTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.first_fragment_tab;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        mToolbar.setTitle("首页");

        mRefreshLayout.setOnRefreshListener(this);

        mRecy.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecy.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        mRecy.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });

        mAdapter = new FirstAdapter();
        mRecy.setAdapter(mAdapter);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dx=" + dy + ",dy=" + dy);

                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });

        mAdapter.setOnItemClickListener(R.id.first_item_root_layout, new LGRecycleViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                FragmentUtils.add(getParentFragment().getChildFragmentManager(),
                        MsgFragment.newInstance(new FirstBean()),
                        R.id.fl_container,
                        true,
                        R.anim.h_fragment_enter,
                        R.anim.h_fragment_exit,
                        R.anim.h_fragment_pop_enter,
                        R.anim.h_fragment_pop_exit
                        );
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public void doBusiness() {
        String[] name = new String[]{"Jake", "Eric", "Kenny", "Helen", "Carr"};
        String[] chats = new String[]{"message1", "message2", "message3", "message4", "message5"};

        for (int i = 0; i < 15; i++) {
            int index = (int) (Math.random() * 5);
            FirstBean firstBean = new FirstBean();
            firstBean.name = name[index];
            firstBean.message = chats[index];
            firstBeenList.add(firstBean);
        }

        mAdapter.setDataSet(firstBeenList);
    }

    @Override
    public void onWidgetClick(View view) {

    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新数据
                //1.如果有新数据，则更新
                if (firstBeenList != null && firstBeenList.size() > 0) {
                    firstBeenList.clear();

                    String[] name = new String[]{"Jake2", "Eric2", "Kenny2", "Helen2", "Carr2"};
                    String[] chats = new String[]{"message2", "message2", "message2", "message2", "message2"};

                    for (int i = 0; i < 15; i++) {
                        int index = (int) (Math.random() * 5);
                        FirstBean firstBean = new FirstBean();
                        firstBean.name = name[index];
                        firstBean.message = chats[index];
                        firstBeenList.add(firstBean);
                    }
                }

                mAdapter.setDataSet(firstBeenList);
                //2.如果没有新数据，仅停止刷新动画

                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }
}
