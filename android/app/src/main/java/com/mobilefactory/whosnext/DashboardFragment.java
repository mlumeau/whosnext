package com.mobilefactory.whosnext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mlumeau on 24/12/2015.
 */
public class DashboardFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_dashboard,container,false);

        SwipeRefreshLayout srl = (SwipeRefreshLayout) v.getRootView();
        srl.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimary);

        return v;
    }
}
