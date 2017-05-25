package com.buaa.yushijie.bookreader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.buaa.yushijie.bookreader.Fragments.BookFragment;
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;
import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookListActivity extends FragmentActivity {
    private String query;
    private int categoryID;
    private static final String GET_QUERY_STRING = "query";
    private static final String CATEGORY_ID="CategoryID";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_fragment_recycle_layout);
        query = getIntent().getStringExtra(GET_QUERY_STRING);
        categoryID = getIntent().getIntExtra(CATEGORY_ID,-1);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.navigation_container_book_list,new NavigationFragment())
                .commit();
        BookFragment bookFragment = new BookFragment();
        bookFragment.setQuery(query);
        bookFragment.setCategoryID(categoryID);
        fm.beginTransaction()
                .replace(R.id.book_list_container,bookFragment)
                .commit();
    }
}
