package com.umbrella.application;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Zhang.Y.C on 2017/4/10.
 */

public class UmbrellaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        UMShareAPI.get(this);

        setSharePlatformConfig();
    }

    private void setSharePlatformConfig() {
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
}
