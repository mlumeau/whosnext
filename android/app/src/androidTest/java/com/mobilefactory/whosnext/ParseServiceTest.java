package com.mobilefactory.whosnext;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.mobilefactory.whosnext.model.Group;
import com.mobilefactory.whosnext.model.User;
import com.mobilefactory.whosnext.service.DBService;
import com.mobilefactory.whosnext.service.ServiceCallback;
import com.mobilefactory.whosnext.service.parse.ParseService;

import java.util.concurrent.CountDownLatch;


public class ParseServiceTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private DBService dbService;

    public ParseServiceTest(){
        super(LoginActivity.class);
    }

    @Override
     protected void setUp() throws Exception {
        super.setUp();
        dbService = new ParseService();
    }

    public void testGetUser() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        dbService.getUser("8wXBSF8EJH", new ServiceCallback<User>() {
            @Override
            public void doWithResult(User result) {
                Log.e("PARSE", "GOT A USER: " + result.getId() + ", " + result.getUsername());
                assertNotNull(result);
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

        dbService.getGroup("lrZHyhSyi5", new ServiceCallback<Group>() {
            @Override
            public void doWithResult(Group result) {
                Log.e("PARSE", "GOT A GROUP: " + result.getId() + ", " + result.getName());
                assertNotNull(result);
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

}