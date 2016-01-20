package com.mobilefactory.whosnext;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
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
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID= "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Group mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.group_detail, container, false);

        if (getArguments().containsKey(ARG_ITEM_ID)) {


            //TODO: get from local datastore
            dbService.getGroup(getArguments().getString(ARG_ITEM_ID), new ServiceCallback<Group>() {
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

                }

                @Override
                public void failed(DBException e) {
                    Log.e("DBSERVICE", "Failed to retrieve group "+getArguments().getString(ARG_ITEM_ID));
                }
            });
        }

        return rootView;
    }

}
