package com.uday.mvp_recyclerview_demo.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.uday.mvp_recyclerview_demo.R;
import com.uday.mvp_recyclerview_demo.adapter.CountryFactsAdapter;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.presenter.MainPresenter;
import com.uday.mvp_recyclerview_demo.presenter.MainViewInterface;
import com.uday.mvp_recyclerview_demo.utils.MyDividerItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FactsFragment extends Fragment implements MainViewInterface, SwipeRefreshLayout.OnRefreshListener {
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
                mainPresenter.getFacts();
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

        mainPresenter.getFacts();

    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        // Fetching data from server
        mainPresenter.getFacts();
    }
    @Override
    public void showToast(String str) {
        Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void displayFacts(Country country) {
        adapter = new CountryFactsAdapter(Arrays.asList(country.getRows()), getActivity().getApplicationContext());
        rvFacts.setAdapter(adapter);
        mCallback.onCountrySelected(country.getTitle());
    }

    @Override
    public void displayError(String e) {

        showToast(e);

    }

    private void updateAdapter(@Nullable Country country) {
        adapter = new CountryFactsAdapter(Arrays.asList(country.getRows()), getActivity().getApplicationContext());
        rvFacts.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}