package com.test.add_root_view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.aaa.R;

public class AddRootViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_root_view);

        final View btn = this.findViewById(R.id.add_test_btn_id);
        btn.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("onCreate btn大小：" + btn.getWidth() + "," + btn.getHeight());
            }
        });


    }

    /**
     * Activity已经彻底运行起来时回调
     * 注意：由于setContentView的根布局是Framelayout,故动态添加的view是该根布局的子试图且覆盖setContentView设置的试图
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final View root = this.findViewById(android.R.id.content);
        if (root instanceof FrameLayout) {//decorview 子LinearLayout内部的的第二个子试图FrameLayout
            FrameLayout content = (FrameLayout) root;

            //创建栈试图的图标
            final TextView stackView = new TextView(this);
//            stackView.setImageResource(android.R.drawable.ic_menu_add);
            stackView.setText("你好，Java&Android");
            //设定LayoutParams
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            //通过系统内置的尺寸转换器，将18dp转换成对应的px值
            final int dp18 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, this.getResources().getDisplayMetrics());
            params.topMargin = dp18 * 7;
            params.rightMargin = dp18;
            //imageview 接收 LayoutParams
            stackView.setLayoutParams(params);
            //最终将imageview添加到content窗体中
            content.addView(stackView);
        }

        final View btn = this.findViewById(R.id.add_test_btn_id);
        btn.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("onpostCreate btn大小：" + btn.getWidth() + "," + btn.getHeight());
            }
        });
    }

    public void testClick(View view) {
        ViewGroup decorView = (ViewGroup) findViewById(R.id.decor_root_layout_id).getParent().getParent().getParent();

        if (decorView instanceof  FrameLayout) {
            ViewGroup group =(ViewGroup)getWindow().getDecorView();
            if (decorView == group){
                System.out.println("decorview的大小：" + decorView.getWidth() + "," + decorView.getHeight());
            }
        }

        //这是setContentView的内容
        View root = this.findViewById(android.R.id.content);
        System.out.println("decorview的content大小：" + root.getWidth() + "," + root.getHeight());
        root.setBackgroundColor(Color.RED);
        root.invalidate();


        WindowManager wm = this.getWindowManager();

        Point point=new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;

        System.out.println("screen width & height = "+ width +"," + height);

        //
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }

        System.out.println("状态栏高度之一：" + result);


        int[] statusBarHeight = getStatusBarHeight(this);
        System.out.println("状态栏高度之二：" + statusBarHeight[0]  + "," + statusBarHeight[1]);


        //----------->>

        System.out.println("-------------一下为调用getRootView------------>>>");

        Button btn = findViewById(R.id.add_test_btn_id);

        ViewGroup dView = (ViewGroup) findViewById(R.id.decor_root_layout_id).getParent().getParent().getParent();
        System.out.println("decorView =" + dView.getClass().getSimpleName());

        ViewGroup dLinearLayout = (ViewGroup) findViewById(R.id.decor_root_layout_id).getParent().getParent();
        System.out.println("dLinearLayout =" + dLinearLayout.getClass().getSimpleName() + "," + dLinearLayout);

        ViewGroup dFragLayout = (ViewGroup) findViewById(R.id.decor_root_layout_id).getParent();
        System.out.println("dFragLayout =" + dFragLayout.getClass().getSimpleName() + "," +dFragLayout);

        ViewGroup dRootLinearLayout = (ViewGroup) findViewById(R.id.decor_root_layout_id);
        System.out.println("dRootLinearLayout =" + dRootLinearLayout.getClass().getSimpleName() + "," + dRootLinearLayout);

//        2当view处于xml的非根节点时，通过getParent获得的是view的父亲节点。
        System.out.println("getRootView ----非根节点--->>>+" + btn.getRootView().getClass().getSimpleName()+ ","+ btn.getRootView() +"<<-----");
        System.out.println("getRootView ----非根节点--->>>+" + btn.getParent().getClass().getSimpleName()+ ","+ btn.getParent() +"<<-----");

//        1.当view处于xml文件的根节点时，通过getParent到的view都是它身。
        System.out.println("getRootView ----根节点>>>+" + dRootLinearLayout.getRootView().getClass().getSimpleName()+ ","+ dRootLinearLayout.getRootView() +"<<-----");
        System.out.println("getParent ----根节点>>>+" + dRootLinearLayout.getParent().getClass().getSimpleName()+ ","+ dRootLinearLayout.getParent() +"<<-----");
    }

    /**
     * 获取状态栏+标题栏的高度
     *
     * @param context
     * @return int[]：下标0为状态拦高度，下标1为标题栏高度
     */
    public int[] getStatusBarHeight(Context context) {
        int[] result = new int[2];
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        result[0] = statusBarHeight;

        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentTop - statusBarHeight;
        result[1] = titleBarHeight;
        return result;
    }
}
