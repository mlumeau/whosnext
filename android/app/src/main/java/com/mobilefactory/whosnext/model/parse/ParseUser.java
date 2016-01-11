package com.mobilefactory.whosnext.model.parse;

import android.graphics.Bitmap;
import android.util.Log;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("_User")
public class ParseUser extends com.parse.ParseUser implements User {

    public static final String KEY_BIRTH_DATE = "birthDate";
    public static final String KEY_GOOGLE_ID = "googleId";
    private static final String KEY_PIC = "avatar";
    private List<Group> userGroups;

    public ParseUser(){
        super();
        userGroups = new ArrayList<>();
    }


    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public Date getBirthdate() {
        return getDate(KEY_BIRTH_DATE);
    }

    @Override
    public void setBirthdate(Date date) {
        this.put(KEY_BIRTH_DATE, date);
    }

    @Override
    public String getGoogleId() {
        return getString(KEY_GOOGLE_ID);
    }

    @Override
    public void setGoogleId(String googleId) {
        this.put(KEY_GOOGLE_ID,googleId);
    }

    @Override
    public List<Group> getGroups() {
        return userGroups;
    }

    @Override
    public String getPictureUrl() {
        ParseFile coverFile = getParseFile(KEY_PIC);
        if(coverFile!=null)
            return coverFile.getUrl();
        else
            return "";
    }

    @Override
    public void setPictureImage(Bitmap bitmap) {
        // Convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        final ParseFile pf = new ParseFile(byteArray);
        try {
            pf.save();
            ParseUser.this.put(KEY_PIC, pf);
        } catch (ParseException e) {
            Log.d("PARSE", "Unable to save file." , e);
        }

    }

    public void fetchGroups(final ServiceCallback<User> callback){
        ParseService.getInstance().getUserGroups(this, new ServiceCallback<List<Group>>() {
            @Override
            public void doWithResult(List<Group> result) {
                userGroups = result;
                callback.doWithResult(ParseUser.this);
            }

            @Override
            public void failed(DBException e) {
                callback.failed(new DBException(e.getCode(),e.getMessage()));
            }
        });
    }

    @Override
    public void saveUser(final ServiceCallback<User> callback){
        saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    callback.doWithResult(ParseUser.this);
                }else{
                    callback.failed(new DBException(e.getCode(),e.getMessage()));
                }
            }
        });
    }

}
