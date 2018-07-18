package com.uday.mvp_recyclerview_demo.app;

import com.uday.mvp_recyclerview_demo.di.ApiModule;
import com.uday.mvp_recyclerview_demo.di.AppModule;
import com.uday.mvp_recyclerview_demo.network.NetworkService;
import javax.inject.Singleton;
import dagger.Component;



@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {
    void inject(NetworkService networkService);

}
