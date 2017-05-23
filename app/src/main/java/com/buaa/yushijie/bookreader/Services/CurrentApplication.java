package com.buaa.yushijie.bookreader.Services;

import android.app.Application;

import com.buaa.yushijie.bookreader.Fragments.CommentRecyclerFragment;
import com.buaa.yushijie.bookreader.Fragments.MyBookShelfDeleteACategoryDialogFragment;
import com.buaa.yushijie.bookreader.Fragments.MyBookShelfMainPartFragment;

import java.util.ArrayList;

import bean.BookBean;
import bean.CommentBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-16.
 */

public class CurrentApplication extends Application {
    private UserBean user;
    private ArrayList<UserCategory> userCategories;
    private ArrayList<ArrayList<BookBean>> bookList;
    private MyBookShelfMainPartFragment.ExpandableListViewAdapter mAdapter;
    private CommentRecyclerFragment.CommentAdapter currentAdapter;
    private ArrayList<CommentBean> commentBeanArrayList;

    public CommentRecyclerFragment.CommentAdapter getCurrentAdapter() {
        return currentAdapter;
    }

    public void setCurrentAdapter(CommentRecyclerFragment.CommentAdapter currentAdapter) {
        this.currentAdapter = currentAdapter;
    }

    public ArrayList<CommentBean> getCommentBeanArrayList() {
        return commentBeanArrayList;
    }

    public void setCommentBeanArrayList(ArrayList<CommentBean> commentBeanArrayList) {
        this.commentBeanArrayList = commentBeanArrayList;
    }


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

    public MyBookShelfMainPartFragment.ExpandableListViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MyBookShelfMainPartFragment.ExpandableListViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public ArrayList<ArrayList<BookBean>> getBookList() {
        return bookList;
    }

    public void setBookList(ArrayList<ArrayList<BookBean>> bookList) {
        this.bookList = bookList;
    }

    public CurrentApplication(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
