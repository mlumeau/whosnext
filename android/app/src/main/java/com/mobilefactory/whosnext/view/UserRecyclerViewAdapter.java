package com.mobilefactory.whosnext.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mobilefactory.whosnext.R;
import com.mobilefactory.whosnext.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter
            extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private Activity activity;


    public UserRecyclerViewAdapter(Activity activity) {
        mValues = new ArrayList<>();
        this.activity = activity;
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
        holder.mContentView.setText(mValues.get(position).getUsername());

        Drawable placeholder = TextDrawable.builder().buildRound(mValues.get(position).getUsername().substring(0, 1).toUpperCase(), ColorGenerator.MATERIAL.getColor(mValues.get(position).getUsername()));
        holder.mImageView.setImageDrawable(placeholder);

        String pictureUrl = holder.mItem.getPictureUrl();
        if(!pictureUrl.equals(""))
            Picasso.with(activity)
                .load(pictureUrl)
                .fit()
                .centerCrop()
                .placeholder(placeholder)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void setValues(List<User> items){
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
        public User mItem;

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