package com.test.merge;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhouwei.aaa.R;

public class MergeActivity extends Activity{

    View mIncludeRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);

        //通过include获取到的View作为父布局直接操作本身Layout或者并间接获取引用的子试图view
       /* mIncludeRootView = findViewById(R.id.include_id);
        if (mIncludeRootView instanceof RelativeLayout){
            mIncludeRootView.setBackgroundColor(Color.BLUE);
            //通过获取到的Include引用的根布局来获取间接获取子试图
            Button viewBtn1 = mIncludeRootView.findViewById(R.id.incule_btn_id_1);
            viewBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MergeActivity.this,"btn1",Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        //直接通过访问被引用的布局会不会报错?会报错！注意：在使用为include定义id时会报错！故此种情况不要为include设置id
//        final View rootLayotId = findViewById(R.id.incule_root_layout_id);
//
//        rootLayotId.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        //内部的子布局可通过父布局试图间接获取到
//        Button viewBtn1 = rootLayotId.findViewById(R.id.incule_btn_id_1);
//        viewBtn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MergeActivity.this,"btn1",Toast.LENGTH_SHORT).show();
//            }
//        });

        //发现include不设置id，被引用的父试图可以直接通过id获取，那么子试图呢？可以直接获取
//        Button viewBtn1 = findViewById(R.id.incule_btn_id_1);
//        viewBtn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MergeActivity.this,"btn1",Toast.LENGTH_SHORT).show();
//            }
//        });


        //注：1.通过以上测试发现，如果include复写了id，则不能正常获取到被引用布局的根布局
        //2.即使include的复写了id，但是仍然可以直接findViewById去获取被引用布局中的子试图，不管include复写还是不复写，子试图直接正常find就行
        // 父试图不能直接获取，仅能通过include的复写id获取

    }

    public void show_viewstub_bnt_id(View view) {
        ViewStub viewStub = findViewById(R.id.viewstub);
        viewStub.inflate();

        Button viewBtn1 = findViewById(R.id.incule_btn_id_1);

        viewBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MergeActivity.this,"btn1",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
