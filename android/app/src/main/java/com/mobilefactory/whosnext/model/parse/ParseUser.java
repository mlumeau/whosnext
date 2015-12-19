package com.mobilefactory.whosnext.model.parse;

import com.mobilefactory.whosnext.model.User;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("User")
public class ParseUser extends ParseObject implements User {

    public ParseUser(){

    }

    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getUsername() {
        return getString("username");
    }
}
