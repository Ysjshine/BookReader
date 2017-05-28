package com.buaa.yushijie.bookreader.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.buaa.yushijie.bookreader.Fragments.BookFragment;
import com.buaa.yushijie.bookreader.Fragments.BookListNavigationFragment;
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;
import com.buaa.yushijie.bookreader.R;

import java.util.Map;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookListActivity extends FragmentActivity {
    private String query;
    private int categoryID;
    private static final String GET_QUERY_STRING = "query";
    private static final String CATEGORY_ID="CategoryID";
    private String[] dict = new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initDict();
        setContentView(R.layout.book_fragment_recycle_layout);
        query = getIntent().getStringExtra(GET_QUERY_STRING);
        categoryID = getIntent().getIntExtra(CATEGORY_ID,-1);
        FragmentManager fm = getSupportFragmentManager();
        BookListNavigationFragment fragment = new BookListNavigationFragment();
        fragment.setText(dict[categoryID]);
        fm.beginTransaction()
                .add(R.id.navigation_container_book_list,fragment)
                .commit();
        BookFragment bookFragment = new BookFragment();
        bookFragment.setQuery(query);
        bookFragment.setCategoryID(categoryID);
        fm.beginTransaction()
                .replace(R.id.book_list_container,bookFragment)
                .commit();
    }

    void initDict(){
        dict[1] = "青春励志";
        dict[2] = "社会科学";
        dict[3] = "历史人文";
        dict[4] = "散文诗词";
        dict[5] = "军事战争";
        dict[6] = "玄幻修真";
        dict[7] = "恐怖灵异";
        dict[8] = "工程技术";
    }
}
