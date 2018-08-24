package com.utils_moudle;


import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by zhouwei on 2018/7/24.
 */

public class MyUtilsApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
