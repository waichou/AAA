package com.glide.module;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by zhouwei on 2018/8/16.
 * 更改磁盘缓存路径 : https://www.jb51.net/article/83156.htm
 * 默认的磁盘缓存是在： data/data/应用包名/cache/image_manager_disk_cache
 * 参考：https://blog.csdn.net/hqfok/article/details/53284180 glide 之 BitmapPool 详解
 *       https://www.cnblogs.com/whoislcj/p/5565012.html  glide的使用
         https://blog.csdn.net/wyb112233/article/details/52337392 浅谈Glide加载图片的框架和封装
 */
public class GlideModelConfig implements GlideModule {

    int diskSize = 1024 * 1024 * 250;//磁盘缓存空间，如果不设置，默认是：250 * 1024 * 1024 即250MB
    int memorySize = (int) (Runtime.getRuntime().maxMemory()) / 4;  // 取1/4最大内存作为最大缓存

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));  //手机磁盘
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR, diskSize)); //sd卡磁盘

        // 默认内存和图片池大小
      /*MemorySizeCalculator calculator = new MemorySizeCalculator(context);
      int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
      int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
      builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize)); // 该两句无需设置，是默认的
      builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));*/

        // 设置内部缓存空间大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        //设置BitmapPool缓存内存大小。避免频繁的创建以及回收Bitmap对象，进而减少GC的出现，可以使用BitmapPool统一的管理Bitmap的创建以及重用。
        builder.setBitmapPool(new LruBitmapPool(memorySize));

        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
