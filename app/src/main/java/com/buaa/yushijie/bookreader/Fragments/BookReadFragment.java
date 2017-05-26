package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    private int currentPage = 0;
    private GestureDetector gesture = null;
    private int mWebViewHeight;
    private int mWebViewWidth;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            books = (Book) msg.obj;
            try {
                String a = new String(books.getContents().get(2).getData(),"UTF-8");
               // goToNextPage(mWebView,5);
                mWebView.loadData(a, "text/html;charset=UTF-8", null);
                //Log.e("sss", "onCreateView: "+mWebView.getHeight()+" "+mWebView.getContentHeight()+" "+mWebView.getWidth() );

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
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        gesture =  new GestureDetector(currentActivity,new GestureListeners());
        mWebView.setOnTouchListener(new GestureListeners());
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
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

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            String js = "javascript:function initialize() { " +
                    "var d = document.getElementsByTagName('body')[0];" +
                    "var ourH = window.innerHeight; " +
                    "var ourW = window.innerWidth; " +
                    "var fullH = d.offsetHeight; " +
                    "var pageCount = Math.floor(fullH/ourH)+1;" +
                    "var newW = pageCount*ourW; " +
                    "d.style.height = (ourH-30)+'px';" +
                    "d.style.width = newW+'px';" +
                    "d.style.webkitColumnGap = '2px'; " +
                    "d.style.padding = 10; " +
                    "d.style.webkitColumnCount = pageCount;" +
                    "window.scrollBy("+currentPage+"*ourW,0)"+
                    "}";
            view.loadUrl(js);
           view.loadUrl("javascript:initialize()");
        }
    }

    private void goToNextPage(WebView view,int currentPage) {
        String js = "javascript:function nextPage() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourH = window.innerHeight; " +
                "var ourW = window.innerWidth; " +
                "window.scrollBy("+currentPage+"*ourW,0)"+
                "}";
        view.loadUrl(js);
        view.loadUrl("javascript:nextPage()");
    }
    private class GestureListeners  extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gesture.onTouchEvent(event);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e1.getX()-e2.getX();
            float y = e1.getY()-e2.getY();
            if(x>0 && Math.abs(y)<x) {
                Log.w("ss", "right");
                //currentPage++;
                goToNextPage(mWebView,1);

            }
            else if(x<0 && Math.abs(y)<Math.abs(x)){
                Log.d("sss", "onFling: left");
                //currentPage--;
                goToNextPage(mWebView,-1);

            }
            return true;
        }
    }


}

