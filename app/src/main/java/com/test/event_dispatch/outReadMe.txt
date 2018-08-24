
####################################### Outter ViewPager ######################################
#在ViewPager中上下滑动Listview，注意：onInterceptTouchEvent方法在处理down执行一次，move执行多次，up不执行
为什么Viwepager的拦截方法对于 up 操作不执行了，估计是因为move的时候已经确定后续事件交给ListView，则拦截方法自然就不处理后续事件了。
一般情况下，在move的时候会动态判断上下级的由谁处理，up自然就跟随处理了（我暂时是这样理解的）
08-19 16:49:35.050 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----down
08-19 16:49:35.050 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:49:35.050 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-down
08-19 16:49:35.100 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----move
08-19 16:49:35.100 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:49:35.100 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-move
08-19 16:49:35.110 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----move
08-19 16:49:35.110 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:49:35.110 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-move
08-19 16:49:35.130 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----move
08-19 16:49:35.130 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:49:35.130 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-move
08-19 16:49:35.140 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:49:35.140 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-up

############ ViewPager的左右滑动 ######
# ViewPager 的拦截up事件也是不执行，仍保持我个人理解（即：既然已经确定最终的执行者，则就不在处理up拦截，直接执行onTouchEvent了）
08-19 16:59:32.780 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----down
08-19 16:59:32.780 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent
08-19 16:59:32.780 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--onTouchEvent-down
08-19 16:59:32.790 2578-2578/com.example.zhouwei.aaa I/System.out: onInterceptTouchEvent is called!----move
08-19 16:59:32.790 2578-2578/com.example.zhouwei.aaa I/System.out: outer listview--dispatchTouchEvent

