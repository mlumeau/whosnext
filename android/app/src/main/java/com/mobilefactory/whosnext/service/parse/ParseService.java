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
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public class ParseService implements DBService {

    @Override
    public void getUser(final String id, final ServiceCallback<User> usc) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.getInBackground(id, new GetCallback<ParseUser>() {

            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    usc.doWithResult(object);
                } else {
                    Log.d("PARSE","Unable to retrieve user "+id,e);
                    usc.failed();
                }
            }
        });

    }

    @Override
    public void getGroup(final String id, final ServiceCallback<Group> gsc) {
        ParseQuery<ParseGroup> query = ParseQuery.getQuery(ParseGroup.class);
        query.getInBackground(id, new GetCallback<ParseGroup>() {

            public void done(ParseGroup object, ParseException e) {
                if (e == null) {
                    gsc.doWithResult(object);
                } else {
                    Log.d("PARSE","Unable to retrieve group "+id,e);
                    gsc.failed();
                }
            }
        });
    }

    @Override
    public void getGroupUsers(final String groupId, final ServiceCallback<List<User>> gusc) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        //query.whereEqualTo("group",group); //TODO: use relation table user_group
        query.findInBackground(new FindCallback<ParseUser>() {

            public void done(List<ParseUser> object, ParseException e) {
                if (e == null) {
                    List<User> users = new ArrayList<>();
                    for (ParseUser pu : object){
                        users.add(pu);
                    }
                    gusc.doWithResult(users);
                } else {
                    Log.d("PARSE", "Unable to retrieve group user" + groupId, e);
                    gusc.failed();
                }
            }
        });
    }
}
