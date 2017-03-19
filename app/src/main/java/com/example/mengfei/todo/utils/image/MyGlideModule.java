package com.example.mengfei.todo.utils.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Glide的默认配置文件
 * Created by mengfei on 2017/3/18.
 */
public class MyGlideModule implements GlideModule {

    private static final int DISK_LRU_CACHE_SIZE = 80 * 1024;
    private static final int THREAD_COUNT = 4;
    private static final int CONNECTION_TIMEOUT = 5 * 1000;//超时时间
    private static final int READ_TIME_OUT = 30 * 1000;


    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, DISK_LRU_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
