package com.test.retrofit2.interfaces;


import com.test.retrofit2.bean.ResultMsg;
import com.test.retrofit2.body_for_bean.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhouwei on 2018/5/7.
 *
 * http请求报400错误的原因分析: 就是语法错误造成的
 *
 */
public interface IGetListener {
    //---------------------------------------------------Get----------------------------------------
    //第一种注解头的方式
    @Headers(
              {  /*"Connection: Keep-Alive",*/
                "Accept-Language: zh-Hans-CN, zh-Hans; q=0.5",
                "Accept: text/html, application/xhtml+xml, image/jxr, */*"
              }
             )
    //第二种注解头的方式
//    @Headers("Cache-Control: max-age=640000")
    @GET("SessionExample/servlet/{path}")//注意：{path}替换标签，不能单独使用，需要与其他前缀或后缀结合使用，如：xxx/{path}/yyy
                                             //注意：增加的header不允许是中文
    Observable<ResultMsg> reqForGet1(@Header("Set-Cookie") String conn,@Header("temp_header")String temp,@Path("path") String path,@QueryMap Map<String,String> map,@QueryMap Map<String,Object> addMap);

    @HTTP(method = "GET", hasBody = false)//定义请求的另种方式 && 当有URL注解时，@GET传入的URL就可以省略 &&当GET、POST...HTTP等方法中没有设置Url时，则必须使用 {@link Url}提供
    Observable<ResultMsg> reqForGet2(@Url String url,@Query("username") String username ,@Query("password") String password);


    //对于query?key1=v1&key2=v2中的问号，在这种情况下可以简写“@GET("query")”或者@GET("query?")
    //@Url cannot be used with @GET URL (parameter #1) 错误原因：是因为@GET中定义了请求地址，则请求参数中不能再使用@URL
    //形成的请求URL=http://192.168.1.111:8080/SessionExample/servlet/LoginServlet?&username=aaa&password=123&list[]=aaa&linked[]=aaa&linked[]=bbb&linked[]=ccc
    @GET("SessionExample/servlet/LoginServlet?")
    Observable<ResultMsg> reqForGet3(@Query("username") String username , @Query("password") String password, @Query("list[]") List<String> listParamsList,@Query("linked[]") String... linked);


    //获取单个网络图片
    @GET
    @Streaming
    Observable<ResponseBody> getRequestGetBitmapForRxJava(@Url String url1);

    //---------------------------------------------------Post----------------------------------------

    //注意的是：是直接Post请求,不是某个表单页面地址
    //提交Field字段值时，必须要有FormUrlEncoded
    //当GET、POST...HTTP等方法中没有设置Url时，则必须使用 {@link Url}提供
    @FormUrlEncoded
    @POST("SessionExample/servlet/LoginServlet")
    Observable<ResultMsg> reqForPost1(@Field("username") String username , @Field("password") String password, @Field("list[]") List<String> listParamsList,@Field("linked[]") String... linked);

    //第一种注解头的方式
    @Headers(
            {  /*"Connection: Keep-Alive",*/
                    "Accept-Language: zh-Hans-CN, zh-Hans; q=0.5",
                    "Accept: text/html, application/xhtml+xml, image/jxr, */*"
            }
    )
    //第二种注解头的方式
//    @Headers("Cache-Control: max-age=640000")
    @POST("SessionExample/servlet/{path}")//注意：{path}替换标签，不能单独使用，需要与其他前缀或后缀结合使用，如：xxx/{path}/yyy
    @FormUrlEncoded  //注意：增加的header不允许是中文
    Observable<ResultMsg> reqForPost2(@Header("Connection") String conn, @Header("temp_header")String temp, @Path("path") String path, @FieldMap Map<String,String> map, @FieldMap Map<String,Object> addMap);

    @HTTP(method = "POST", hasBody = true) //定义请求的另种方式 && 当有URL注解时，@GET传入的URL就可以省略 &&当GET、POST...HTTP等方法中没有设置Url时，则必须使用 {@link Url}提供
    @FormUrlEncoded                         //如果POST请求中的 hasBody=false 报错：FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).
                                            //必须为：hasBody=true
    Observable<ResultMsg> reqForPost3(@Url String url,@Field("username") String username ,@Field("password") String password);


    //上传图片，txt ，音频，视频 文件 (都是单个|多个附件上传)
    @POST
    @Multipart //注意上传文件的时候需要加上注解"Multipart"
    Call<ResultMsg> uploadFile2NetWorkForRxJava(@Url String url, @PartMap Map<String, RequestBody> requestBodyMap, @Part MultipartBody.Part ...part);

    //上传图片，txt ，音频，视频 文件 (都是单个|多个附件上传)
    @POST//@Body parameters cannot be used with form or multi-part encoding. (parameter #1)
    Flowable<ResultMsg> uploadFile2NetWorkForRxJava(@Url String url,@Body RequestBody Body);

    //post请求以body作为请求体参数项，比FieldMap|Filed都方便
    //注意：@Body标签不能同时和@FormUrlEncoded、@Multipart标签同时使用。
    @POST("SessionExample/servlet/LoginServlet")
    Observable<ResultMsg> reqForBodyByPost(@Body User user);

}
