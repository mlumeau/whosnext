package com.mobilefactory.whosnext.service;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;

import java.util.List;

/**
 * Created by Maxime on 19/12/2015.
 */
public interface DBService {

    void getUser(String userId, ServiceCallback<User> usc);
    void getGroup(String groupId, ServiceCallback<Group> gsc);
    void getGroupUsers(String groupId, ServiceCallback<List<User>> gusc);

}
