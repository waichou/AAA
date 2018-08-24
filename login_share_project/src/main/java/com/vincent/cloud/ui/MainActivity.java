package com.vincent.cloud.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vincent.cloud.R;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_qq_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QQLoginActivity.actionStart(MainActivity.this);
            }
        });
        findViewById(R.id.btn_wx_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXLoginActivity.actionStart(MainActivity.this);
            }
        });
        findViewById(R.id.btn_sina_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SinaLoginActivity.actionStart(MainActivity.this);
            }
        });
        findViewById(R.id.btn_common_shared).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedActivity.actionStart(MainActivity.this);
            }
        });
    }
}
