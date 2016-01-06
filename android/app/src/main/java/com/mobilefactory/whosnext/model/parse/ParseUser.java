package com.mobilefactory.whosnext.model.parse;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.parse.ParseClassName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("_User")
public class ParseUser extends com.parse.ParseUser implements User {

    public static final String KEY_USERNAME = "username";
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
    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    @Override
    public List<Group> getGroups() {
        return userGroups;
    }

    public void fetchGroups(final ServiceCallback<User> callback){
        new ParseService().getUserGroups(this, new ServiceCallback<List<Group>>() {
            @Override
            public void doWithResult(List<Group> result) {
                userGroups = result;
                callback.doWithResult(ParseUser.this);
            }

            @Override
            public void failed() {
                callback.failed();
            }
        });
    }


}
