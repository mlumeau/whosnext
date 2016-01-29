package com.mobilefactory.whosnext.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mobilefactory.whosnext.GroupDetailActivity;
import com.mobilefactory.whosnext.GroupDetailFragment;
import com.mobilefactory.whosnext.R;
import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupRecyclerViewAdapter
            extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder> {

    private List<Group> mValues;
    private Activity activity;
    private boolean mTwoPane;


    public GroupRecyclerViewAdapter(Activity activity, boolean hasTwoPane) {
        mValues = new ArrayList<>();
        this.activity = activity;
        mTwoPane = hasTwoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_content, parent, false);
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
            Picasso.with(activity)
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
                    arguments.putString(Constants.ARG_ITEM_ID, holder.mItem.getId());
                    GroupDetailFragment fragment = new GroupDetailFragment();
                    fragment.setArguments(arguments);
                    ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.group_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GroupDetailActivity.class);
                    intent.putExtra(Constants.ARG_ITEM_ID, holder.mItem.getId());

                    //scene transitions
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.mImageView,activity.getString(R.string.image_transition_name));
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
        activity.runOnUiThread(new Runnable() {
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