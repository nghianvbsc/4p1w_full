package com.fourpicsinword;

import android.app.Application;
import android.content.Context;

import com.fourpicsinword.module.ImageLoaderConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ucweb.union.ads.mediation.MediationSdk;

/**
 * Created by NGHIA_IT on 12/21/2015
 */
public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MediationSdk.start(getApplicationContext());
        initImageLoader(getApplicationContext());

    }

    public void initImageLoader(Context context) {


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB

                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .imageDownloader(new ImageLoaderConfig(context))
                .build();

        ImageLoader.getInstance().init(config);

    }
}
