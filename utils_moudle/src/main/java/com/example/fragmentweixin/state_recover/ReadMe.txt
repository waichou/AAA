工程功能描述：

1.保存fragment状态新方式：
  1.1可以直接通过调用fragmentManager.putFragment方法在Activity的onSaveInstanceState方法中执行保存状态
          在Activity的onCreate方法中嗲用fragmentManager.getFragment方法获取重新恢复的这个fragment实例！
         【注意】经测试发现：这种方式恢复获取到的实例 == fragment试图栈中恢复的实例     ====>相同
         
  【小结】工程内部实现方式：借助上述putFragment方法，A fragment 为创建 B fragment bundle对象，bundle对象中
         包含requestcode , resultCode , bundle ，三个参数维护在一个RecodeResult实体中，在跳转前执行
    putFragment,在退出前执行getFragment传递给A fragment接收参数的方法。 
  
 