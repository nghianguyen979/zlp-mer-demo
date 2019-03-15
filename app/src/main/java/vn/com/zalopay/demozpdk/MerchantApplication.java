package vn.com.zalopay.demozpdk;

import android.app.Application;

import vn.zalopay.sdk.ZaloPaySDK;

public class MerchantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZaloPaySDK.getInstance().initWithAppId(3);
    }
}
