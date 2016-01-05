package com.mobilefactory.whosnext.model;

import com.mobilefactory.whosnext.service.ServiceCallback;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface Group {

    String getId();

    String getName();

    String getCoverUrl();

    List<User> getUsers();

    void fetchUsers(ServiceCallback<Group> callback);
}
