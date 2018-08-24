package com.glide.module;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 *  经测试发现：
 *  1.在没有网络的情况请求图片，一旦在联网的情况下，自动显示出来
 *  2.缓存方式：
 *
 */
public class MainActivity extends AppCompatActivity {


    @Bind(R.id.load_network_image_id)
    ImageView loadNetworkImageId;

    @Bind(R.id.load_local_image_id)
    ImageView loadLocalImageId;

    @Bind(R.id.load_resource_image_id)
    ImageView loadResourceImageId;

    @Bind(R.id.load_imputstrem_image_id)
    ImageView loadImputstremImageId;

    @Bind(R.id.load_uri_image_id)
    ImageView loadUriImageId;

    @Bind(R.id.load_asset_image_id)
    ImageView loadAssetImageId;

    @Bind(R.id.load_raw_image_id)
    ImageView loadRawImageId;
    @Bind(R.id.load_placeholder_image_id)
    ImageView loadPlaceholderImageId;

    //图形变换部分
    @Bind(R.id.load_croptransform_image0_id)
    ImageView loadCroptransformImage0Id;

    @Bind(R.id.load_croptransform_image1_id)
    ImageView loadCroptransformImage1Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    public void getNetworkImgClick(View view) {
        //获取网络图片
        Glide.with(this).load("https://www.baidu.com/img/dong_f614aae151545e62b42a08bd9c07a144.gif").asGif().into(loadNetworkImageId);

        //获取本地SD卡图片
        Glide.with(this).load("file://" + Environment.getExternalStorageDirectory().getPath() + "/tempImage.jpg").into(loadLocalImageId);

        //获取res卡图片
        Glide.with(this).load("android.resource://" + getPackageName() + "/drawable/" + R.mipmap.no_active_img).into(loadResourceImageId);

        //获取inputstream图片
        File sdFile = new File(Environment.getExternalStorageDirectory().getPath() + "/glide_test_inputstream.txt");
        String imgStr = readDataFromFile(sdFile.getAbsolutePath());

        byte[] decodeImg = Base64.decode(imgStr, Base64.DEFAULT);
        Glide.with(this).load(decodeImg).into(loadImputstremImageId);

        //获取uri图片
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.no_active_img);
        Glide.with(this).load(uri).into(loadUriImageId);

        //获取asset图片
        String assetImg = "file:///android_asset/no_active_img.gif";
        Glide.with(this).load(assetImg).into(loadAssetImageId);

        //获取raw图片
        String rawImg = "android.resource://" + getPackageName() + "/raw/" + R.raw.no_active_img;
        Glide.with(this).load(rawImg).into(loadRawImageId);
    }


    /**
     * 读取文件内容
     *
     * @param filePath
     * @return
     */
    public static String readDataFromFile(String filePath) {
        if (!new File(filePath).exists()) return null;

        ByteArrayOutputStream baos = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(filePath);
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readLen = 0;
            while ((readLen = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, readLen);
            }
            return new String(baos.toByteArray());

        } catch (IOException e) {
            return "";
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
            }
        }
    }

    //测试占位图
    public void testPlaceholder(View view) {
        //获取网络图片
        Glide.with(this)
                .load("https://www.baidu.com/img/dong_f614aae151545e62b42a08bd9c07a144.gif")
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓存
                .error(R.mipmap.error)
                .placeholder(R.mipmap.udefault)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        System.out.println("发生错误");
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "发生错误：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "加载成功！", Toast.LENGTH_SHORT).show();
                        //注意：做其他处理
//                        loadPlaceholderImageId.setBackgroundResource(R.mipmap.no_active_img);
                        return false;
                    }
                })
                .into(loadPlaceholderImageId);//不能去掉这个into()否则不能正常显示图片

    }


    /**
     * 图形变换 官方地址：https://github.com/wasabeef/glide-transformations
     *
     * @param view
     */
    public void testTransformImg(View view) {
        Glide.with(this)
                .load("http://img.mukewang.com//55237dcc0001128c06000338.jpg")
                .bitmapTransform(new CropCircleTransformation(this))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(loadCroptransformImage0Id);

    }

    /**
     * 清理缓存的方式，需要在后台线程中执行
     * 否则报错：Caused by: java.lang.IllegalArgumentException: YOu must call this method on a background thread
     * //清理磁盘缓存 需要在子线程中执行
     * //清理内存缓存 可以在UI主线程中进行
     * @param view
     */
    public void clearCacheClick(View view) {
        GlideCacheUtils.getInstance().clearImageAllCache(this);

    }

    //加载原型图片
    public void loadCircleImageClick(View view) {
        GlideUtils.getInstance().loadCircleImage(this,loadCroptransformImage1Id,"http://img.mukewang.com//55237dcc0001128c06000338.jpg");
    }
}
