package com.uday.mvp_recyclerview_demo.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.uday.mvp_recyclerview_demo.R;
import com.uday.mvp_recyclerview_demo.ui.fragments.FactsFragment;


public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new FactsFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
}
