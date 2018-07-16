package com.uday.mvp_recyclerview_demo.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;

import com.uday.mvp_recyclerview_demo.R;
import com.uday.mvp_recyclerview_demo.adapter.CountryFactsAdapter;
import com.uday.mvp_recyclerview_demo.model.Country;
import com.uday.mvp_recyclerview_demo.presenter.MainPresenter;
import com.uday.mvp_recyclerview_demo.presenter.MainViewInterface;
import com.uday.mvp_recyclerview_demo.ui.activity.MainActivity;
import com.uday.mvp_recyclerview_demo.utils.MyDividerItemDecoration;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.Unbinder;

public class FactsFragment extends Fragment implements MainViewInterface {

    MainPresenter mainPresenter;
    private CountryFactsAdapter adapter;
    @BindView(R.id.rvFacts)
    RecyclerView rvFacts;
    @BindView(R.id.imgnointernet)
    ImageView imgNoInternet;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_factlist, container, false);
        initView(view);
        setupMVP();
        getFacts();
        return view;
    }
    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    private void initView(View view){
        rvFacts = view.findViewById(R.id.rvFacts);
        rvFacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFacts.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
    }


    private void getFacts() {

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
    }

    @Override
    public void displayError(String e) {

        showToast(e);

    }

    private void updateAdapter(@Nullable Country country) {
        adapter = new CountryFactsAdapter(Arrays.asList(country.getRows()), getActivity().getApplicationContext());
        rvFacts.setAdapter(adapter);
    }
}