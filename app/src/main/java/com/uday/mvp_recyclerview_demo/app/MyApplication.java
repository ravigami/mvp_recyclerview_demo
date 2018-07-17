package com.uday.mvp_recyclerview_demo.app;

import android.app.Application;

import com.uday.mvp_recyclerview_demo.constant.Constant;
import com.uday.mvp_recyclerview_demo.di.ApiModule;
import com.uday.mvp_recyclerview_demo.di.AppModule;
import com.uday.mvp_recyclerview_demo.network.ConnectivityReceiver;


public class MyApplication extends Application {

    private static ApiComponent mApiComponent;
    private static MyApplication mInstance;


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constant.BASE_URL))
                .build();

    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public static ApiComponent getNetComponent() {
        return mApiComponent;
    }
}