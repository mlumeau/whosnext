package com.mobilefactory.whosnext.service;

/**
 * Created by mlumeau on 11/01/2016.
 */
public class DBException extends Exception{


    public static final int CODE_USERNAME_TAKEN = 202;

    private int code = 0;

    public DBException(){
        super();
    }

    public DBException(int code, String message){
        super(message);
        this.code = code;

    }

    public int getCode(){
        return code;
    }

}
