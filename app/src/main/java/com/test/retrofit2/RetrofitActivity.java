package com.test.retrofit2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhouwei.aaa.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.test.retrofit2.bean.ResultMsg;
import com.test.retrofit2.body_for_bean.User;
import com.test.retrofit2.cookie.CookieManager;
import com.test.retrofit2.interfaces.IGetListener;
import com.test.retrofit2.intorceptor.HttpCommonInterceptor;
import com.test.retrofit2.utils.RetrofitUtils;

import io.reactivex.Observable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private final String REQ_SERVLET_URL = "SessionExample/servlet/LoginServlet";
    private final String BASE_URL = "http://192.168.1.111:8080/";

    private  IGetListener mListener;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit2_rxjava2);
        mImageView = (ImageView) findViewById(R.id.img_id);
        initRetrofit();
    }

    public void print(String ss){
        System.out.println(ss);
    }

    /**
     * 初始化Retrofit实例。并创建接口类。
     * 注意：IpService不须要我们去实现。直接Retrofit=类有create方法生成。
     */
    private void initRetrofit() {
        //创建Retrofit的实例，把Gson转换器设置下
        HttpCommonInterceptor httpCommonInterceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("hh", "heihei")
                .build();

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(httpCommonInterceptor);
        okHttpClientBuilder.cookieJar(new CookieManager(this));//参考：https://www.jianshu.com/p/1a222a9394ce

        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClientBuilder.build())
                //设置API的基础地址（注意：必须需要设置baseUrl 以及 baseUrl要以“/”结尾，否则报错提示：Caused by: java.lang.IllegalArgumentException: baseUrl must end in /: http://gank.io/api/data/Android/10）
//                .baseUrl("http://gank.io/api/data/Android/10/")

