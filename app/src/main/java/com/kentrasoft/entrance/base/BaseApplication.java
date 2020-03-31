package com.kentrasoft.entrance.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;
import com.kentrasoft.entrance.base.callback.ActivityLifecycleCallbacksImpl;
import com.kentrasoft.entrance.base.net.RetrofitManager;
import com.kentrasoft.entrance.base.utils.dialogtiplib.util.AppUtils;

import me.jessyan.autosize.AutoSizeConfig;


/**
 * created date: 2020/2/27 on 17:42
 * des:
 * author: HJW HP
 */
public class BaseApplication extends Application {
    //全局唯一的context
    private static BaseApplication application;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.getInstance().init();
        //监听全局activity生命周期
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
        AppUtils.init(this);
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }

    /**
     * 获取全局唯一上下文
     *
     * @return BaseApplication
     */
    public static BaseApplication getApplication() {
        return application;
    }


    /**
     * 退出应用
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
