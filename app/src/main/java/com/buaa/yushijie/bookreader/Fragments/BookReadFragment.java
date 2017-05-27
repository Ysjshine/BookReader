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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.buaa.yushijie.bookreader.Activities.BookReadingActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;
import com.buaa.yushijie.bookreader.UI.MyWebView;

import java.util.Timer;
import java.util.TimerTask;

import bean.BookBean;
import nl.siegmann.epublib.domain.Book;

/**
 * Created by yushijie on 17-5-25.
 */

public class BookReadFragment extends Fragment {
    private MyWebView mWebView;
    private static final String TAG="BookReadFragment";
    private Activity currentActivity;
    private Book books;
    private GestureDetector gesture = null;

    private BookBean currentBook;
    private int currentChapter = 0;
    private int currentPage = 0;

    private static final int NEXT=1;
    private static final int PREV=-1;

    private float htmlWidth=0;
    private int pageCount=0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                books = (Book) msg.obj;
                try {
                    loadDataToWebView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{


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
        mWebView = (MyWebView)v.findViewById(R.id.book_reading_webview);
        initWebView();
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
                    msg.what = 0;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //initialize webview;
    private void initWebView(){
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
               createPagesView(mWebView);
            }

        });

        gesture =  new GestureDetector(currentActivity,new GestureListeners());
        mWebView.setOnTouchListener(new GestureListeners());

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebView.setScrollContainer(false);

    }

    private void loadDataToWebView() throws Exception{
        String text = new String(books.getContents().get(currentChapter).getData(),"UTF-8");
        mWebView.loadData(text,"text/html;charset=UTF-8",null);

    }

    private void createPagesView(WebView view) {
        String javascriptCode = "javascript:function initialize() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourH = window.innerHeight; " +
                "var ourW = window.innerWidth; " +
                "var fullH = d.offsetHeight; " +
                "var pageCount = Math.floor(fullH/ourH)+1;" +
                "var newW = pageCount*ourW; " +
                "d.style.height = (ourH-50)+'px';" +
                "d.style.width = newW+'px';" +
                "d.style.webkitColumnGap = '20px'; " +
                "d.style.webkitColumnCount = pageCount;" +
                "window.scrollBy(0,0)"+
                "}";
        view.loadUrl(javascriptCode);
        view.loadUrl("javascript:initialize()");

    }

    private void goToNextOrBackPage(WebView view,int pos) {
        String javascriptCode = "javascript:function nextPage() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourW = window.innerWidth+1; " +
                "window.scrollBy("+pos+"*ourW,0)"+
                "}";
        view.loadUrl(javascriptCode);
        view.loadUrl("javascript:nextPage()");
    }

    private class GestureListeners  extends GestureDetector.SimpleOnGestureListener
            implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(htmlWidth==0&&pageCount==0){
                htmlWidth = mWebView.getContentWidth();
                pageCount = (int)Math.floor(htmlWidth/mWebView.getWidth());
                Log.e(TAG, "onTouch: "+htmlWidth+" "+pageCount );
            }
            return gesture.onTouchEvent(event)||(event.getAction() == MotionEvent.ACTION_MOVE);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e1.getX()-e2.getX();
            float y = e1.getY()-e2.getY();
            if(x>0 && Math.abs(y)<x) {
                Log.w(TAG, "onFling: "+"right" );
                if(currentPage<pageCount)
                {
                    currentPage++;
                    goToNextOrBackPage(mWebView,NEXT);
                    Log.e(TAG, "onFling: "+currentPage );
                }else if(currentPage==pageCount){
                    currentPage = 0;
                    currentChapter++;
                    htmlWidth = 0;
                    pageCount = 0;
                    try {
                        String text = new String(books.getContents().get(currentChapter).getData(),"UTF-8");
                        mWebView.loadData(text,"text/html;charset=UTF-8",null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
            else if(x<0 && Math.abs(y)<Math.abs(x)){
                Log.w(TAG, "onFling: "+"left" );
                currentPage--;
                goToNextOrBackPage(mWebView,PREV);
            }
            return true;
        }
    }

}

