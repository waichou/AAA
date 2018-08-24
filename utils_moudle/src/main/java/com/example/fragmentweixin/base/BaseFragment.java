package com.example.fragmentweixin.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.fragmentweixin.anim.DefaultHorizontalAnimator;
import com.example.fragmentweixin.anim.helper.AnimatorHelper;
import com.example.fragmentweixin.state_recover.intefaces.ISupportFragment;
import com.example.fragmentweixin.state_recover.resultrecord.ResultRecord;
import com.example.fragmentweixin.testfragment.interfaces.IFragmentListener;
import com.utils_moudle.R;

public abstract class BaseFragment extends Fragment implements ISupportFragment{
 
	//##################### ViewPager + Fragment 的预加载+懒加载的处理部分 START #############
 
	protected boolean isUserViewPager = false;//表示当前是否处理viewPager+fragment的场景
	
	private boolean isFragmentVisible;	
    private boolean isReuseView;
    private boolean isFirstVisible;
    protected View cacheRootView;//这个rootView的目的就是为了控制new Fragment的时候也会触发setUserVisibleHint拦截到了。
                          //主要是为了预加载的情况.因预加载的fragment也会把自己的view实例化出来，故下次切到它身上的时候，直接在setUserVisibleHint方法上做处理即可。
                          //而对于默认第一次加载显示的fragment则正常在onViewCreate的时候处理加载数据即可
                          //综上：默认加载的在onViewCreate方法执行加载数据，预加载的都在setUserVisible中加载数据！


    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    
    protected View mRootView;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用,即：在new Fragment()时也会被回调
        if (cacheRootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {//默认fragment进不去 | 预加载的能进去
            onFragmentFirstVisible();//预加载的fragment回调加载数据
            isFirstVisible = false;
        }
        if (isVisibleToUser) {//默认的fragment进不去 | 预加载fragment进入
            onFragmentVisibleChange(true);//预加载的fragment回调更新状态
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {//默认的能进去
            isFragmentVisible = false;
            onFragmentVisibleChange(false);//回调默认framgnet的状态为false不可见
        }
    }
    
    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        cacheRootView = null;
        isReuseView = true;
    }

    /**
    *
    * @param isReuse
    */
    protected void reuseView(boolean isReuse) {
       isReuseView = isReuse;
    }
    
    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     * 可在该回调方法里进行一些ui显示与隐藏
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    /**
     * 在fragment首次可见时回调，可用于加载数据，防止每次进入都重复加载数据
     */
    protected void onFragmentFirstVisible() {

    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }
    
    private void handleDefaultLoadDataForFragment(View view,Bundle savedInstanceState){
    	//如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        if (cacheRootView == null) {
        	cacheRootView = view;
            if (getUserVisibleHint()) {//fragment可见
                if (isFirstVisible) {//默认加载的fragment执行此条件
                    onFragmentFirstVisible();//回调加载数据
                    isFirstVisible = false;
                }
                onFragmentVisibleChange(true);//回调状态可见
                isFragmentVisible = true;//fragment可见
            }
        }
        super.onViewCreated(isReuseView && cacheRootView != null ? cacheRootView : view, savedInstanceState);
    }
    
	//##################### ViewPager + Fragment 的预加载+懒加载的处理部分 END #############
	
	private final String FRAGMENT_HIDDEN_STATE = "isHidden";
	protected static final String IS_ROOT_LOAD = "isRootLoad";
	
	protected boolean isAnimEnable = true;//全局控制是否启用动画
	
	protected FragmentActivity mActivity;
	protected IFragmentListener mFragmentListener; 
	protected Fragment mFragment;
	
	protected AnimatorHelper mAnimatorHelper;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		/**
		 * 此处解决getActivity()方法的调用导致空指针问题：
		 * 与父Activity关联后，持有其引用，避免在onActivity被系统销毁，或当前fragment被Activity移除的情况下，但内部仍在调用getActivity()方法而导致报空指针，
		 */
		mActivity = (FragmentActivity) activity;
		mFragmentListener = (IFragmentListener) activity;
		mFragment = this;
		
		mAnimatorHelper = new AnimatorHelper(mActivity.getApplicationContext(), new DefaultHorizontalAnimator());
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	//预加载变量初始化
    	initVariable();
    	
    	//如果默认第一次加载根Fragment的时候执行退出，不用设置监听
    	if (getArguments().containsKey(IS_ROOT_LOAD)) {
			return;
		}
    	
    	final Animation enter = mAnimatorHelper.enterAnim;
        if (enter == null) return;

        mAnimatorHelper.enterAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            	
            	
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mFragmentListener.setIsDispatchBoo(false);
						System.out.println("跳转到的fragment[时间="+enter.getDuration()+"]，在进入动画结束后释放Activity的事件分发权力！");
					}
				}, enter.getDuration());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                System.out.println("动画执行结束");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    
    /**
	 * 注册组件
	 */
	protected void registMouble() {
	}
	/**
	 * 注销组件
	 */
	protected void unRegistMouble() {
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if (savedInstanceState != null) {
			System.out.println(getClass().getSimpleName() + "重建中...");
			boolean isHidden = savedInstanceState.getBoolean(FRAGMENT_HIDDEN_STATE);
			processRestoreInstanceState(isHidden);
			
			handleRestoreSaveInstanceState(savedInstanceState);
		}
    	int rootViewId = setFragmentRootView(inflater,container,savedInstanceState);
    	if (rootViewId < 0) {
			throw new IllegalArgumentException("fragment' rootViewId is not right!");
		}
    	
    	mRootView = inflater.inflate(rootViewId, container,false);
    	//注册
    	registMouble();
    	return mRootView;
    }
    
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	if (isUserViewPager) {
    		handleDefaultLoadDataForFragment(view, savedInstanceState);
    		return;
		}
    	super.onViewCreated(view, savedInstanceState);
    }
	
    protected abstract int setFragmentRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    
    protected void handleSaveInstanceState(Bundle outState){}
    
	/**
     * fragment重建的情况下，获取当前fragment的显隐状态值，控制显隐
     */
    protected void processRestoreInstanceState(boolean isHidden) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        if (isHidden) {
            ft.hide(this);
        } else {
            ft.show(this);
        }
        ft.commitAllowingStateLoss();
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		boolean isHidden = isHidden();
		System.out.println("当前"+getClass().getSimpleName()+"的显隐状态为：" + isHidden);
		outState.putBoolean(FRAGMENT_HIDDEN_STATE,isHidden);
		handleSaveInstanceState(outState);
	}
    
    protected void handleRestoreSaveInstanceState(Bundle savedInstanceState){}
    
    
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
 
    	/* 启用全局fragment动画  */
    	if (isAnimEnable) {
    		return handleCustomAnimation(transit,enter,nextAnim);
		}
    	//禁用全局fragment动画,即：采用默认父类方法 | 使用自定义无动画效果
