package com.uday.mvp_recyclerview_demo.presenter;


import com.uday.mvp_recyclerview_demo.constant.Constant;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.network.NetworkService;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


public class MainPresenter implements MainPresenterInterface {

    MainViewInterface mvi;
    private NetworkService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MainPresenter(MainViewInterface mvi,  NetworkService service) {
        this.mvi = mvi;
        this.service = service;
    }
    @Override
    public void getFacts() {
        mvi.showProgressBar();
        compositeDisposable.add(getObservable().subscribeWith(getObserver()));
    }

    public Observable<Country> getObservable(){

        Observable<Country> factResponseObservable = (Observable<Country>)
                service.getPreparedObservable(service.getAPI().getCountryFacts(), Country.class, true, false);
        return factResponseObservable;
    }
    public DisposableObserver<Country> getObserver(){
        return new DisposableObserver<Country>() {

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Country country) {
                mvi.displayFacts(country);

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                mvi.displayError(Constant.ERROR_MSG);
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }

    @Override
    public void  decomposeObservable(){
        compositeDisposable.clear();
    }
}
