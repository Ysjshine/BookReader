package com.buaa.yushijie.bookreader.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.buaa.yushijie.bookreader.Fragments.BookDatailFragment;
import com.buaa.yushijie.bookreader.Fragments.BookDatailNavigationFragment;
import com.buaa.yushijie.bookreader.Fragments.CommentRecyclerFragmemt;
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;
import com.buaa.yushijie.bookreader.R;

import bean.BookBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookDetailActivity extends FragmentActivity {
    private BookDatailFragment bookDatailFragment = new BookDatailFragment();
    private final static String BOOKKEY="BOOKITEM";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_datail_layout);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.navigation_book_datail_container,new BookDatailNavigationFragment())
                .commit();
        BookBean book = (BookBean) getIntent().getSerializableExtra(BOOKKEY);
        bookDatailFragment.setBook(book);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.book_detail_fragment_container,bookDatailFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_recyle_list,new CommentRecyclerFragmemt())
                .commit();

    }
}
