package com.mobilefactory.whosnext.model.parse;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */

@ParseClassName("Group")
public class ParseGroup extends ParseObject implements Group {

    private List<User> groupUsers;

    public ParseGroup(){
        groupUsers = new ArrayList<>();
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    @Override
    public String getName() {
        return getString("name");
    }

    @Override
    public List<User> getUsers() {
        return groupUsers;
    }

    public void fetchUsers(final ServiceCallback<Group> callback){
        new ParseService().getGroupUsers(this, new ServiceCallback<List<User>>() {
            @Override
            public void doWithResult(List<User> result) {
                groupUsers = result;
                callback.doWithResult(ParseGroup.this);
            }

            @Override
            public void failed() {
                callback.failed();
            }
        });
    }

}