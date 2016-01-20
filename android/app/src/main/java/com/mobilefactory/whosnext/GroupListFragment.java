package com.mobilefactory.whosnext;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.mobilefactory.whosnext.view.GroupRecyclerViewAdapter;

/**
 * A fragment representing a list of Groups. This fragment
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GroupDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GroupListFragment extends Fragment {


    DBService dbService = ParseService.getInstance();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_group_list, container, false);

        progress = (ProgressBar) v.findViewById(R.id.progress);

        View recyclerView = v.findViewById(R.id.group_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (v.findViewById(R.id.group_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        return v;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        recyclerView.setAdapter(new GroupRecyclerViewAdapter(this.getActivity(), mTwoPane));
        dbService.getCurrentUser().fetchGroups(new ServiceCallback<User>() {
            @Override
            public void doWithResult(User result) {
                ((GroupRecyclerViewAdapter) recyclerView.getAdapter()).setValues(result.getGroups());
                changeIndeterminateProgress(false);
            }

            @Override
            public void failed(DBException e) {
                Log.e(dbService.getClass().getSimpleName(), "Failed to fetch groups of user " + dbService.getCurrentUser().getId());
                changeIndeterminateProgress(false);
            }
        });
    }

    private void changeIndeterminateProgress(final boolean inProgress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inProgress)
                    progress.setVisibility(View.VISIBLE);
                else
                    progress.setVisibility(View.GONE);
            }
        });
    }

}
