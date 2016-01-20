package com.mobilefactory.whosnext.model;

import android.graphics.Bitmap;

import com.mobilefactory.whosnext.service.ServiceCallback;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface Group {

    void saveGroup(ServiceCallback<Group> callback);

    String getId();

    String getName();

    void setName(String name);

    String getCoverUrl();

    void setCoverImage(Bitmap bitmap);

    List<User> getUsers();

    void fetchUsers(ServiceCallback<Group> callback);
}
