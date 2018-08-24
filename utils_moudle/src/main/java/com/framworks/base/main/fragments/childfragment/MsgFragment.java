package com.framworks.base.main.fragments.childfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.framworks.base.BaseFragment;
import com.framworks.base.main.fragments.childfragment.adapters.MsgAdapter;
import com.framworks.base.main.fragments.childfragment.beans.FirstBean;
import com.framworks.base.main.fragments.childfragment.beans.Msg;
import com.utils_moudle.R;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class MsgFragment extends BaseFragment {
    private static final String ARG_MSG = "arg_msg";

    private Toolbar mToolbar;
    private RecyclerView mRecy;
    private EditText mEtSend;
    private Button mBtnSend;

    private FirstBean mChat;
    private MsgAdapter mAdapter;

    public static MsgFragment newInstance(FirstBean msg) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        MsgFragment fragment = new MsgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.first_2_second_list;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {
        KeyboardUtils.hideSoftInput(mActivity);
        mChat = getArguments().getParcelable(ARG_MSG);
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mBtnSend = (Button) view.findViewById(R.id.btn_send);
        mEtSend = (EditText) view.findViewById(R.id.et_send);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        mToolbar.setTitle(mChat.name);
        initToolbarNav(mToolbar);

        mRecy.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecy.setHasFixedSize(true);
        mAdapter = new MsgAdapter();
        mRecy.setAdapter(mAdapter);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEtSend.getText().toString().trim();
                if (TextUtils.isEmpty(str)) return;

                mAdapter.addMsg(new Msg(str,(Math.random()* 100 % 2 == 0) ? true:false));
                mEtSend.setText("");
                mRecy.scrollToPosition(mAdapter.getItemCount() - 1);

                KeyboardUtils.hideSoftInput(mActivity);
            }
        });
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_launcher);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        });
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onWidgetClick(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy = null;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
}
