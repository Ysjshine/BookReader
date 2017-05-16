package com.buaa.yushijie.bookreader.Services;

import android.app.Application;

import bean.UserBean;

/**
 * Created by yushijie on 17-5-16.
 */

public class CurrentUser extends Application {
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public CurrentUser(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
