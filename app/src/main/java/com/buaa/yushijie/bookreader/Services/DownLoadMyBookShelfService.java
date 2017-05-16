package com.buaa.yushijie.bookreader.Services;

import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bean.BookBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-16.
 */

public class DownLoadMyBookShelfService {
    private static final String URL_GET_BOOK_SHELF_CATEGORY_INFO="";
    private static final String URL_GET_BOOK_SHELF_BOOKBEAN_INFO="";
    HttpURLConnection conn = null;
    URL url = null;

    public HttpURLConnection getConn(){
        return conn;
    }
    private void connectToServer(String path) throws Exception{
        url = new URL(path);
        conn = (HttpURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("POST");
        conn.connect();
    }

    public ArrayList<UserCategory> getCategoryNameList(String username) throws Exception{
        ArrayList<UserCategory> res = new ArrayList<>();
        connectToServer(URL_GET_BOOK_SHELF_CATEGORY_INFO+EncodeAndDecode.encodeString(username));
        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        UserCategory uc;

        while((uc=(UserCategory) ois.readObject()) != null){
            res.add(uc);
        }
        ois.close();
        if(conn!=null)conn.disconnect();
        return res;
    }

    public ArrayList<BookBean> getBookOfEveryCategory(UserCategory uc) throws Exception{
        ArrayList<BookBean> res = new ArrayList<>();
        connectToServer(URL_GET_BOOK_SHELF_BOOKBEAN_INFO+uc.CategoryID);
        ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
        BookBean bookBean;
        while((bookBean = (BookBean) ois.readObject())!=null){
            res.add(bookBean);
        }
        ois.close();
        if(conn!=null) conn.disconnect();
        return res;
    }
}
