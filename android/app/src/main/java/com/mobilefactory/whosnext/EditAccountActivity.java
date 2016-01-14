package com.mobilefactory.whosnext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;

public class EditAccountActivity extends AppCompatActivity {

    public static final int EDIT_ACCOUNT_OKAY_RESULT_CODE = 200;
    public static final int EDIT_ACCOUNT_ABORT_RESULT_CODE = -1;
    public static final int SELECT_PICTURE_REQUEST_CODE = 9000;
    private DBService dbService = ParseService.getInstance();
    private User mUser;
    private EditText mBirthdate;
    private EditText mUsername;
    private Calendar mCalendar = Calendar.getInstance();
    private ImageView mProfilePic;
    private RelativeLayout mProfilePicWrapper;
    private Uri outputFileUri;
    private boolean isModified = false;
    private Bitmap mNewBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUser = dbService.getCurrentUser();

        setContentView(R.layout.activity_edit_account);

        mProfilePicWrapper = (RelativeLayout) findViewById(R.id.edit_profile_pic_wrapper);
        mProfilePic = (ImageView) findViewById(R.id.edit_profile_pic);
        mUsername = (EditText) findViewById(R.id.edit_username);
        mBirthdate = (EditText) findViewById(R.id.edit_birthdate);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mUsername.setText(mUser.getUsername());
        mBirthdate.setText(DateFormat.getDateInstance().format(mUser.getBirthdate()));
        mCalendar.setTime(mUser.getBirthdate());

        if(!mUser.getPictureUrl().isEmpty())
            Picasso.with(this).load(mUser.getPictureUrl()).fit().centerCrop().into(mProfilePic);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mCalendar.set(Calendar.HOUR,0);
                mCalendar.set(Calendar.MINUTE,0);
                mCalendar.set(Calendar.SECOND,0);
                mCalendar.set(Calendar.MILLISECOND,0);
                updateLabel();
            }

        };
        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAccountActivity.this, R.style.DialogTheme, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

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
                final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
                final File imgFile = new File(EditAccountActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fname);
                outputFileUri = Uri.fromFile(imgFile);
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                String pickTitle = getString(R.string.image_intent_picker_title);
                Intent chooserIntent = Intent.createChooser(new Intent(), pickTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{getIntent,pickIntent,captureIntent});

                startActivityForResult(chooserIntent, SELECT_PICTURE_REQUEST_CODE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mUsername.getText().toString().trim().equals(mUser.getUsername())){
                    mUser.setUsername(mUsername.getText().toString().trim());
                    isModified = true;
                }
                if(!mCalendar.getTime().equals(mUser.getBirthdate())){
                    mUser.setBirthdate(mCalendar.getTime());
                    isModified = true;
                }
                if(mNewBitmap!=null) {
                    mUser.setPictureImage(mNewBitmap);
                    isModified = true;
                }

                if(isModified){
                    mUser.saveUser(new ServiceCallback<User>() {

                        @Override
                        public void doWithResult(User result) {
                            setResult(EDIT_ACCOUNT_OKAY_RESULT_CODE);
                            finish();
                        }

                        @Override
                        public void failed(final DBException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //todo: handle error types properly
                                    if(e.getCode()==202){ //username taken
                                        mUsername.setError(getString(R.string.username_taken));
                                    }
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
                    Picasso.with(EditAccountActivity.this).load(picturePath).fit().centerCrop().into(mProfilePic);
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

    private void updateLabel() {

        DateFormat sdf = DateFormat.getDateInstance();

        mBirthdate.setText(sdf.format(mCalendar.getTime()));
    }
}
