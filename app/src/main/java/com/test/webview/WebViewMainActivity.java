package com.test.webview;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zhouwei.aaa.R;
//login --- jump -->>mainactivity -->> logout （ remove cookie && 不能检索出cookie）——————>> login （webviewactivity && 提示重新登陆 ）
public class WebViewMainActivity extends AppCompatActivity {

    boolean isLogin = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_main);
    }

    public void loginAndLoginOutClick(View view) {
        if (isLogin){
            ToastUtils.showShort("开始注销！");
            isLogin = false;
            removeCookie();
        }else{
            ToastUtils.showShort("重新登陆！");
            ActivityUtils.startActivity(this,WebViewActivity.class);
            finish();
        }
    }

    private void removeCookie(){
        SPUtils.getInstance("sp").remove("account");
        SPUtils.getInstance("sp").remove("ssid");


        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.

        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }

        String CookieStr = cookieManager.getCookie("http://192.168.1.111:8080/SessionExample/main.jsp");
        System.out.println("[移除后是否还能获取到缓存的Cookie]:" + CookieStr);

    }
}
