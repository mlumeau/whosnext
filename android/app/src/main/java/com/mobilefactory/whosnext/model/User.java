package com.mobilefactory.whosnext.model;

import com.mobilefactory.whosnext.service.ServiceCallback;

import java.util.Date;
import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface User {

    String getId();

    String getUsername();

    void setUsername(String name);

    Date getBirthdate();

    void setBirthdate(Date date);

    String getGoogleId();

    void setGoogleId(String googleId);

    List<Group> getGroups();

    void fetchGroups(ServiceCallback<User> callback);
}
