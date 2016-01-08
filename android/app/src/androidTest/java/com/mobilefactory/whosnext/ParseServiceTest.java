package com.mobilefactory.whosnext;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;

import java.util.concurrent.CountDownLatch;


public class ParseServiceTest extends ActivityInstrumentationTestCase2<SignInActivity> {

    private DBService dbService;

    public ParseServiceTest(){
        super(SignInActivity.class);
    }

    @Override
     protected void setUp() throws Exception {
        super.setUp();
        dbService = new ParseService();
    }

    public void testGetUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        dbService.getUser("buqPgzIGBL", new ServiceCallback<User>() {
            @Override
            public void doWithResult(User user) {
                Log.e("PARSE", "GOT A USER: [" + user.getId() + ", " + user.getUsername() + "]");
                assertNotNull(user);
                signal.countDown();
            }

            @Override
            public void failed() {
                Log.e("PARSE", "GOT NOTHING");
                assertNotNull(null);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void testGetGroup() throws InterruptedException {

        final CountDownLatch signal = new CountDownLatch(1);

        dbService.getGroup("qSE7ikCKbH", new ServiceCallback<Group>() {
            @Override
            public void doWithResult(Group group) {
                Log.e("PARSE", "GOT A GROUP: [" + group.getId() + ", " + group.getName() + "]");
                assertNotNull(group);
                signal.countDown();
            }

            @Override
            public void failed() {
                Log.e("PARSE", "GOT NOTHING");
                assertNotNull(null);
                signal.countDown();
            }
        });
        signal.await();
    }

    public void testGetGroupUsers() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        dbService.getGroup("qSE7ikCKbH", new ServiceCallback<Group>() {
            @Override
            public void doWithResult(final Group group) {
                Log.e("PARSE", "GOT A GROUP: [" + group.getId() + ", " + group.getName() + "]");

                group.fetchUsers(new ServiceCallback<Group>() {
                    @Override
                    public void doWithResult(Group group) {
                        for (User u : group.getUsers()) {
                            Log.e("PARSE", "GROUP [" + group.getId() + ", " + group.getName() + "] - GOT A USER: [" + u.getId() + ", " + u.getUsername() + "]");
                        }
                        assertFalse(group.getUsers().isEmpty());
                        signal.countDown();
                    }

                    @Override
                    public void failed() {
                        Log.e("PARSE", "GOT NOTHING");
                        assertNotNull(null);
                        signal.countDown();
                    }
                });
            }

            @Override
            public void failed() {
                Log.e("PARSE", "GOT NOTHING");
                assertNotNull(null);
                signal.countDown();
            }
        });

        signal.await();
    }

    public void testGetUserGroups() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        dbService.getUser("buqPgzIGBL", new ServiceCallback<User>() {
            @Override
            public void doWithResult(final User user) {
                Log.e("PARSE", "GOT A USER: [" + user.getId() + ", " + user.getUsername() + "]");

                user.fetchGroups(new ServiceCallback<User>() {
                    @Override
                    public void doWithResult(User user) {
                        for (Group g : user.getGroups()) {
                            Log.e("PARSE", "USER [" + user.getId() + ", " + user.getUsername() + "] - GOT A GROUP: [" + g.getId() + ", " + g.getName() + "]");
                        }
                        assertFalse(user.getGroups().isEmpty());
                        signal.countDown();
                    }

                    @Override
                    public void failed() {
                        Log.e("PARSE", "GOT NOTHING");
                        assertNotNull(null);
                        signal.countDown();
                    }
                });
            }

            @Override
            public void failed() {
                Log.e("PARSE", "GOT NOTHING");
                assertNotNull(null);
                signal.countDown();
            }
        });

        signal.await();
    }

}