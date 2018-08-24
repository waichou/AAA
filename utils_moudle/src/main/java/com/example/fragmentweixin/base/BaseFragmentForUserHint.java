package com.example.fragmentweixin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by dasu on 2016/9/27.
 *
 * Fragment基类，封装了懒加载的实现
 *
 * 1、Viewpager + Fragment情况下，fragment的生命周期因Viewpager的缓存机制而失去了具体意义
 * 该抽象类自定义一个新的回调方法，当fragment可见状态改变时会触发的回调方法
 *
 * @see #onFragmentVisibleChange(boolean)
 * @see #onFragmentFirstVisible()
 * 
 * 
 * 这个类主要要控制完成几点：
 * 第一：当前显示的fragment，则直接回调加载数据，下次再进入时则不再执行数据加载
 * 第二：预加载的fragment，则仅加载要显示的view，下次切换到它的时候，则直接在setUserVisibleHint方法中加载数据的回调，保证下次再进来时仅加载一次数据
 * 小结：每个fragment控制了仅加载一次数据的机会，但是onFragmentVisibleChange(boolean)这个方法每次在切换fragment过程都是执行的
 */
public abstract class BaseFragmentForUserHint extends Fragment {

    private static final String TAG = BaseFragmentForUserHint.class.getSimpleName();

    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    private View rootView;//这个rootView的目的就是为了控制new Fragment的时候也会触发setUserVisibleHint拦截到了。
                          //主要是为了预加载的情况.因预加载的fragment也会把自己的view实例化出来，故下次切到它身上的时候，直接在setUserVisibleHint方法上做处理即可。
                          //而对于默认第一次加载显示的fragment则正常在onViewCreate的时候处理加载数据即可
                          //综上：默认加载的在onViewCreate方法执行加载数据，预加载的都在setUserVisible中加载数据！


    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用,即：在new Fragment()时也会被回调
        if (rootView == null) {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {//fragment可见
                if (isFirstVisible) {//默认加载的fragment执行此条件
                    onFragmentFirstVisible();//回调加载数据
                    isFirstVisible = false;
                }
                onFragmentVisibleChange(true);//回调状态可见
                isFragmentVisible = true;//fragment可见
            }
        }
        super.onViewCreated(isReuseView && rootView != null ? rootView : view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initVariable();
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
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
}
