package com.mobilefactory.whosnext;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;

import java.text.DateFormat;
import java.util.Calendar;

public class EditAccountActivity extends AppCompatActivity {

    public static final int EDIT_ACCOUNT_OKAY_RESULT_CODE = 200;
    public static final int EDIT_ACCOUNT_ABORT_RESULT_CODE = -1;
    private DBService dbService = ParseService.getInstance();
    private User mUser;
    private EditText mBirthdate;
    private EditText mUsername;
    private Calendar mCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mUser = dbService.getCurrentUser();

        setContentView(R.layout.activity_edit_account);

        //todo: implement edit profile pic
        mUsername = (EditText) findViewById(R.id.edit_username);
        mBirthdate = (EditText) findViewById(R.id.edit_birthdate);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mUsername.setText(mUser.getUsername());
        mBirthdate.setText(DateFormat.getDateInstance().format(mUser.getBirthdate()));
        mCalendar.setTime(mUser.getBirthdate());

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isModified = false;
                if(!mUsername.getText().toString().trim().equals(mUser.getUsername())){
                    mUser.setUsername(mUsername.getText().toString().trim());
                    isModified = true;
                }
                if(!mCalendar.getTime().equals(mUser.getBirthdate())){
                    mUser.setBirthdate(mCalendar.getTime());
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

    private void updateLabel() {

        DateFormat sdf = DateFormat.getDateInstance();

        mBirthdate.setText(sdf.format(mCalendar.getTime()));
    }
}
