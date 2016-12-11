package com.zhotkit.justin.zhotkit.app;

import android.app.Application;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        //File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "NetViewer/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheSize(50 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        mInstance = this;
    }

    public  static synchronized AppController getmInstance() {
        return mInstance;
    }
}

