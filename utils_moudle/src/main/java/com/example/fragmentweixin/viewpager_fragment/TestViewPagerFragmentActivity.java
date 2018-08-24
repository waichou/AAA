package com.example.fragmentweixin.viewpager_fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.example.fragmentweixin.base.BaseActivity;
import com.example.fragmentweixin.viewpager_fragment.fragments.New2Fragment;
import com.example.fragmentweixin.viewpager_fragment.fragments.New3Fragment;
import com.example.fragmentweixin.viewpager_fragment.fragments.New4Fragment;
import com.example.fragmentweixin.viewpager_fragment.fragments.NewFragment;
import com.utils_moudle.R;

public class TestViewPagerFragmentActivity extends BaseActivity {

	@Bind(R.id.viewpager_id)
	ViewPager mViewPager;

	private List<Fragment> mFragmentList;
	private String[] mTitles = new String[] { "社会", "体育", "科技", "世界" };
	private NewsPagerAdapter mAdapter;

	@Override
	protected int setContentRootView() {
		return R.layout.main_viewpager_fragment;
	}

	@Override
	protected void initViews() {
		// 数据源已经集成到adapter，setAdapter之后便会获取对应的fragment实例显示
		mAdapter = new NewsPagerAdapter(mFragmentManager);// 注意：因为此ViewPager布局是嵌套在Fragment中的，故传递getChildFragmnetManager对象
		mViewPager.setAdapter(mAdapter);
		//设定viewpager的显示指定的itemview，下标是从0开始计算的。即：设置第二张显示，则传入1 
		mViewPager.setCurrentItem(2);
	}

	@Override
	protected void initDatas() {
		mFragmentList = new ArrayList<>();
	    mFragmentList.clear();
	    for(int i = 0;i<mTitles.length;i++){
            Bundle bundle = new Bundle();
            bundle.putString("type","第"+(i+1)+"页");
            if (i == 0) {
            	mFragmentList.add(NewFragment.NewInstance(bundle));
			}else if (i == 1) {
				mFragmentList.add(New2Fragment.NewInstance(bundle));
			}else if (i == 2) {
				mFragmentList.add(New3Fragment.NewInstance(bundle));
			}else if (i == 3) {
				mFragmentList.add(New4Fragment.NewInstance(bundle));
			}
        }
	}
	
	@Override
	public void registMouble() {
		ButterKnife.bind(this);
	}

	@Override
	public void unRegistMouble() {
		ButterKnife.unbind(this);
	}

	private class NewsPagerAdapter extends FragmentStatePagerAdapter {

		public NewsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mTitles.length;
		}

		public CharSequence getPageTitle(int position) {
			return mTitles[position];
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
		}
	}

}
