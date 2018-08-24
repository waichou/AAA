package com.test.retrofit2.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.test.retrofit2.interfaces.IGetListener;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhouwei on 2018/5/9.
 * 负责构建网络请求
 */

public class RetrofitUtils {
    /**
     * Get Retro Client
     *
     * @return JSON Object
     */
    private static Retrofit getRetroClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static IGetListener getApiService(String baseUrl) {
        return getRetroClient(baseUrl).create(IGetListener.class);
    }
}
