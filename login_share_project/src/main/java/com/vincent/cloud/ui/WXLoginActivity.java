package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.vincent.cloud.R;
import com.vincent.cloud.base.App;
import com.vincent.cloud.base.Config;
import com.vincent.cloud.entity.WXUserInfo;
import com.vincent.cloud.entity.WxRefresh;
import com.vincent.cloud.util.SharedPreferencesUtils;
import com.vincent.cloud.wxapi.WXEntryActivity;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * @name Login
 * @class name：com.vincent.cloud
 * @class describe
 * @anthor Vincent
 * @time 2017/7/19 10:34
 * @change
 * @chang time
 * @class describe
 *
 * 参看微信登陆官方文档：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317851&token=ef74535fc4031d14b6ed626f50f46050a38f9fd3&lang=zh_CN
 *
 * 小结：1.每次重新登陆过程都是全新的。
 *
 * 微信登陆处理逻辑：
 * 1.首次登陆需要获取相应的参数，如：access_token , refresh_token 以及微信用户的相关信息 并进行相应参数的缓存
 * 2.控制多次登陆的情况
 * 3.登陆时，首要判断是否本地已存在相应参数，如access_token，如果不存在，则直接请求获取access_token参数，之后通过access_token & openid 获取用户信息。
 *   注意：需要将access_token & 相关需要的用户信息进行缓存
 *
 *   获取access_token地址：https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
 *
 *   判断access_token是否有效地址：https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
 *                                返回结果：{"errcode":0,"errmsg":"ok"}
 *                                         {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest, hints: [ req_id: ttjisa06622045 ]"}
 *
 *   退出登陆时，则将缓存的数据清除掉！[注意：没有发现微信登陆注销的api，即：不能像QQ注销切换用户进行重新登陆的能力]
 *
 *   如果有需求在保持登陆的状态下，要获取更新后的用户信息，则要加入access_token的过期判断，
 *   <1></>如果access_token在本地不存在，则正常获取access_token参数 + 用户信息 & 缓存，
 *   <2></>如果access_token在本地存在，则判断当前access_token是否过期，如果过期了，则刷新或续增时间，
 *         调用接口：https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
 *         反之，如果没有过期，则直接正常使用access_token参数去获取相应的用户信息。
 *
 *   具有唯一性的参数有：
 *   openid unionid  （已确认唯一)
 *   access_token (每次访问不是唯一的，存在有效期2小时)
 *
 * 疑问：如果注销掉微信登陆用户？暂时没有发现，但是可以采用重新授权登陆来解决！每次重新登陆都是全新的过程。
 *
 */

