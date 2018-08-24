package com.example.fragmentweixin.testfragment.interfaces;

public interface IFragmentListener {

	/**
	 * 定义此方法是为了Fragment内部持有接口对象更新释放Activity的事件分发能力
	 * @param isDispatch
	 */
	public void setIsDispatchBoo(boolean isDispatch);
	
	/**
	 * 定义此方法用于控制，如果在fragment之间切换过程中，存在切换动画，则弹栈需要在动画结束时间后释放activity的事件分发
	 * @param time
	 */
	public void setCostAnimDuration(long time);
	public void setLastCostAnimDuration(long time);
	
	/**
	 * 定义fragment内部跳转另个fragment的回调方法，提给给
	 */
	public void commitFrament2Fragment();
	
}
