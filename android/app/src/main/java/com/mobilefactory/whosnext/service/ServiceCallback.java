package com.mobilefactory.whosnext.service;

/**
 * Created by Maxime on 19/12/2015.
 */
public abstract class ServiceCallback<T> {

    public abstract void doWithResult(T result);

    public abstract void failed(DBException e);
}
