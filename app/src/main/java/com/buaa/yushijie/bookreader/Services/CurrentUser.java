package com.buaa.yushijie.bookreader.Services;

import android.app.Application;

import java.util.ArrayList;

import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-16.
 */

public class CurrentUser extends Application {
    private UserBean user;
    private ArrayList<UserCategory> userCategories;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ArrayList<UserCategory> getUserCategories() {
        return userCategories;
    }

    public void setUserCategories(ArrayList<UserCategory> userCategories) {
        this.userCategories = userCategories;
    }

    public CurrentUser(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
