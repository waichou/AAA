package com.framworks.base.example.toolbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.utils_moudle.R;

public class ToolbarActivity extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5_0);

        mToolbar = (Toolbar)findViewById(R.id.toobar_id);
        if (getSupportActionBar() == null)
            LogUtils.d("toolbar have not exit to activity");
        //如果你用不到ActionBar的一些特性的话，建议setSupportActionBar(toolbar); 这行代码不用添加了。
        setSupportActionBar(mToolbar);//如果设定了setNavigationOnClickListener需要在它之前设置setSupportActionBar

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("click navigation btn!");
            }
        });
        //添加溢出菜单:注意注意加载的时候不能 setSupportActionBar
        //mToolbar.inflateMenu(R.menu.activity_main_drawer);
        // 添加菜单点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_discover:
                        ToastUtils.showShort("discover");
                        break;

                    case R.id.nav_login:
                        ToastUtils.showShort("login");
                        break;
                }
                return false;
            }
        });
    }

    //设置menu（右边图标）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu); //解析menu布局文件到menu
        return true;
    }

}