//                .baseUrl("http://www.kuaidi100.com/")
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//设置后才才支持json字符串转化为Bean
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //使用Retrofit的create方法传入创建接口实例
        mListener = retrofit.create(IGetListener.class);
    }


    //-------------------------------------整理部分---------------------------------

    //[get](headers+header+path+QueryMap)
    public void reqForGetClick_1(View view) {
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("username","aaa");
        reqMap.put("password","123");

        Map<String,Object> reqAddMap = new HashMap<>();
        reqAddMap.put("add_1","111");
        reqAddMap.put("add_2",333);

        mListener.reqForGet1("Keep-Alive","temp_head_xxx","LoginServlet",reqMap,reqAddMap)//若header参数值为 null ，这个头会被省略，否则，会使用参数值的 toString 方法的返回值
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //[get](url+query+http+hasBody)
    public void reqForGetClick_2(View view) {
        mListener.reqForGet2(REQ_SERVLET_URL,"aaa","123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //[get](单个Query+一键多值Query)
    public void reqForGetClick_3(View view) {
        List<String> reqParamsList = new ArrayList<>();
        reqParamsList.add("aaa");

        mListener.reqForGet3("aaa","123",reqParamsList,new String[]{"aaa","bbb","ccc"})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * GET方式下载网络图片(单张)
     */
    public void reqForGetClick_4_getImage(View view) {
        //创建Retrofit的实例，把Gson转换器设置下
        Retrofit retrofit = new Retrofit
                .Builder()
                //设置API的基础地址（注意：必须需要设置baseUrl 以及 baseUrl要以“/”结尾，否则报错提示：Caused by: java.lang.IllegalArgumentException: baseUrl must end in /: http://gank.io/api/data/Android/10）
                .baseUrl("http://pic41.nipic.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //使用Retrofit的create方法传入创建接口实例
        IGetListener mListener = retrofit.create(IGetListener.class);

        mListener.getRequestGetBitmapForRxJava("20140509/4746986_145156378323_2.jpg")
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull ResponseBody responseBody) throws Exception {
                        return BitmapFactory.decodeStream(responseBody.byteStream());
                    }
                })
                .subscribeOn(Schedulers.io())//注意引入的时这个包下的调度实例 //向上设定发布者的处理线程
                .observeOn(AndroidSchedulers.mainThread())//向下设定订阅者的处理线程
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {

                        mImageView.setImageBitmap(bitmap);
                    }
                });
    }



    //------------------------------------ Post 部分 -----------------------------------------------
    //[post](单个Query+一键多值Query)
    public void reqForPostClick1(View view) {
        List<String> reqParamsList = new ArrayList<>();
        reqParamsList.add("aaa");

        mListener.reqForPost1("aaa","123",reqParamsList,new String[]{"aaa","bbb","ccc"})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //[post](headers+header+path+QueryMap)
    public void reqForPostClick2(View view) {
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("username","aaa");
        reqMap.put("password","123");

        Map<String,Object> reqAddMap = new HashMap<>();
        reqAddMap.put("add_1","111");
        reqAddMap.put("add_2",333);

        mListener.reqForPost2("Keep-Alive","temp_head_xxx","LoginServlet",reqMap,reqAddMap)//若header参数值为 null ，这个头会被省略，否则，会使用参数值的 toString 方法的返回值
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //[post](url+query+http+hasBody)
    public void reqForPostClick3(View view) {
        mListener.reqForPost3(REQ_SERVLET_URL,"aaa","123")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * [post](upload multiple files 2 server \n<first method>)
     * POST上传多个文件以及请求参数[第一个负责上传数据的方法]
     */
    public void reqForPostClick4(View view) {
        //第一个文件-------------------------
        String filePath =  Environment.getExternalStorageDirectory() + "/123.png";
        //File creating from selected URL
        File file = new File(filePath);

        // create RequestBody instance from file 封装请求part1
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        //第二个文件-------------------------
        String filePath2 = Environment.getExternalStorageDirectory() + "/text.txt";
        //File creating from selected URL
        File file2 = new File(filePath2);

        // create RequestBody instance from file 封装请求part2
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("uploaded_file", file2.getName(), requestFile2);

        //Other params
        MediaType textType = MediaType.parse("text/plain");
        RequestBody username = RequestBody.create(textType, "aaa");
        RequestBody password = RequestBody.create(textType, "123");

        //封装请求参数-body中
        Map<String, RequestBody> fileUpload2Args = new HashMap<>();
        fileUpload2Args.put("username", username);
        fileUpload2Args.put("password", password);

        Call<ResultMsg> resultMsgCall = mListener.uploadFile2NetWorkForRxJava(REQ_SERVLET_URL,fileUpload2Args,body,body2);
        resultMsgCall.enqueue(new Callback<ResultMsg>() {
            @Override
            public void onResponse(Call<ResultMsg> call, Response<ResultMsg> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")){
                        Toast.makeText(RetrofitActivity.this, "成功！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RetrofitActivity.this, "不成功！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultMsg> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(RetrofitActivity.this, "错误！" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //[post](upload multiple files 2 server \n<second method>)
    public void reqForPostClick5(View view) {
        //第一个文件
        String filePath = Environment.getExternalStorageDirectory() + "/123.png";
        File file = new File(filePath);

        String filePath2 = Environment.getExternalStorageDirectory() + "/text.txt";
        File file2 = new File(filePath2);

        List<File> fileList = new ArrayList<>();
        fileList.add(file);
        fileList.add(file2);

        Flowable<ResultMsg> resultMsgCall = uploadMultiPartFile(fileList);
        resultMsgCall.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<ResultMsg>() {
                    @Override
                    public void onNext(ResultMsg resultMsg) {
                        print("接收结果:" + resultMsg);
                    }

                    @Override
                    public void onError(Throwable t) {
                        print("错误:" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        print("完成");
                    }
                });
    }

    /**
     * 配合上面方法[uploadMoreFile2NetworkForPost2] 结合访问调用
     * @param fileList
     * @return
     */
    private Flowable<ResultMsg> uploadMultiPartFile(List<File> fileList) {
        //构建body
        //addFormDataPart()第一个参数为表单名字，这是和后台约定好的
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "aaa")
                .addFormDataPart("password", "123");
        //注意，file是后台约定的参数，如果是多图，file[]，如果是单张图片，file就行
        for (File file : fileList) {
            //这里上传的是多图
            builder.addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        RequestBody requestBody = builder.build();
        return RetrofitUtils.getApiService(BASE_URL).uploadFile2NetWorkForRxJava("SessionExample/servlet/LoginServlet",requestBody);
    }

    public void reqForPostClick6(View view) {
        Observable<ResultMsg> resultMsgObservable =  mListener.reqForBodyByPost(new User("aaa","123"));
        resultMsgObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultMsg>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultMsg resultMsg) {
                        print("成功获取数据--->>" + resultMsg.getResult());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        print("出错了--->" +e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //flatmap + compose

    /**
     08-13 19:44:59.375 31082-31954/com.example.zhouwei.aaa I/System.out: flatMap 当前线程---RxCachedThreadScheduler-1
     08-13 19:45:28.165 31082-31954/com.example.zhouwei.aaa I/System.out: 转换时机---->>原数据源--->>ResultMsg{code=1, result='success'}
     08-13 19:45:44.765 31082-31954/com.example.zhouwei.aaa I/System.out: compost-->flatMap 当前线程---RxCachedThreadScheduler-1
     08-13 19:45:46.315 31082-31082/com.example.zhouwei.aaa I/System.out: 结果--->>111+122
     08-13 19:45:46.315 31082-31082/com.example.zhouwei.aaa I/System.out: Consumer当前线程---main
     * @param view
     */
    public void testRxjavaForFlatMapClick(View view) {
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("username","aaa");
        reqMap.put("password","123");

        Map<String,Object> reqAddMap = new HashMap<>();
        reqAddMap.put("add_1","111");
        reqAddMap.put("add_2",333);

        mListener.reqForGet1("Keep-Alive","temp_head_xxx","LoginServlet",reqMap,reqAddMap)//若header参数值为 null ，这个头会被省略，否则，会使用参数值的 toString 方法的返回值
                .compose(new ObservableTransformer<Object, Object>() {

                    @Override
                    public ObservableSource<Object> apply(@NonNull Observable<Object> upstream) {
                        return upstream.flatMap(new Function<Object, ObservableSource<?>>() {

                            @Override
                            public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                                System.out.println("compost-->flatMap 当前线程---"+ Thread.currentThread().getName());

                                return Observable.just(o.toString() + "122");
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                }).flatMap(new Function<Object, ObservableSource<?>>() {

                        @Override
                        public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                            System.out.println("flatMap 当前线程---"+ Thread.currentThread().getName());
                            System.out.println("转换时机---->>原数据源--->>" + o.toString());
                            return Observable.just(o.toString() + "111");
                        }
                    })
/*
        new Function<ResultMsg, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull ResultMsg resultMsg) throws Exception {
                System.out.println("flatMap 当前线程---"+ Thread.currentThread().getName());
                System.out.println("转换时机---->>原数据源--->>" + resultMsg);
                return Observable.just("111");
            }
        }*/
//                .flatMap()

//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("结果--->>" + o);
                        System.out.println("Consumer当前线程---"+ Thread.currentThread().getName());
                    }
                });

    }
}
