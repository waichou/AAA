package com.vincent.cloud.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.utils.ImageUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.vincent.cloud.R;
import com.vincent.cloud.base.Config;
import com.vincent.cloud.entity.QQUser;
import com.vise.log.ViseLog;

import org.json.JSONException;


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
 * 疑问：1.过期的条件是什么？
 *        通过清理工具清理后就会过期，对于退出当前应用才不会过期
 *
 *      2.过期之后，再登陆 access_token，openid 哪个会改变？
 *        2.1 不同的用户对应的 openid access_token 都是不一样的，同一个用户这两个参数是不变的。
 *        2.2 token openid 等值都是没有缓存到本地的
 *
 * 小结.1.没有过期重复登陆需要判断是否登陆过：mTencent.isSessionValid  true表示没有过期，false表示过期了
 *      2.首次登陆获取到 access_token 以及 通过access_token获取到的相关用户信息后，提交注册到Web后台，
 *        Web后台动态生成唯一UID或以access_token 可以作为UID 进行注册，再自动登陆。
 *
 *        下次授权时，仍然调用Web后台接口进行登陆注册判断，有对应的用户，则返回相应的反馈结果！
 *
 *  --------------------------------->>>> 22810****
    "openid":"017CCB35793433586D17A435895BDCD2",
    "access_token":"ADB1A0D6A5E49870DC2F2F79B4885435"

    --------------------------------->>>> 22810*****
    "openid":"017CCB35793433586D17A435895BDCD2",
    "access_token":"ADB1A0D6A5E49870DC2F2F79B4885435"


    --------------------------------->>>> 1935*****
    "openid":"1C5A1859F71675951A623EA39086D26E",
    "access_token":"144DA1ECF080177AAD2CEF3395DAF119"

 */

public class QQLoginActivity extends AppCompatActivity {

    private Tencent mTencent;

    private UserInfo userInfo;

    private BaseUiListener listener = new BaseUiListener();

    private String QQ_uid;//qq_openid

    private ImageView ivHead;

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,QQLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_login);

        ivHead = (ImageView)findViewById(R.id.iv);
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, this.getApplicationContext());
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViseLog.d("开始QQ登录..");
                ViseLog.d("是否Session过期=" + mTencent.isSessionValid());//true表示没有过期，false表示过期了
                if (!mTencent.isSessionValid())
                {
                    //注销登录 mTencent.logout(this);
                    mTencent.login(QQLoginActivity.this, "all", listener);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = JSONObject.parseObject(String.valueOf(msg.obj));
                ViseLog.d("UserInfo:"+ JSON.toJSONString(response));
                QQUser user=JSONObject.parseObject(response.toJSONString(),QQUser.class);
                if (user!=null) {
                    ViseLog.d("userInfo:昵称："+user.getNickname()+"  性别:"+user.getGender()+"  地址："+user.getProvince()+user.getCity());
                    ViseLog.d("头像路径："+user.getFigureurl_qq_2());
                    Glide.with(QQLoginActivity.this).load(user.getFigureurl_qq_2()).into(ivHead);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }

    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            ViseLog.d("授权:"+o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            ViseLog.d("onError:code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }
        @Override
        public void onCancel() {
            ViseLog.d("onCancel");
        }
    }

    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     * @param jsonObject
     */
    public void initOpenidAndToken(org.json.JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                QQ_uid = openId;
            }
        } catch(Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    ViseLog.e("................"+response.toString());
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                @Override
                public void onCancel() {
                    ViseLog.d("登录取消..");
                }
            };
            userInfo = new UserInfo(this, mTencent.getQQToken());
            userInfo.getUserInfo(listener);
        }
    }

    public void qqLoginOutClick(View view) {
        ViseLog.d("注销：是否Session过期=" + mTencent.isSessionValid());
        qq_loginout();
    }

    public void qq_loginout() {
        if (!mTencent.isSessionValid()){//true表示没有过期，false表示过期了
            Toast.makeText(this,"session 是失效的，不用注销！",Toast.LENGTH_SHORT).show();
            return;
        }

        if (mTencent == null) {
            mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, getApplicationContext());
        }
        mTencent.logout(this);
    }

}
