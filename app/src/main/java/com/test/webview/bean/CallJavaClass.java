package com.test.webview.bean;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class CallJavaClass extends Object {

	WebView webview ;

    //java.lang.InstantiationException: class com.test.webview.bean.CallJavaClass has no zero argument constructor
    public CallJavaClass(){}

	public CallJavaClass(WebView view){
		this.webview = view;
	}

	@JavascriptInterface
	public String printTestInfo(String content){
		System.out.println("已通过反射的方式，回调到此方法！");
		return "解决了信息泄漏的问题";
	}
	@JavascriptInterface
	public void reloadLoginUrl(){
        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("about:blank");// 避免出现默认的错误界面
                webview.loadUrl("http://192.168.1.111:8080/SessionExample/login.html");
            }
        });
	}
}
