package com.uday.mvp_recyclerview_demo.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uday.mvp_recyclerview_demo.R;
import com.uday.mvp_recyclerview_demo.adapter.CountryFactsAdapter;
import com.uday.mvp_recyclerview_demo.app.MyApplication;
import com.uday.mvp_recyclerview_demo.constant.Constant;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.network.ConnectivityReceiver;
import com.uday.mvp_recyclerview_demo.presenter.MainPresenter;
import com.uday.mvp_recyclerview_demo.presenter.MainViewInterface;
import com.uday.mvp_recyclerview_demo.utils.MyDividerItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FactsFragment extends Fragment implements MainViewInterface, SwipeRefreshLayout.OnRefreshListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private CountrySelectedListener mCallback;
    MainPresenter mainPresenter;
    private CountryFactsAdapter adapter;
    @BindView(R.id.rvFacts)
    RecyclerView rvFacts;
    @BindView(R.id.imgnointernet)
    ImageView imgNoInternet;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;

    // Container Activity must implement this interface
    public interface CountrySelectedListener {
        public void onCountrySelected(String Country);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_factlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        setupMVP();
        return view;
    }
    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void initView(View view){
        rvFacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFacts.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeContainer.post(new Runnable() {

            @Override
            public void run() {
                // Fetching data from server
                getFacts();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a = null;

        if (context instanceof Activity) {
            a = (Activity) context;
        }
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (CountrySelectedListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    private void getFacts() {
       if(isNetworkAvailable()) {
           showRecyclerView();
           mainPresenter.getFacts();
       } else {
           showSnack(Constant.INTERNET_NOT_CONNECTED);
           showNoConnection();
       }

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        // Fetching data from server
        getFacts();
    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void showMsg(String str) {
        showSnack(str);
    }
    // Showing the status in Snackbar
    private void showSnack(String Msg) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(android.R.id.content), Msg, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();

    }
    @Override
    public void showProgressBar() {
        swipeContainer.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void displayFacts(Country country) {
        updateAdapter(country);
    }

    @Override
    public void displayError(String e) {
        swipeContainer.setRefreshing(false);
        showMsg(e);

    }

    private void updateAdapter(@Nullable Country country) {
        adapter = new CountryFactsAdapter(Arrays.asList(country.getRows()), getActivity().getApplicationContext());
        rvFacts.setAdapter(adapter);
        mCallback.onCountrySelected(country.getTitle());
    }
    private void showNoConnection() {
        imgNoInternet.setVisibility(View.VISIBLE);
        rvFacts.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
    }
    private void showRecyclerView() {
        imgNoInternet.setVisibility(View.GONE);
        rvFacts.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        return ConnectivityReceiver.isConnected();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //showSnack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}