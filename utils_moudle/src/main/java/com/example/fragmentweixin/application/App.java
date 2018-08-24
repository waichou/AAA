package com.example.fragmentweixin.application;

import android.app.Application;

import com.example.fragmentweixin.fragmentation.Fragmentation;
import com.example.fragmentweixin.interfaces.ExceptionHandler;

/**
 * Created by YoKey on 16/11/23.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                    	//将事务提交出错的异常信息，根据需求进一步处理。e.getMessage
                    	System.out.println("#################捕捉事务提交出错异常信息START####################");
                    	e.printStackTrace();
                    	System.out.println("#################捕捉事务提交出错异常信息END####################");
                    }
                })
                .install();
    }
}
