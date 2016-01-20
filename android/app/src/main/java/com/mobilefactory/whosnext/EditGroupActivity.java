package com.mobilefactory.whosnext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.model.parse.ParseGroup;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.List;

public class EditGroupActivity extends AppCompatActivity {

    public static final int EDIT_ACCOUNT_OKAY_RESULT_CODE = 200;
    public static final int EDIT_ACCOUNT_ABORT_RESULT_CODE = -1;
    public static final int SELECT_PICTURE_REQUEST_CODE = 9000;
    private DBService dbService = ParseService.getInstance();
    private Group mGroup;
    private EditText mGroupName;
    private ImageView mProfilePic;
    private Uri outputFileUri;
    private boolean isModified = false;
    private Bitmap mNewBitmap;
    private View mAddMember;
    private RecyclerView mMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mNewBitmap = null;

        setContentView(R.layout.activity_edit_group);

        RelativeLayout mProfilePicWrapper = (RelativeLayout) findViewById(R.id.edit_profile_pic_wrapper);
        mProfilePic = (ImageView) findViewById(R.id.edit_profile_pic);
        mGroupName = (EditText) findViewById(R.id.edit_groupname);
        mAddMember = findViewById(R.id.add_member);
        mMemberList = (RecyclerView) findViewById(R.id.group_members);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //Init existing group
        if(getIntent().getStringExtra("groupId")!=null){
            this.setTitle(getString(R.string.edit_group));
            dbService.getGroup(getIntent().getStringExtra("groupId"), new ServiceCallback<Group>() {
                @Override
                public void doWithResult(Group result) {
                    initGroup(result);
                }

                @Override
                public void failed(DBException e) {

                }
            });
        }else{
            mGroup = new ParseGroup();
            this.setTitle(getString(R.string.create_group));
        }

        mProfilePicWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //BUILD INTENTS LIST

                //pick
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_PICK);

                //get
                Intent getIntent = new Intent();
                getIntent.setType("image/*");
                getIntent.setAction(Intent.ACTION_GET_CONTENT);

                //camera
                final String fname = "img_" + System.currentTimeMillis() + ".jpg";
                final File imgFile = new File(EditGroupActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fname);
                outputFileUri = Uri.fromFile(imgFile);
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                String pickTitle = getString(R.string.image_intent_picker_title);
                Intent chooserIntent = Intent.createChooser(new Intent(), pickTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{getIntent, pickIntent, captureIntent});

                startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
            }
        });


        mAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mGroupName.getText().toString().trim().equals(mGroup.getName())){
                    mGroup.setName(mGroupName.getText().toString().trim());
                    isModified = true;
                }
                if(mNewBitmap!=null) {
                    mGroup.setCoverImage(mNewBitmap);
                    isModified = true;
                }

                if(isModified){
                    mGroup.saveGroup(new ServiceCallback<Group>() {

                        @Override
                        public void doWithResult(Group result) {
                            setResult(EDIT_ACCOUNT_OKAY_RESULT_CODE);
                            finish();
                        }

                        @Override
                        public void failed(final DBException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    });
                }else{
                    setResult(EDIT_ACCOUNT_ABORT_RESULT_CODE);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri path = outputFileUri; //camera
            if(data != null) { //pick
                path = data.getData();
            }

            final Uri picturePath = path;

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mNewBitmap = bitmap;
                    Picasso.with(EditGroupActivity.this).load(picturePath).fit().centerCrop().into(mProfilePic);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            mProfilePic.setTag(target);
            Picasso.with(this).load(picturePath).resize(500, 500).onlyScaleDown().centerInside().into(target);

        }

    }

    private void initGroup(final Group group){
        mGroup = group;
        dbService.getGroupUsers(mGroup, new ServiceCallback<List<User>>() {
            @Override
            public void doWithResult(List<User> result) {
                mGroup.getUsers().addAll(result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: add users to recycler
                    }
                });
            }

            @Override
            public void failed(DBException e) {

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGroupName.setText(group.getName());
                if (!mGroup.getCoverUrl().isEmpty())
                    Picasso.with(EditGroupActivity.this).load(mGroup.getCoverUrl()).fit().centerCrop().into(mProfilePic);
            }
        });

    }
}
