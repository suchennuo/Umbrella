package com.umbrella.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.BuildConfig;
import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Zhang.Y.C on 2017/4/10.
 */
@DefaultLifeCycle(application = ".BaseUmbrellaApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)

public class UmbrellaApplication extends DefaultApplicationLike {

    private static final String tinkerAppKey = "6eda7b6cae3821ed";
    private ApplicationLike tinkerApplicationLike;

    public UmbrellaApplication(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                               long applicationStartElapsedTime, long applicationStartMillisTime,
                               Intent tinkerResultIntent){
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setSharePlatformConfig();
    }

    /**
     * 相当于 Application 中的 onCreate() 方法，一些初始化操作可放在此处完成。
     * @param context
     */
    @Override
    public void onBaseContextAttached(Context context){
        super.onBaseContextAttached(context);
        MultiDex.install(context);
        //初始化 Tinker
        TinkerInstaller.install(this);
    }

    private void setSharePlatformConfig() {
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
}
