package com.uday.mvp_recyclerview_demo.presenter;


import com.uday.mvp_recyclerview_demo.model.Country;

public interface MainViewInterface {

    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayFacts(Country country);
    void displayError(String s);
}
