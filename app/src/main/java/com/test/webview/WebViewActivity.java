package com.test.webview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zhouwei.aaa.R;
import com.test.webview.bean.CallJavaClass;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *  WebView的实践：
 *  <1>js -->> android
 *  <2>android -->> js
 *  <3>webviewclient
 *  <4>webchromclient
 *  <5>实现免二次登录（利用cookie || share共享参数）
 *  <6>注意：实现原生+web=web app -->> 使用hbuilder + mui + vue.js
 *  <7>shouldInterceptRequest拦截资源，替换成本地资源
 *  <8>缓存：参考：https://blog.csdn.net/t12x3456/article/details/13745553
 *     当我们加载Html时候，会在我们data/应用package下生成database与cache两个文件夹:
       我们请求的Url记录是保存在webviewCache.db里，而url的内容是保存在webviewCache文件夹下.
       WebView中存在着两种缓存：网页数据缓存（存储打开过的页面及资源）、H5缓存（即AppCache）。
       注：页面缓存是webview默认功能，即使将dom + database + appcache 都关闭 也能缓存页面！

 一、网页缓存

 1、缓存构成
 /data/data/package_name/cache/
 /data/data/package_name/database/webview.db
 /data/data/package_name/database/webviewCache.db
 *  处理节点：
 *  >>>免二次登陆的处理方式以及原理：
 *                         就是利用webview的本地缓存技术，将用户第一次访问站点后，把站点返回的用户的相关uid,sid等缓存到本地，
 *                         下次在登陆的时候判断本地是否存在这些缓存的数据，
 *                         如果有，则跳过登陆，直接带过去缓存的参数访问地址即可。（有了认证的登陆状态的参数，就可以附带参数进行访问）
 *                         如果本地没有||过期了，需再次进行登陆认证流程。
 *
 *  >>>webview仅能通过loadurl的方式来完成界面的切换（包含：重定向加载），即：页面的切换加载必须要loadurl才可以！（目前理解）
 *  >>>点击网页中的按钮||链接后，回调到android java code & 发起http请求，服务端servlet接收到请求后，不能通过重定向的方式来改变webview的内部页面加载
 *
 *  >>>由于accessToken不能直接固定到网页中，以免造成他人利用，所以正常来讲放置到Java native
 *
 *
 *  >>> 报错：java.lang.Throwable: A WebView method was called on thread 'JavaBridge'. All WebView methods must be called on the same thread. (Expected Looper Looper (main, tid 1) {2f2fe63e} called on Looper (JavaBridge, tid 4997) {3073005f}, FYI main Looper is Looper (main, tid 1) {2f2fe63e})
                     at android.webkit.WebView.checkThread(WebView.java:2194)
                     at android.webkit.WebView.loadUrl(WebView.java:851)
                     at com.test.webview.bean.CallJavaClass.reloadLoginUrl(CallJavaClass.java:19)
                     at com.android.org.chromium.base.SystemMessageHandler.nativeDoRunLoopOnce(Native Method)
                     at com.android.org.chromium.base.SystemMessageHandler.handleMessage(SystemMessageHandler.java:28)
                     at android.os.Handler.dispatchMessage(Handler.java:102)
                     at android.os.Looper.loop(Looper.java:135)
                     at android.os.HandlerThread.run(HandlerThread.java:61)


 */
public class WebViewActivity extends AppCompatActivity {
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录
    private TextView mTitleTv;
    private WebView mWebView;
    private WebSettings webSettings;
    private Button errBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        errBtn = findViewById(R.id.btn_error_id);

        mTitleTv = findViewById(R.id.webview_title_tv_id);
        mWebView = findViewById(R.id.webview_id);
        mWebView.setWebChromeClient(new CustomWebChromeClient());
        mWebView.setWebViewClient(new CustomWebClient());

        webSettings = mWebView.getSettings();

        //js -->> android 的关键步骤之1
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //js -->> android 的关键步骤之2
        mWebView.addJavascriptInterface(new CallJavaClass(mWebView),"callwebview");

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(true);

        mWebView.getSettings().setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染

        //缩放操作
        webSettings.setSupportZoom(false); //开启支持缩放开关，默认为true，表示支持缩放，false为不支持缩放；此参数决定下面两个参数，是前提！
        webSettings.setBuiltInZoomControls(true); //控制是否可以手势缩放界面内容，false不能，true能
        webSettings.setDisplayZoomControls(true); //隐藏|显示原生的缩放控件，false不显示，true显示

