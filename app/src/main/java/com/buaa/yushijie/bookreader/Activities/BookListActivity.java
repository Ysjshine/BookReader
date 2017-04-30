package com.buaa.yushijie.bookreader.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.buaa.yushijie.bookreader.Fragments.BookFragment;
import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookListActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_layout);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.book_list_container,new BookFragment()).commit();
    }
}