public class WXLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,WXLoginActivity.class);
        activity.startActivity(intent);
    }

    public SharedPreferencesUtils mCacheWxDataUtils;

    private ImageView ivHead;

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login);

        mCacheWxDataUtils = new SharedPreferencesUtils(this,"cache_wx_data");

        ivHead = (ImageView)findViewById(R.id.iv_wx_head);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);

        if (!api.isWXAppInstalled()){
            Toast.makeText(this, "没有安装微信客户端!", Toast.LENGTH_SHORT).show();
            return;
        }

        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api.sendReq(req);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(WXLoginActivity.this).load(App.getShared().getString("headUrl","")).into(ivHead);
        ((TextView)findViewById(R.id.iv_wx_nickname)).setText(App.getShared().getString("nickName",""));
    }

   /* @Override  //内部是通过startActivity方式启动的，不会回调到该方法上
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 0){
            String headUrl = data.getStringExtra("headUrl");
            ViseLog.d("url:"+headUrl);
            Glide.with(WXLoginActivity.this).load(headUrl).into(ivHead);

//            String nickname = data.getStringExtra("nickName");
//            ((TextView)findViewById(R.id.iv_wx_nickname)).setText(nickname);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /**
     * 刷新方法
     * 如果access_token过期则刷新，刷新后重新获取用户信息。
     * 如果refresh_token过期，则提示重新授权。
     * @param view
     */
    public void updateTokenAndUserinfoClick(View view) {
        reUpdateUserInfo();
    }

    /**
     * 此方法用于清除缓存的登陆信息
     * @param view
     */
    public void clearCacheClick(View view) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(this,"cache_wx_data");
        sharedPreferencesUtils.clear();

        //将应用的appid注销到微信
        api.unregisterApp();
        //api = null;
    }

    /**
     * 重新更新用户信息
     */
    public void reUpdateUserInfo(){
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(this,"cache_wx_data");
        //判断access_token是否存在
        final String access_token = sharedPreferencesUtils.getString(WXEntryActivity.ACCESS_TOKEN,"");
        final String open_id = sharedPreferencesUtils.getString(WXEntryActivity.OPEN_ID,"");
        final String refresh_token = sharedPreferencesUtils.getString(WXEntryActivity.REFRESH_TOKEN,"");

        if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(open_id) && !TextUtils.isEmpty(refresh_token)){
            ViseLog.d("缓存都存在！ token="+ access_token + ",openid=" + open_id +",refresh_token=" + refresh_token);

            //判断是否accessToken过期了
            //请求获取access_token
            OkHttpUtils.get().url("https://api.weixin.qq.com/sns/auth")
                    .addParams("access_token",access_token)//access_token=ACCESS_TOKEN&openid=OPENID
                    .addParams("openid",open_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            ViseLog.d("判断access_token状态是否请求错误..");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            ViseLog.d("判断access_token状态：response:"+response);

                            //{"errcode":0,"errmsg":"ok"}
                            JSONObject jsonObject = (JSONObject) JSON.parse(response);
                            int errcode = jsonObject.getInteger("errcode");
                            String ok = jsonObject.getString("errmsg");

                            if ("ok".equals(ok) && errcode == 0){
                                ViseLog.d("当前token没有过期！");

                                //证明没有过期，直接使用token，去获取获取用户信息
                                getUserInfo(access_token,open_id);

                            }else{//token过期，刷新或延长过期时间
                                //请求获取access_token
                                OkHttpUtils.get().url("https://api.weixin.qq.com/sns/oauth2/refresh_token")
                                        .addParams("appid",Config.APP_ID_WX)
                                        .addParams("grant_type","authorization_code")
                                        .addParams("refresh_token",refresh_token)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(okhttp3.Call call, Exception e, int id) {
                                                ViseLog.d("刷新请求错误..");
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                ViseLog.d("刷新请求response:"+response);

                                                //{"errcode":40030,"errmsg":"invalid refresh_token"}
                                                JSONObject jsonObject = (JSONObject) JSON.parse(response);
                                                int errcode = jsonObject.getInteger("errcode");
                                                String ok = jsonObject.getString("errmsg");
                                                if (errcode == 40030){
                                                    Toast.makeText(WXLoginActivity.this,"刷新过期了，重新授权吧！",Toast.LENGTH_LONG).show();
                                                    finish();
                                                    return;
                                                }

                                                        /*{
                                                            "access_token":"ACCESS_TOKEN",
                                                                "expires_in":7200,
                                                                "refresh_token":"REFRESH_TOKEN",
                                                                "openid":"OPENID",
                                                                "scope":"SCOPE"
                                                        }*/

                                                WxRefresh wxRefresh = JSON.parseObject(response, WxRefresh.class);
                                                String newAccessToken = wxRefresh.getAccess_token();
                                                String newRefreshToken = wxRefresh.getRefresh_token();
                                                String newOpenId = wxRefresh.getOpenid();

                                                ViseLog.d("当前AccessToken="+newAccessToken);
                                                ViseLog.d("当前Refreshtoken="+newRefreshToken);
                                                ViseLog.d("当前OpenId="+newOpenId);

                                                //缓存access_token
                                                mCacheWxDataUtils.putString(WXEntryActivity.ACCESS_TOKEN,newAccessToken);
                                                mCacheWxDataUtils.putString(WXEntryActivity.OPEN_ID,newOpenId);
                                                mCacheWxDataUtils.putString(WXEntryActivity.REFRESH_TOKEN,newRefreshToken);

                                                //获取用户信息
                                                getUserInfo(newAccessToken,newOpenId);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    /**
     * 获取个人信息
     //     * @param accessTokenEntity
     */
    private void getUserInfo(/*WXAccessTokenEntity accessTokenEntity*/ String accessToken,String openId) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        OkHttpUtils.get()
                .url("https://api.weixin.qq.com/sns/userinfo")
                .addParams("access_token",/*accessTokenEntity.getAccess_token()*/accessToken)
                .addParams("openid",/*accessTokenEntity.getOpenid()*/ openId)//openid:授权用户唯一标识
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        ViseLog.d("获取错误..");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ViseLog.d("userInfo:"+response);

                        WXUserInfo wxResponse = JSON.parseObject(response,WXUserInfo.class);
                        ViseLog.d("微信登录资料已获取，后续未完成");

                        String headUrl = wxResponse.getHeadimgurl();
                        ViseLog.d("头像Url:"+headUrl);
                        App.getShared().putString("headUrl",headUrl);
                    }
                });
    }
}

