package com.example.fragmentweixin.testfragment.eventbus;

/**
 *
 * @author Wei.zhou
 * @date 2018年7月7日
 * @time 上午11:52:38
 * @desc 定义EventBus事件类
 */
public class EventMsg {

	public int tag;
	public Object object;
	public EventMsg(int tag,Object object){
		this.tag = tag;
		this.object = object;
	}
}
