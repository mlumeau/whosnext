package com.mobilefactory.whosnext.service.parse;

import android.util.Log;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.model.parse.ParseGroup;
import com.mobilefactory.whosnext.model.parse.ParseUser;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
                try{
                    User u = query.get(id);
                    callback.doWithResult(u);
                } catch(ParseException e){
                    Log.d("PARSE","Unable to retrieve user "+id,e);
                    callback.failed();
                }
            }
        }).start();

    }

    @Override
    public void getGroup(final String id, final ServiceCallback<Group> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseGroup> query = ParseQuery.getQuery(ParseGroup.class);

                try{
                    Group g = query.get(id);
                    callback.doWithResult(g);
                }catch(ParseException e){
                    Log.d("PARSE", "Unable to retrieve group " + id, e);
                    callback.failed();
                }
            }
        }).start();
    }

    @Override
    public void getGroupUsers(final Group group, final ServiceCallback<List<User>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Group");
                query.whereEqualTo("group", group);
                try {
                    List<ParseObject> relations = query.find();
                    List<User> users = new ArrayList<>();
                    for (ParseObject po : relations) {
                        users.add((User) po.getParseObject("user").fetchIfNeeded());
                    }
                    callback.doWithResult(users);
                }catch (ParseException e){
                    Log.d("PARSE", "Unable to retrieve users from group" + group.getId(), e);
                    callback.failed();
                }
            }
        }).start();
    }

    @Override
    public void getUserGroups(final User user, final ServiceCallback<List<Group>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Group");
                query.whereEqualTo("user", user);
                try {
                    List<ParseObject> relations = query.find();
                    List<Group> groups = new ArrayList<>();
                    for (ParseObject po : relations) {
                        groups.add((Group) po.getParseObject("group").fetchIfNeeded());
                    }
                    callback.doWithResult(groups);
                } catch (ParseException e) {
                    Log.d("PARSE", "Unable to retrieve groups from user" + user.getId(), e);
                    callback.failed();
                }
            }
        }).start();

    }
}
