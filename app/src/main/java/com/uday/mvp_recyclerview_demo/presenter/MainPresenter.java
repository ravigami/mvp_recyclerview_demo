package com.uday.mvp_recyclerview_demo.presenter;


import android.util.Log;

import com.uday.mvp_recyclerview_demo.app.MyApplication;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.network.Api;

import java.net.NetworkInterface;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;


public class MainPresenter implements MainPresenterInterface {
    @Inject
    Retrofit retrofit;
    MainViewInterface mvi;
    private String TAG = "MainPresenter";

    public MainPresenter(MainViewInterface mvi) {
        MyApplication.getNetComponent().inject(this);
        this.mvi = mvi;
    }

    @Override
    public void getFacts() {
        mvi.showProgressBar();
        getObservable().subscribeWith(getObserver());
    }

    public Observable<Country> getObservable(){

        return retrofit.create(Api.class)
                            .getCountryFacts()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<Country> getObserver(){
        return new DisposableObserver<Country>() {

            @Override
            public void onNext(@NonNull Country country) {
                mvi.displayFacts(country);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mvi.displayError("Error fetching Facts Data");
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }
}
