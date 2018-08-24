package com.utils_moudle.app;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.utils_moudle.R;
import com.utils_moudle.helper.DialogHelper;

import java.util.List;

/**
 * 注意的是：onRequestPermissionsResult 多个权限授权时仅仅会执行一次回调
 */
public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        imageView = (ImageView) findViewById(R.id.show_draw_img_id);

    }

    /**
     * 动态申请权限
     * @param view
     */
    public void requestPerssimionClick(View view) {
        PermissionUtils.permission(PermissionConstants.CAMERA,PermissionConstants.STORAGE,PermissionConstants.PHONE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        LogUtils.d(permissionsGranted);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog();
                        }
                        LogUtils.d(permissionsDeniedForever, permissionsDenied);

                    }
                })
                .theme(new PermissionUtils.ThemeCallback() {
                    @Override
                    public void onActivityCreate(Activity activity) {
                        //在接收到开始授权信息的时候，回调执行全屏显示
                        ScreenUtils.setFullScreen(activity);
                    }
                })
                .request();

    }

    public void printLogClick(View view) {

        LogUtils.d("原图形大小："+imageView.getWidth() + ","+imageView.getHeight()+"，边框大小：16，请问现在图形多大：接近320px?" + imageView.getWidth() + ",height="+ imageView.getHeight());

    }

    public void drawClick(View view) {
        LogUtils.d("原图形大小："+imageView.getMeasuredWidth() + ","+imageView.getHeight());

        imageView.setImageBitmap(ImageUtils.addCornerBorder(ImageUtils.getBitmap(R.mipmap.img_lena), 100, Color.GREEN, 50));
    }
}
