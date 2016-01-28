package com.mobilefactory.whosnext;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.mobilefactory.whosnext.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;

/**
 * Created by mlumeau on 24/12/2015.
 */
public class AccountFragment extends Fragment {


    DBService dbService = ParseService.getInstance();
    User mUser;
    private TextView mBirthdateView;
    private TextView mUsernameView;
    private ImageView mImageView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_account,container,false);

        mUser = dbService.getCurrentUser();

        mImageView = (ImageView) rootView.findViewById(R.id.profile_pic);
        mUsernameView = (TextView) rootView.findViewById(R.id.username);
        mBirthdateView = (TextView) rootView.findViewById(R.id.birthdate);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        mUsernameView.setText(mUser.getUsername());
        mBirthdateView.setText(DateFormat.getDateInstance().format(mUser.getBirthdate()));
        if(!mUser.getPictureUrl().isEmpty())
            Picasso.with(getContext()).load(mUser.getPictureUrl()).fit().centerCrop().into(mImageView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), EditAccountActivity.class);
                startActivityForResult(intent, Constants.EDIT_ACCOUNT_REQUEST_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== Constants.EDIT_ACCOUNT_REQUEST_CODE && resultCode== Constants.EDIT_ACCOUNT_OKAY_RESULT_CODE){
            mUsernameView.setText(mUser.getUsername());
            mBirthdateView.setText(DateFormat.getDateInstance().format(mUser.getBirthdate()));
            if(!mUser.getPictureUrl().isEmpty())
                Picasso.with(getContext()).load(mUser.getPictureUrl()).fit().centerCrop().into(mImageView);
        }
    }
}
