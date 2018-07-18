package com.uday.mvp_recyclerview_demo.network;


import android.support.v4.util.LruCache;

import com.uday.mvp_recyclerview_demo.app.MyApplication;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class NetworkService {

    @Inject
    Retrofit retrofit;
    private Api networkAPI;
    private LruCache<Class<?>, Observable<?>> apiObservables;
    private static NetworkService networkService_single_instance = null;

    // private constructor restricted to this class itself
    private NetworkService()
    {
        MyApplication.getNetComponent().inject(this);
        apiObservables = new LruCache<>(10);
        networkAPI = retrofit.create(Api.class);
    }

    // static method to create instance of Singleton class
    public static NetworkService getInstance()
    {
        if (networkService_single_instance == null)
            networkService_single_instance = new NetworkService();

        return networkService_single_instance;
    }


    /**
     * Method to return the API interface.
     * @return
     */
    public Api getAPI(){
        return  networkAPI;
    }



    /**
     * Method to clear the entire cache of observables
     */
    public void clearCache(){
        apiObservables.evictAll();
    }


    /**
     * Method to either return a cached observable or prepare a new one.
     *
     * @param unPreparedObservable
     * @param clazz
     * @param cacheObservable
     * @param useCache
     * @return Observable ready to be subscribed to
     */
    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable, Class<?> clazz, boolean cacheObservable, boolean useCache){

        Observable<?> preparedObservable = null;

        if(useCache)//this way we don't reset anything in the cache if this is the only instance of us not wanting to use it.
            preparedObservable = apiObservables.get(clazz);

        if(preparedObservable!=null)
            return preparedObservable;



        //we are here because we have never created this observable before or we didn't want to use the cache...

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if(cacheObservable){
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }


        return preparedObservable;
    }



}
