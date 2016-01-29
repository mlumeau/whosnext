package com.mobilefactory.whosnext.model.parse;

import android.graphics.Bitmap;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBException;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("Group")
public class ParseGroup extends ParseObject implements Group {

    public static final String KEY_NAME = "name";
    private static final String KEY_COVER = "cover";

    private List<User> groupUsers;
    private List<User> groupAdmins;

    public ParseGroup(){
        super();
        groupUsers = new ArrayList<>();
        groupAdmins = new ArrayList<>();
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getName() {
        return getString(KEY_NAME);
    }

    @Override
    public void setName(String name) {
        this.put(KEY_NAME,name);
    }

    @Override
    public String getCoverUrl() {
        ParseFile coverFile = getParseFile(KEY_COVER);
        if(coverFile!=null)
            return coverFile.getUrl();
        else
            return "";
    }

    @Override
    public void setCoverImage(Bitmap bitmap) {
        // Convert bitmap to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        ParseFile pf = new ParseFile(byteArray);

        this.put(KEY_COVER,pf);
    }

    @Override
    public List<User> getUsers() {
        return groupUsers;
    }

    public void fetchUsers(final ServiceCallback<Group> callback){
        ParseService.getInstance().getGroupUsers(this, new ServiceCallback<List<User>>() {
            @Override
            public void doWithResult(List<User> result) {
                groupUsers = result;
                callback.doWithResult(ParseGroup.this);
            }

            @Override
            public void failed(DBException e) {
                callback.failed(new DBException(e.getCode(),e.getMessage()));
            }
        });
    }

    @Override
    public List<User> getAdmins() {
        return groupAdmins;
    }

    @Override
    public void fetchAdmins(final ServiceCallback<Group> callback) {
        ParseService.getInstance().getGroupAdmins(this, new ServiceCallback<List<User>>() {
            @Override
            public void doWithResult(List<User> result) {
                groupAdmins = result;
                callback.doWithResult(ParseGroup.this);
            }

            @Override
            public void failed(DBException e) {
                callback.failed(new DBException(e.getCode(), e.getMessage()));
            }
        });
    }


    @Override
    public void saveGroup(final ServiceCallback<Group> callback) {

        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.doWithResult(ParseGroup.this);
                } else {
                    callback.failed(new DBException(e.getCode(), e.getMessage()));
                }
            }
        });
    }
}
