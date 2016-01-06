package com.mobilefactory.whosnext;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Groups. This fragment
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GroupDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GroupListFragment extends Fragment {

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

        //TODO: get groups from current user instead of dummy
        recyclerView.setAdapter(new GroupRecyclerViewAdapter());

        DBService dbService = new ParseService();
        dbService.getUser("buqPgzIGBL", new ServiceCallback<User>() {
            @Override
            public void doWithResult(final User result) {
                result.fetchGroups(new ServiceCallback<User>() {
                    @Override
                    public void doWithResult(User result) {
                        ((GroupRecyclerViewAdapter) recyclerView.getAdapter()).setValues(result.getGroups());
                        changeIndeterminateProgress(false);
                    }

                    @Override
                    public void failed() {
                        Log.e("DBSERVICE", "Failed to fetch groups of user " + result.getId());
                        changeIndeterminateProgress(false);
                    }
                });
            }

            @Override
            public void failed() {
                Log.e("DBSERVICE", "Failed to retrieve user");
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void changeIndeterminateProgress(final boolean inProgress){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(inProgress)
                    progress.setVisibility(View.VISIBLE);
                else
                    progress.setVisibility(View.GONE);
            }
        });
    }

    public class GroupRecyclerViewAdapter
            extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {

        private List<Group> mValues;

        public GroupRecyclerViewAdapter() {
            mValues = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getName());

            Drawable placeholder = TextDrawable.builder().buildRound(mValues.get(position).getName().substring(0, 1).toUpperCase(), ColorGenerator.MATERIAL.getColor(mValues.get(position).getName()));
            holder.mImageView.setImageDrawable(placeholder);

            String coverUrl = holder.mItem.getCoverUrl();
            if(!coverUrl.equals(""))
                Picasso.with(getContext())
                    .load(coverUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(placeholder)
                    .into(holder.mImageView);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(GroupDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        GroupDetailFragment fragment = new GroupDetailFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.group_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GroupDetailActivity.class);
                        intent.putExtra(GroupDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        //scene transitions
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), holder.mImageView,getString(R.string.group_image_transition_name));
                        context.startActivity(intent,options.toBundle());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


        public void setValues(List<Group> items){
            mValues = items;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mContentView;
            public Group mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.picture);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
