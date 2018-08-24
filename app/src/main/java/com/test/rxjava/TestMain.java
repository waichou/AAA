package com.test.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import rx.schedulers.Schedulers;

/**
 * Created by zhouwei on 2018/5/17.
 */

public class TestMain {

    public static void main(String[] args) {
        test2();

//        Observable.create(new ObservableOnSubscribe<Object>() {
//
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
//                e.onNext(11);
//                e.onComplete();
//            }
//        }).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                System.out.println("hand");
//            }
//
//            @Override
//            public void onNext(@NonNull Object o) {
//                System.out.println("val--" + o);
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                System.out.println("error");
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("finish");
//            }
//        });
    }

    private static void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(0);
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("第一个观察者执行doOnNext---" + integer);

                    }
                })
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        if (integer == 0) {//发现第一次请求的数据结果是失败的状态，所以不要求
//                    return Observable.just("0");

                            throw new Exception("exist is error!");
                        }
                        return Observable.just("ss-" + integer);
                    }
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        System.out.println("连接---" + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if ("0".equals(s)) {
                            System.out.println("onNext---提示异常吧！" + s);
                        } else {
                            System.out.println("onNext---提示成功吧！" + s);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println("error--error-" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("finish---");
                    }
                });

    }

    private static void test3() {
        Observable.just(1, 2, 3)
//                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {//第一个被观察者 -->第一个观察者
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("第一个观察者执行doOnNext---" + integer);

                    }
                })
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
//                        if (integer == 2)
//                            throw new NullPointerException("error is null !");
//
                        return Observable.just("hello---" + integer);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                        System.out.println("第二个观察者接收结果--" + result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("登录失败");
                    }
                });
    }
}
