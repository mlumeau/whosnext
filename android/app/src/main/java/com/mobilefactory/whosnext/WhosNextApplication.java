package com.mobilefactory.whosnext;

import android.app.Application;

import com.mobilefactory.whosnext.model.parse.ParseGroup;
import com.mobilefactory.whosnext.model.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseObject;

public class WhosNextApplication extends Application
{
  @Override
  public void onCreate()
  {
    super.onCreate();

    // [Optional] Power your app with Local Datastore. For more info, go to
    // https://parse.com/docs/android/guide#local-datastore
    Parse.enableLocalDatastore(this);

    ParseObject.registerSubclass(ParseUser.class);
    ParseObject.registerSubclass(ParseGroup.class);

    Parse.initialize(this);
  }
}