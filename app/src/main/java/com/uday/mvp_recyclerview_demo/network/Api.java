package com.uday.mvp_recyclerview_demo.network;

import com.uday.mvp_recyclerview_demo.model.Country;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface Api {

    @GET("facts.json")
    Observable<Country> getCountryFacts();
}