//    	return super.onCreateAnimation(transit, enter, nextAnim);
    	return AnimationUtils.loadAnimation(mActivity, R.anim.no_anim);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //如果不启用动画切换fragment，则在onActivityCreate方法中处理恢复Activity的事件分发的能力
        if (!isAnimEnable) {
        	mFragmentListener.setIsDispatchBoo(false);
		}
    }
    
    /**
     * 设定fragment的切换动画
     */
    protected Animation handleCustomAnimation(int transit, boolean enter, int nextAnim){
    	System.out.println("当前fragment[" + this.getClass().getSimpleName() +"]实例执行动画");
    	if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {//hoemfragment先执行打开动画操作
            if (enter) {
                Animation enterAnim;
                if (getArguments().getBoolean(IS_ROOT_LOAD,false)) {//默认加载根homefragment或者叫第一个fragment的时候加载“无动画”资源
                    enterAnim = mAnimatorHelper.getNoneAnim();
                } else {//
                    enterAnim = mAnimatorHelper.enterAnim;//目标进入动画
                }
                return enterAnim;
            } else {
                return mAnimatorHelper.popExitAnim;//源退出
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
        	if (enter) {//此处判断是为了在弹栈后，释放父Activity的延迟时间！
        		if (mFragmentListener != null) {
					mFragmentListener.setLastCostAnimDuration(mAnimatorHelper.popEnterAnim.getDuration());
				}
			}else {
				if (mFragmentListener != null) {
					mFragmentListener.setCostAnimDuration(mAnimatorHelper.exitAnim.getDuration());
				}
			}
            return enter ? mAnimatorHelper.popEnterAnim /*源进入*/: mAnimatorHelper.exitAnim/*目标退出*/;
        } 
    	return null;
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	unRegistMouble();
    	
    	initVariable();
    }
    
    /**
	 * 此方法是在目标fragment退出的时候，从bundle中获取到源fragment的引用对象，调用此方法回调
	 */
	protected void handleResultRecord(Fragment target,String key,String fTag) {
        try {
            Bundle args = target.getArguments();
            if (args == null) return;
            final ResultRecord resultRecord = args.getParcelable(key);
            if (resultRecord == null) return;

            final ISupportFragment preFragment = (ISupportFragment) target.getFragmentManager().getFragment(target.getArguments(),fTag);//注意此处
            new Handler(mActivity.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    preFragment.onFragmentResult(resultRecord.requestCode, resultRecord.resultCode, resultRecord.resultBundle);
                }
            });
        } catch (IllegalStateException ignored) {
            // Fragment no longer exists
        }
    }

	
	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		//空方法，子类愿意实现就是实现，不实现就拉倒！
	}
}
