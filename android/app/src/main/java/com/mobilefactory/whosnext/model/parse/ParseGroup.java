package com.mobilefactory.whosnext.model.parse;

import com.mobilefactory.whosnext.model.Group;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("Group")
public class ParseGroup extends ParseObject implements Group {

    public ParseGroup(){

    }


    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getName() {
        return getString("name");
    }

}
