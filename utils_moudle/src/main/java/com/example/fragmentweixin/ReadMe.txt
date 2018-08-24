1.解决fragment的重叠问题
  Fragment自身来维护当前显隐状态，重建时可正常恢复显示

2.fragment之间的切换动画效果问题
  2.1 采用setCustomAnimation方法
  2.2 采用onCreateAnimation方法
  2.3 为了控制在动画切换过程中导致触发事件，利用父Activity的事件分发方式进行控制
            处理细节：应保证两者fragment的动画应采用时间最长的那个延迟时间，延迟时间作为释放父Activity的事件分发！
  2.4 如果两者fragment处于不同的布局上显示，则没有交互的动画可言。
  2.5 <1>默认动画效果
      //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
      
	  <2>自定义动画效果setCustomAnimations	
		/* 前者一个负责进入，一个负责退出 */
		//第一个参数：要入栈的fragment进入动画（指的是目标fragment）
		//第二个参数：要隐藏的fragment退出动画（指的是原fragment）
		
		/* 前者一个负责进入，一个负责退出 */
		//第三个参数：要恢复的原fragment进入动画（指的是原fragment）
		//第四个参数：要退出的目标fragment退出动画（指的是目标fragment）
		
3.fragment的弹栈和入栈问题:
  3.1维护回退栈时调用此方法: mFragmentManager.popBackStackImmediate();
  3.2不维护回退栈时调用下面的方法:mFragmentManager.beginTransaction().remove(f2).commit(); -->"remove"!

4.事务监听器：
 
5.fragment跳转到fragment过程中防抖动：
    处理思路:针对两种场景：
  <1>跳转过程中有动画执行：
         都交由Activity触发跳转fragment为前提:
         
          试验一【不可行】：fragment自身控制根布局的不可点击
          如果可行执行步骤为：
     1.在onViewCreate方法中控制rootview不可用
     2.onCreateAnimation方法中控制动画执行完毕后，使rootView可用（再次方法中控制动画执行完毕后使rootView可用）
     
          试验二【成功】：
          采用的方式：在fragment在切换过程中，即事务commit提交代码前一行，控制父Activity拦截事件分发，组织点击事件分发到子层的思路，
          一旦存在切换动画，则应该在动画结束后是否父Activity不在拦截，使其正常事件分发。
          如果没有动画，则直接在onActivityCreate方法中回调方法中解除父Activity事件拦截。
     
     #基于试验二：激发事务跳转的2种方式
     <1>一种事务提交是由父Activity中触发跳转（即：Activity->fragment）
     <2>另一种事务提交是由fragment直接负责跳转到其他fragment(即：fragment->fragment)
     
     
  <2>跳转过程中无动画执行：在activity实例化完毕之后的回调方法：onActivityCreated方法做释放Activity的事件分发能力！
      
  
6.技术问题小结：
     6.1 activity的click事件的定义方式常用两种：第一种：setOnclickListener 第二种：public void XxX(View v) -> 布局定义onClick 
                  第一种方式的优先级大于第二种优先级
     6.2 fragment不能直接定义onClick事件，仅能通过获取view+setOnclickListener方法
                
7.#####A方调用B方的方法，必然A持有B方法引用#### --->>接口回调原理就是如此
          对于：如果fragment需要调用activity的内部成员，则可以定义接口，activity实现此接口，在fragment在onAttach方法中转换出接口引用即可。 
          注：此接口可以定义在fragment内部或者外部都可以。

8.同父Activity中的fragment传值另个fragment：【fragment调用startActivityForResult，回调onActivityResult】
   8.1 场景：Activity1中包含Fragment1，通过Fragment1跳转到Activity2中的Fragment2，并传递消息给fragment2，再通过fragment2返回数据给Activity1中的Fragment1
               第一步，在fragment1中调用fragment1.startActivityResult()
               第二步，在fragment2中调用getActivity.setResult() + getActivity().finish();
 
              注意：如果重写了activity中的onActivityResult方法，下面如下：
              如果注释掉了super.onActivityResult(arg0, arg1, arg2); 这句话则不会正常将数据分发给子Fragment的onActivityResult方法中的！！！！

       @Override//父Activity重写方法，注意不要注释掉super这句话
	   protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		   super.onActivityResult(arg0, arg1, arg2);
		   System.out.println("FActivity------------------------>");
		   System.out.println("父Activity获取的参数=>" + arg2 != null? arg2.getStringExtra("key"):"");
	   }
    
