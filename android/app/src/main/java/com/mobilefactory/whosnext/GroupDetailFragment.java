package com.mobilefactory.whosnext;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.mobilefactory.whosnext.utils.Animations;
import com.mobilefactory.whosnext.utils.Constants;
import com.mobilefactory.whosnext.view.UserRecyclerViewAdapter;
import com.mobilefactory.whosnext.view.WrappingRecyclerViewLayoutManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Group detail screen.
 * This fragment is either contained in a {@link GroupListFragment}
 * in two-pane mode (on tablets) or a {@link GroupDetailActivity}
 * on handsets.
 */
public class GroupDetailFragment extends Fragment {
    DBService dbService = ParseService.getInstance();


    /**
     * The content this fragment is presenting.
     */
    private Group mItem;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_detail, container, false);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.group_members);

        if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {


            //TODO: get from local datastore
            dbService.getGroup(getArguments().getString(Constants.ARG_ITEM_ID), new ServiceCallback<Group>() {
                @Override
                public void doWithResult(final Group result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mItem = result;

                            ImageView iv = null;
                            String coverUrl = mItem.getCoverUrl();

                            Activity activity = getActivity();
                            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                            if (appBarLayout != null) {

                                iv = (ImageView) getActivity().findViewById(R.id.main_backdrop);

                                appBarLayout.setTitle(mItem.getName());
                            }

                            TextView titleView = ((TextView) rootView.findViewById(R.id.group_title));
                            if (titleView != null) {
                                titleView.setText(mItem.getName());
                                iv = (ImageView) rootView.findViewById(R.id.cover_photo);
                            }

                            if (!coverUrl.equals("") && iv != null)
                                Picasso.with(getContext())
                                        .load(coverUrl)
                                        .noFade()
                                        .fit()
                                        .centerCrop()
                                        .placeholder(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorPrimary)))
                                        .into(iv, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                ActivityCompat.startPostponedEnterTransition(getActivity());
                                            }

                                            @Override
                                            public void onError() {
                                                ActivityCompat.startPostponedEnterTransition(getActivity());
                                            }
                                        });
                            else if (iv != null) {
                                iv.setBackground(new ColorDrawable(ColorGenerator.MATERIAL.getColor(mItem.getName())));
                                ActivityCompat.startPostponedEnterTransition(getActivity());
                            }




                        }
                    });
                    setupRecyclerView(result);
                    setupAdmin(result);

                }

                @Override
                public void failed(DBException e) {
                    Log.e("DBSERVICE", "Failed to retrieve group "+getArguments().getString(Constants.ARG_ITEM_ID));
                }
            });
        }

        return rootView;
    }

    private void setupAdmin(final Group group) {
        group.fetchAdmins(new ServiceCallback<Group>() {
            @Override
            public void doWithResult(final Group result) {

                for(User u : result.getAdmins()) {
                    if (u.getId().equals(dbService.getCurrentUser().getId())){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
                                fab.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), EditGroupActivity.class);
                                        intent.putExtra(Constants.ARG_ITEM_ID,result.getId());
                                        startActivityForResult(intent, Constants.EDIT_GROUP_REQUEST_CODE);
                                    }
                                });
                                Animations.reveal(fab, null);
                            }
                        });
                    }
                }
            }

            @Override
            public void failed(DBException e) {
                Log.e(dbService.getClass().getSimpleName(), "Failed to fetch admins of group " + group.getId());
            }
        });
    }

    private void setupRecyclerView(final Group group) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new WrappingRecyclerViewLayoutManager(getContext()));
                recyclerView.setAdapter(new UserRecyclerViewAdapter(GroupDetailFragment.this.getActivity()));
            }
        });
        group.fetchUsers(new ServiceCallback<Group>() {
            @Override
            public void doWithResult(Group group) {
                ((UserRecyclerViewAdapter) recyclerView.getAdapter()).setValues(group.getUsers());
                changeIndeterminateProgress(false);
            }

            @Override
            public void failed(DBException e) {
                Log.e(dbService.getClass().getSimpleName(), "Failed to fetch users of group " + group.getId());
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
