package com.mobilefactory.whosnext.model.parse;

import com.mobilefactory.whosnext.model.User;
import com.parse.ParseClassName;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("_User")
public class ParseUser extends com.parse.ParseUser implements User {

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
