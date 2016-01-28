package com.mobilefactory.whosnext.service;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public abstract class DBService {
    abstract public User getCurrentUser();
    abstract public void getUser(String userId, ServiceCallback<User> callback);
    abstract public void getGroup(String groupId, ServiceCallback<Group> callback);
    abstract public void getGroupUsers(Group group, ServiceCallback<List<User>> callback);
    abstract public void getUserGroups(User user, ServiceCallback<List<Group>> callback);
    abstract public void addGroupUsers(List<User> users, Group group, ServiceCallback<Group> callback);

}
