package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.buaa.yushijie.bookreader.Activities.BookReadingActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.CurrentApplication;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;
import com.buaa.yushijie.bookreader.Services.SQLUpload;
import com.buaa.yushijie.bookreader.UI.MyWebView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bean.BookBean;
import bean.UserBean;
import nl.siegmann.epublib.domain.Book;

/**
 * Created by yushijie on 17-5-25.
 */

public class BookReadFragment extends Fragment {
    private MyWebView mWebView;
    private static final String TAG="BookReadFragment";
    private Activity currentActivity;
    private Context context;
    private Book books;
    private GestureDetector gesture = null;

    private BookBean currentBook;
    private int currentChapter =0 ;
    private int currentPage =0;

    private static final int NEXT=1;
    private static final int PREV=-1;

    private float htmlWidth=0;
    private int pageCount=0;
    private boolean isBackFlag = false;


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
            }else if(msg.what == 1){
                ArrayList<Integer> process = (ArrayList<Integer>)msg.obj;
                currentChapter = process.get(0);
                currentPage = process.get(1);
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
        context = getContext();
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

    @Override
    public void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CurrentApplication currentApplication = (CurrentApplication)currentActivity.getApplication();
                UserBean user = currentApplication.getUser();
                try{
                    SQLUpload.sendProcessOfBook(user,currentBook,currentChapter,currentPage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        super.onDestroy();
    }

    private void getBookData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CurrentApplication currentApplication = (CurrentApplication)currentActivity.getApplication();
                UserBean user = currentApplication.getUser();
                DownLoadBookInfoService service = new DownLoadBookInfoService();
                try{
                    ArrayList<Integer> process =service.getProcess(user,currentBook);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = process;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {

                    Book book = service.getEPUBBook(currentBook.fileSource,currentActivity);
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
        mWebView.setBackgroundColor(Color.rgb(255,250,205));
        mWebView.getSettings().setDefaultFontSize(18);
        mWebView.getSettings().setSerifFontFamily("微软雅黑");

    }

    //load data
    private void loadDataToWebView() throws Exception{
        String text = new String(books.getContents().get(currentChapter).getData(),"UTF-8");
        mWebView.loadData(text,"text/html;charset=UTF-8",null);

    }

    //pagination
    private void createPagesView(WebView view) {
        String javascriptCodeOfPage = "javascript:function initialize() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourH = window.innerHeight; " +
                "var ourW = window.innerWidth; " +
                "var fullH = d.offsetHeight; " +
                "var pageCount = Math.floor(fullH/ourH)+1;" +
                "var newW = pageCount*ourW; " +
                "d.style.height = (ourH-50)+'px';" +
                "d.style.width = newW+'px';" +
                "d.style.webkitColumnCount = pageCount;" +
                "}";
        view.loadUrl(javascriptCodeOfPage);
        view.loadUrl("javascript:initialize()");

        if(isBackFlag){
            flipBack(mWebView);
        }else {
            flipToHistoryLocation(mWebView,currentPage-1);
        }
    }

    //
    private void flipToHistoryLocation(WebView view,int currentPage){
        String javascriptCodeForScrollTo = "javascript:function ScrollTo(){"+
                "var ourW = window.innerWidth; " +
                "window.scrollTo("+currentPage+"*ourW,0)"+
                "}";
        view.loadUrl(javascriptCodeForScrollTo);
        view.loadUrl("javascript:ScrollTo()");
    }

    //flip back
    private void flipBack(WebView view){
        String javascriptCodeForScrollTo = "javascript:function back(){"+
                "var d = document.getElementsByTagName('body')[0];" +
                "window.scrollTo(1000000,0)"+
                "}";
        view.loadUrl(javascriptCodeForScrollTo);
        view.loadUrl("javascript:back()");
    }

    //flip pages
    private void goToNextOrBackPage(WebView view,int pos) {
        String javascriptCode = "javascript:function nextPage() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourW = window.innerWidth+1; " +
                "window.scrollBy("+pos+"*ourW,0)"+
                "}";
        view.loadUrl(javascriptCode);
        view.loadUrl("javascript:nextPage()");
    }

    //gesture listener
    private class GestureListeners  extends GestureDetector.SimpleOnGestureListener
            implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            htmlWidth = mWebView.getContentWidth();
            pageCount = (int)Math.floor(htmlWidth/mWebView.getWidth());
            Log.e(TAG, "onTouch: "+htmlWidth+" "+pageCount );
            if(isBackFlag){
                currentPage = pageCount;
                isBackFlag = false;
            }
            return gesture.onTouchEvent(event)||(event.getAction() == MotionEvent.ACTION_MOVE);
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e1.getX()-e2.getX();
            float y = e1.getY()-e2.getY();
            if(x>0 && Math.abs(y)<x) {
                Log.w(TAG, "onFling: "+"right" );
                jumpToNextPage();
            } else if(x<0 && Math.abs(y)<Math.abs(x)){
                Log.w(TAG, "onFling: "+"left" );
                jumpToPrevPage();

            }else if(y>0){
                Log.e(TAG, "onFling: "+"up" );
                BookReadSettingFragmentDialog dialog = new BookReadSettingFragmentDialog();
                dialog.setmWebView(mWebView);
                dialog.show(getFragmentManager(),"TAG");
            }
            return true;
        }
    }


    private void jumpToNextPage(){
        if(currentPage<pageCount) {
            currentPage++;
            goToNextOrBackPage(mWebView,NEXT);
        }else if(currentPage==pageCount){
            if(currentChapter+1==books.getContents().size()){
                Toast.makeText(currentActivity,"已经看完了哦！",Toast.LENGTH_SHORT).show();
                return;
            }
            currentPage = 1;
            currentChapter++;
            htmlWidth = 0;
            pageCount = 0;
            try {
                loadDataToWebView();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void jumpToPrevPage(){
        if(currentPage > 1){
            currentPage--;
            goToNextOrBackPage(mWebView,PREV);
        }else if(currentPage == 1){
            if(currentChapter == 0){
                Toast.makeText(currentActivity,"已经在最前面了哦！",Toast.LENGTH_SHORT).show();
                return;
            }
            currentChapter--;
            htmlWidth = 0;
            pageCount = 0;
            isBackFlag = true;
            try{
                loadDataToWebView();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}

