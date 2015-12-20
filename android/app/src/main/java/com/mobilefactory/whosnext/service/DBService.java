package com.mobilefactory.whosnext.service;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface DBService {

    void getUser(String userId, ServiceCallback<User> callback);
    void getGroup(String groupId, ServiceCallback<Group> callback);
    void getGroupUsers(Group group, ServiceCallback<List<User>> callback);
    void getUserGroups(User user, ServiceCallback<List<Group>> callback);

}
