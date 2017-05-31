package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buaa.yushijie.bookreader.MainActivity;
import com.buaa.yushijie.bookreader.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import bean.BookBean;

/**
 * Created by yushijie on 17-5-1.
 */

public class BookDatailNavigationFragment extends Fragment {
    private Tencent mTencent;
    private Button backButton;
    private Button shareButton;
    private Activity currentActivity;
    private BookBean currentBook;

    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_datail_head_navigation_layout,container,false);
        backButton = (Button)v.findViewById(R.id.book_datail_navigation_back);
        shareButton = (Button)v.findViewById(R.id.book_datail_navigation_share);
        mTencent = Tencent.createInstance("1106195100",getContext().getApplicationContext());
        currentActivity = getActivity();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IUiListener myListener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {

                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                };

                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, currentBook.title);
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  currentBook.author);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://120.25.89.166/BookReaderServer/Chapter?bid="+currentBook.BookID+"&chapter=2");
                //params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"https://www.baidu.com/img/bd_logo1.png");
                mTencent.shareToQQ(currentActivity, params, myListener);
            }
        });
        return v;
    }


}