        // 设置缓存模式 https://www.cnblogs.com/zhangqie/p/6171012.html
        if (NetworkUtils.isConnected()) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }


        //其他细节操作
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //缓存模式如下：
        //LOAD_CACHE_ONLY: 只读取本地缓存数据,不使用网络
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK:不管过期不过期，只要有取本地缓存，反之，再去取网络

        // 开启 DOM storage API 功能 https://blog.csdn.net/xhf_123/article/details/77893645
        webSettings.setDomStorageEnabled(true);//控制本地缓存+会话缓存。设置到的是local storage(不开启，则不能正常使用，禁用掉js操作local storage) + session storage
        webSettings.setAppCacheEnabled(true);//开启Application Caches 功能

        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能

//        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        mWebView.getSettings().setAppCachePath(cacheDirPath);

        webSettings.setAllowFileAccess(false); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        boolean isLogin = loadUrlSettingCookie();
        if (isLogin){
            ToastUtils.showShort("已经登陆过了，直接方法首页！");
//            mWebView.loadUrl("http://192.168.1.111:8080/SessionExample/main.jsp");
        }else{
            ToastUtils.showShort("请登录！");
//            mWebView.loadUrl("http://192.168.1.111:8080/SessionExample/login.html");
        }
//        mWebView.loadUrl("file:///android_asset/html/index.html");
        mWebView.loadUrl("https://m.baidu.com/?from=844b&vit=fpsdfs");
