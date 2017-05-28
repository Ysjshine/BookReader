package com.buaa.yushijie.bookreader.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.buaa.yushijie.bookreader.Fragments.BookReadFragment;
import com.buaa.yushijie.bookreader.R;


import java.io.File;

import bean.BookBean;
import nl.siegmann.epublib.domain.Book;


/**
 * Created by yushijie on 17-5-25.
 */

public class BookReadingActivity extends AppCompatActivity {
    private BookBean currentBook;
    private static final String GET_CURRENT_BOOK ="CurrentBook";
    private static final String TAG = "BookReadActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reading_activity);
        currentBook =(BookBean) getIntent().getSerializableExtra(GET_CURRENT_BOOK);
        BookReadFragment bookReadFragment = new BookReadFragment();
        bookReadFragment.setCurrentBook(currentBook);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.book_reading_webview_container,bookReadFragment)
                .commit();
    }
}
