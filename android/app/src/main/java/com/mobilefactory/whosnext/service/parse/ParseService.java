package com.mobilefactory.whosnext.service.parse;

import android.util.Log;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.model.parse.ParseGroup;
import com.mobilefactory.whosnext.model.parse.ParseUser;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public class ParseService implements DBService {

    @Override
    public void getUser(final String id, final ServiceCallback<User> callback) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.getInBackground(id, new GetCallback<ParseUser>() {

            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    callback.doWithResult(object);
                } else {
                    Log.d("PARSE","Unable to retrieve user "+id,e);
                    callback.failed();
                }
            }
        });

    }

    @Override
    public void getGroup(final String id, final ServiceCallback<Group> callback) {
        ParseQuery<ParseGroup> query = ParseQuery.getQuery(ParseGroup.class);
        query.getInBackground(id, new GetCallback<ParseGroup>() {

            public void done(ParseGroup object, ParseException e) {
                if (e == null) {
                    callback.doWithResult(object);
                } else {
                    Log.d("PARSE","Unable to retrieve group "+id,e);
                    callback.failed();
                }
            }
        });
    }

    @Override
    public void getGroupUsers(final Group group, final ServiceCallback<List<User>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Group");
        query.whereEqualTo("group",group);
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> relations, ParseException e) {
                if (e == null) {
                    List<User> users = new ArrayList<>();
                    for (ParseObject po : relations){
                        try {
                            users.add((User) po.getParseObject("user").fetchIfNeeded());
                        } catch (ParseException e1) {
                            Log.d("PARSE", "Unable to retrieve group user" + po.getParseObject("user").getObjectId(), e);
                        }
                    }
                    callback.doWithResult(users);
                } else {
                    Log.d("PARSE", "Unable to retrieve users from group" + group.getId(), e);
                    callback.failed();
                }
            }
        });
    }

    @Override
    public void getUserGroups(final User user, final ServiceCallback<List<Group>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Group");
        query.whereEqualTo("user",user);
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> relations, ParseException e) {
                if (e == null) {
                    List<Group> groups = new ArrayList<>();
                    for (ParseObject po : relations){
                        try {
                            groups.add((Group) po.getParseObject("group").fetchIfNeeded());
                        } catch (ParseException e1) {
                            Log.d("PARSE", "Unable to retrieve user's group" + po.getParseObject("group").getObjectId(), e);
                        }
                    }
                    callback.doWithResult(groups);
                } else {
                    Log.d("PARSE", "Unable to retrieve groups from user" + user.getId(), e);
                    callback.failed();
                }
            }
        });
    }
}