//        mWebView.loadUrl("http://192.168.1.111:8080/SessionExample/login.html");
    }

    //loadurl之前设置cookie到webview中
    private boolean loadUrlSettingCookie(){
        String account = SPUtils.getInstance("sp").getString("account");
        String ssid = SPUtils.getInstance("sp").getString("ssid");
        System.out.println("login --->> account="+ account +",ssid="+ssid);

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(ssid))
            return false;

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //注意：每一对 key-value 需要分开设置, 不能这样写:
        String url = "http://192.168.1.111:8080/SessionExample/main.jsp";
        cookieManager.setCookie(url, "account=" + SPUtils.getInstance("sp").getString("account"));
        cookieManager.setCookie(url, "ssid="+ SPUtils.getInstance("sp").getString("ssid"));
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
        return true;
    }

    private void print(String ss){
        System.out.println(ss);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        mWebView.onPause();
        //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //激活WebView为活跃状态，能正常执行网页的响应
        mWebView.onResume();
        //恢复pauseTimers状态
        mWebView.resumeTimers();
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            //-------------
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearHistory();
            //
            mWebView.destroy();
            mWebView = null;
        }

        //清空所有Cookie
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

        super.onDestroy();
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void LOAD_CACHE_ONLY_CLICK(View view) {
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//        loadUrl();
        SPUtils.getInstance("sp").put("cache_mode",0);
    }

    public void LOAD_NO_CACHE_CLICK(View view) {
        SPUtils.getInstance("sp").put("cache_mode",1);
    }

    public void LOAD_CACHE_ELSE_NETWORK_CLICK(View view) {
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        SPUtils.getInstance("sp").put("cache_mode",2);
    }

    public void LOAD_DEFAULT_CLICK(View view) {
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        SPUtils.getInstance("sp").put("cache_mode",3);
    }

    private void loadUrl(){
        boolean isLogin = loadUrlSettingCookie();
        if (isLogin){
            ToastUtils.showShort("已经登陆过了，直接方法首页！");
            mWebView.loadUrl("http://192.168.1.111:8080/SessionExample/main.jsp");
        }else{
            ToastUtils.showShort("请登录！");
            mWebView.loadUrl("http://192.168.1.111:8080/SessionExample/login.html");
        }

    }

    //JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
    //经测试发现，如果存在缓存，则可能会在执行下面的代码时无法执行调用
    public void onJsAlert(View view) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在小于 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mWebView.loadUrl("javascript:callJS()");
        } else {
            mWebView.evaluateJavascript("javascript:call2JavaMethod('hello android!')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    ToastUtils.showShort("Java received info="+ value);
                }
            });
        }
    }

    public void onJsConfirm(View view) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在小于 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mWebView.loadUrl("javascript:confirmFun()");
        } else {
            mWebView.evaluateJavascript("javascript:confirmFun()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    ToastUtils.showShort("Java received info="+ value);
                }
            });
        }
    }

    public void onJsPrompt(View view) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在小于 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mWebView.loadUrl("javascript:jsInterface.onButtonClick('hello webview')");
        } else {
            mWebView.evaluateJavascript("javascript:jsInterface.onButtonClick('hello webview')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    ToastUtils.showShort("Java received info="+ value);
                }
            });
        }
    }

    public void goForwordClick(View view) {
        if (mWebView.canGoForward())
            mWebView.goForward();
    }

    public void goBackClick(View view) {
        if (mWebView.canGoBack())
            mWebView.goBack();
    }

    // https://blog.csdn.net/xhf_123/article/details/77893645  --->>>local storage
    public void localStorageClick(View view) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                String userAgent = "shixinzhang";
                String js = "window.localStorage.setItem('userAgent','" + userAgent + "');";
                String jsUrl = "javascript:(function({"+
                                "var localStorage = window.localStorage;"+
                                "localStorage.setItem('userAgent','" + userAgent + "')"+
                                "})()";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript(js, null);
                } else {
                    mWebView.loadUrl(jsUrl);
                    mWebView.reload();
                }
            }
        });
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            System.out.println("-->>onReceivedTitle -->" + title);
            // android 6.0 以下通过title获取
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                if (title.contains("404") || title.contains("500") || title.contains("Error")) {
//                    view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    view.loadUrl("file:///android_asset/html/error_page.html");
//                    return;
//                }
//            }



            //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
            if(!TextUtils.isEmpty(title)&&title.toLowerCase().contains("error")){
                loadError = true;
            }

            if (title == null || "".equals(title)){
                mTitleTv.setText("默认标题");
            }else{
                mTitleTv.setText(title);
            }
        }

        //获取加载进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            System.out.println("-->>onProgressChanged");
            String progress = "";
            if (newProgress < 100) {
                progress = newProgress + "%";
            } else if (newProgress == 100) {
                progress = newProgress + "%";
            }

            System.out.println("网页加载进度=" + progress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            System.out.println("第一步：间接通过js回调到java前台并接收要回调的参数...");
            System.out.println("第二步：解析回调的请求参数=" + message);
            //第二步：解析回调的请求参数={"obj":"jsInterface","func":"onButtonClick","args":['']}
            try {
                JSONObject jsonObject = new JSONObject(message);
                String objName = jsonObject.getString("obj");
                String callMethod = jsonObject.getString("func");
                String params = jsonObject.getJSONArray("args").getString(0);

                System.out.println("请求参数：" + objName + "," + callMethod + "," + params);

                //反射部分
                Class<CallJavaClass> callJavaCalzz = CallJavaClass.class;
                //出现了报错：Class类的newInstance方法抛出InstantiationException异常
                //解决方式：将实体类定义到非内部即可
                Object newInstance = callJavaCalzz.newInstance();

                //得到自己的所有的内部方法，包含private | public 修饰的方法，
                //过滤掉Object类的方法（ "getClass","hashCode","notify","notifyAll","equals","toString","wait"）
                Method declaredMethod = callJavaCalzz.getDeclaredMethod(callMethod, String.class);

                Object invoke = declaredMethod.invoke(newInstance, params);
                System.out.println("第三步：反射回调Java对象内部方法，结果为：" + invoke.toString());

                //----------------------------dialog--------------------------------
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("对话框").setMessage(message);//{"key":"value"}

                final EditText et = new EditText(view.getContext());
                et.setSingleLine();
                et.setText(invoke.toString());//invoke.toString() -->> 处理后接收的结果
                builder.setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }

                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                        ToastUtils.showShort("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                        return true;
                    }
                });

                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //为确保下次能够正常再次触发点击事件，需要提交结果，否则，html中的按钮不能够再次被触发，进而导致该方法不响应的问题。
            result.confirm();
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定", null);

            // 不需要绑定按键事件
            // 屏蔽keycode等于84之类的按键
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {//Android KEYCODE键值对应大全  https://blog.csdn.net/shililang/article/details/14449527
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                    ToastUtils.showShort("onJsAlert", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("对话框")
                    .setMessage(message)
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            result.confirm();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });

            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                    ToastUtils.showShort("onJsConfirm", "keyCode==" + keyCode + "event="+ event);
                    return true;
                }
            });
            // 禁止响应按back键的事件
            // builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

    }
    private boolean loadError;
    private class CustomWebClient extends WebViewClient {
        private final int SC_NOT_FOUND = 404;
        //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
        //步骤2：将该html文件放置到代码根目录的assets文件夹下

        //步骤3：复写WebViewClient的onRecievedError方法
        //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            System.out.println("-->>onReceivedError");


            loadError = true;

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            System.out.println("-->>onReceivedError FOR M");
//            //super.onReceivedError(view, request, error);
//            // 这个方法在6.0才出现
//            int statusCode = error.getErrorCode();
//            System.out.println("onReceivedHttpError code = " + statusCode);
//            if (404 == statusCode || 500 == statusCode) {
//                //view.loadUrl("about:blank");// 避免出现默认的错误界面
//                view.loadUrl("file:///android_asset/html/error_page.html");
//                return;
//            }

            loadError = true;
        }

        //这个方法过时了，使用下面这个方法：shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        //url重定向会执行此方法以及点击页面某些链接也会执行此方法
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("-->>shouldOverrideUrlLoading");
            //请求的url
            System.out.println("WebViewManger,shouldOverrideUrlLoading url:"+ url);

            WebView.HitTestResult hitTestResult = view.getHitTestResult();
            int hitType = hitTestResult.getType();
            if (hitType != WebView.HitTestResult.UNKNOWN_TYPE) {
                System.out.println("WebViewManger,没有进行重定向操作");
                //这里执行自定义的操作
                view.requestFocus();
//                view.loadUrl(url);
                Map<String,String> headerMap = new HashMap<>();
                headerMap.put("test_header","xxx123");
                view.loadUrl(url,headerMap);
                return true;

            } else{
                System.out.println("WebViewManger进行了重定向操作");//对于重定向：因为返回false系统会执行加载，没必要在执行webVIew.loadUrl(url)
                //重定向时hitType为0 ,执行默认的操作
                return false;//或者返回默认 super.shouldOverrideUrlLoading(view,url);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            System.out.println("-->>shouldOverrideUrlLoading for lollipop="+ request.getUrl());

            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            //设定加载资源的操作
            System.out.println("-->>onLoadResource");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            System.out.println("-->>onPageStarted");

        }
        boolean isFist = true;
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            System.out.println("-->>onPageFinished");

            if (loadError){//加载失败
                errBtn.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                return;
            }else{//加载成功
                errBtn.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
            }

            if (url.contains("login.html") || url.contains("login.jsp")){
                isFist = false;
                return;
            }

            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);

            print("[Cookie来源的URl]:"+ url +",[结束到服务端传过来的Cookie]:" + CookieStr);

            //Cookie的数据：JSESSIONID=D142B0E92D02AECD8EB5DE099B08EB56; ssid=201081549-100-911211697114-41-54-457183-73; account=aaa
            if (StringUtils.isEmpty(CookieStr)){
                return ;
            }
            String[] split = CookieStr.split(";");
            for (int i = 0; i < split.length; i++) {
                if(split[i].equals("ssid")){
                    SPUtils.getInstance("sp").put("ssid",split[i]);
                }else if (split[i].equals("account")){
                    SPUtils.getInstance("sp").put("account",split[i]);
                }
            }
            Toast.makeText(WebViewActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();

//            ActivityUtils.startActivity(WebViewActivity.this,WebViewMainActivity.class);
//            finish();
        }

        //API21以下用shouldInterceptRequest(WebView view, String url)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            System.out.println("-->>WebResourceResponse");

            // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
            // 此处网页里图片的url为:http://s.ip-cdn.com/img/logo.gif
            // 图片的资源文件名为:logo.gif
            if (1==1)return super.shouldInterceptRequest(view,url);

            if (url.contains("xifan_5121.png")) {

                InputStream is = null;
                // 步骤2:创建一个输入流
                try {
                    is =getApplicationContext().getAssets().open("img/active_img.gif");
                    // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                    // 在app/src/main下创建一个assets文件夹
                    // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 步骤4:替换资源

                WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
                // 参数1:http请求里该图片的Content-Type,此处图片为image/png
                // 参数2:编码类型
                // 参数3:替换资源的输入流

                System.out.println("旧API");
                return response;
            }

            return super.shouldInterceptRequest(view, url);
        }

        // API21以上用shouldInterceptRequest(WebView view, WebResourceRequest request)
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            System.out.println("-->>WebResourceResponse LOLLIPOP");

            if (1==1)return super.shouldInterceptRequest(view,request);

            // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
            // 此处图片的url为:http://s.ip-cdn.com/img/logo.gif
            // 图片的资源文件名为:logo.gif
            if (request.getUrl().toString().contains("xifan_5121.png")) {

                InputStream is = null;
                // 步骤2:创建一个输入流

                try {
                    is = getApplicationContext().getAssets().open("img/active_img.gif");
                    // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                    // 在app/src/main下创建一个assets文件夹
                    // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //步骤4:替换资源

                WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
                // 参数1：http请求里该图片的Content-Type,此处图片为image/png
                // 参数2：编码类型
                // 参数3：存放着替换资源的输入流（上面创建的那个）

                return response;
            }
            return super.shouldInterceptRequest(view, request);
        }
    }


}
