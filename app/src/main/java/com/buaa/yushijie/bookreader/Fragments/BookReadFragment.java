package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.buaa.yushijie.bookreader.Activities.BookReadingActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;

import bean.BookBean;
import nl.siegmann.epublib.domain.Book;

/**
 * Created by yushijie on 17-5-25.
 */

public class BookReadFragment extends Fragment {
    private WebView mWebView;
    private BookBean currentBook;
    private Activity currentActivity;
    private Book books;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            books = (Book) msg.obj;
            try {
                String a = new String(books.getContents().get(2).getData(),"UTF-8");
                mWebView.loadData(a, "text/html;charset=UTF-8", null);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.book_read_fragment,container,false);
        mWebView = (WebView)v.findViewById(R.id.book_reading_webview);
        getBookData();
        return v;
    }
    private void getBookData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Book book = new DownLoadBookInfoService().getEPUBBook(currentBook.fileSource,currentActivity);
                    Message msg = new Message();
                    msg.obj = book;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
