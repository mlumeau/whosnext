package com.mobilefactory.whosnext.model;

import com.mobilefactory.whosnext.service.ServiceCallback;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface User {

    String getId();

    String getUsername();

    List<Group> getGroups();

    void fetchGroups(ServiceCallback<User> callback);
}