9.Activity与Fragment && Fragment与Fragment之间 的数据交换：
   9.1 如果触发点是Fragment时，Activity实现定义的接口对象，接着Fragment获取此接口的引用&调用即可交换数据。
              注意：<1>此接口不能implements在Activity中，但可以定义！ 
	              如下：
	       f1.setSwapListener(new TestFragmentAnim.ISwapDataListener() {
				
				@Override
				public String swap2Fragment(String data) {
					// TODO Auto-generated method stub
					return data + " 之 success！" ;
				}
			});
            <2>接口定义位置：fragment内部 | activity内部 | 公有位置
            
   9.2  如果触发点是Activity时： 通过fragment-tag获取到fragment栈中的对应的fragment实例对象，进而调用该fragment对象
                的公有方法，这种方式产生了耦合。
        [修整思路]：定义fragment接口并定义业务逻辑方法，fragment去实现此接口，再通过fragment-tag从栈中获取到fragment对象
                                      结合向上转型的方式得到接口对象，
		                      即：IFragmentListener listener = (IFragmentListener)findFragmentByTag("f1");
		                      最后父Activity调用listener.业务方法(参数);即可进行数据交换！
		                    【同下面9.3第二种方式原理相同】
		            
   9.3 统计fragment与fragment数据交换：
              第一种间接操作方法：通过9.1 + 9.2的结合方式间接传递数据：即9.1接口回调+9.2向上转型
              
             【个人推荐使用】第二种：前期利用setArgument方式初始化传递参数。后期采用目标Fragement定义接口并通过FragmentManager利用tag获取指定的目标对象， 结合向上转型调取方法进行交互。
              即：（前期）manager.add(R.id.content,SecondFragement.getInstance("参数"),"sTag").commit();
                     （后期)<1> interface ISInterface{
                        public String printStr(String params);    
                     }
                 
                 <2> class SecondFragment implements ISInteface{
                        public String printStr(String params){
                        	
                        }
                     }
                 <3>ISInterface isinterface = manager.findFragmentByTag("sTag");
                    String callBackResult = isinterface.printStr("xxx");
                 
             
10.EventBus使用场景：参考：https://blog.csdn.net/u013790519/article/details/49181857
   10.1 Activity与Fragment的数据交换：
       10.1.1 Activiyt->Fragment:
       10.1.2 Fragment->Activity:
       
   10.2 Fragment与Fragment的数据交换：
  
   10.3 Activity与Activity的数据交换：
   
   10.4 A，B代表Activity；A和B中都包含有Fragment：B中 Fragment 传递给 A Fragment 可行！
   
   10.5 同样适用于Service服务
   [注意]
   1.报错：Caused by: de.greenrobot.event.EventBusException: its super classes have no public methods with the @Subscribe annotation
      原因：经排查发现，定义接收传递的数据的方法参数写的不是唯一的导致的。
      解决：保证接收方法要定义在注册的组件中（Activity，Fragment，Service），再次强调，注册的组件中需要保证存在接收方法，
                 且接收方法要符合要求（即：方法是public，且要有@Subscribe），从而避免报错！
   
   2.小结：在Activity|Fragment|Service的onCreate方法中注册，onDestroy注销EventBus
          
   
11.Fragment相关的管理事务相关的API说明：
   https://www.cnblogs.com/getherBlog/p/3946449.html

   <1>//获取Fragment栈中所有的Fragment实例,包含回退栈的fragment实例
      List<Fragment> fragments = mFragmentManager.getFragments();
      
   <2>//fragment回退栈中的fragment实例，仅计算出回退栈中的fragment实例数量
      int fragmentCount = mFragmentManager.getBackStackEntryCount();
	 
		

12.BackStackEntry的作用:通过内部的getName方法获取到addBackStack("名称")中的“名称”，该名称要保证与commit时设定的tag基本一致，之后再间接获取到找指定的fragment：
         第一步：事务提交,即：mFragmentManager.beginTransaction().add(R.id.recover_content_id, f1,FragmentStateRecvoer.class.getSimpleName())//--->注意
                       .addBackStack(FragmentStateRecvoer.class.getSimpleName());//--->注意
                       .commit();
                       注意：add方法的第三个参数为tag，即：FragmentStateRecvoer.class.getSimpleName() 与 addBackStack的参数是一致的
         第二步： int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
			System.out.println("回退栈中fragment的数量：" + backStackEntryCount);
			for (int i = 0; i < backStackEntryCount; i++) {
				BackStackEntry entry = mFragmentManager.getBackStackEntryAt(i);
				Fragment fragment = mFragmentManager.findFragmentByTag(entry.getName())//---->注意：这个entry.getName()就是tag
				System.out.println("当前fragment的Tag标签=" + entry.getName());
			}

13.fragment多个实例从栈中弹出：

14.增加ViewPager+Fragment的结合使用： 
需要明确的问题：ViewPager的预加载Fragment实例 + Fragment懒加载数据的问题----
解释；预加载fragment实例是因为viewpager内部机制控制并非仅加载当前用户所看到的fragment实例，而是要同时加载出多个fragment实例出来。
      懒加载是针对预加载fragment的情况下，出现预加载的fragment内部执行了加载数据的操作，而针对这种用户暂时看不见同时又去加载数据从用户的角度考虑
      这样做是多出流量的耗费问题，所以建议去控制这个让这个fragment在初始化了以及用户可见的情况下去加载真正所要的数据。

参看:https://blog.csdn.net/linglongxin24/article/details/53205878

注意：不要在 public void setUserVisibleHint(boolean isVisibleToUser) 这个重写方法里面处理懒加载处理逻辑。一进来有时候可能看不到第一页的数据，这个时候
你可以在onCreate 或者是onCreateView里面进行判断

经测试发现：public void setUserVisibleHint(boolean isVisibleToUser) 如果没有使用到ViewPager，则fragment不会执行这个方法的。






















