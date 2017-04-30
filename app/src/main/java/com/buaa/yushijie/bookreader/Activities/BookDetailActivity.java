package com.buaa.yushijie.bookreader.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.buaa.yushijie.bookreader.Fragments.BookDatailFragment;
import com.buaa.yushijie.bookreader.Fragments.CommentRecyclerFragmemt;
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;
import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookDetailActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_datail_layout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.navigation_book_datail_container,new NavigationFragment())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.book_detail_fragment_container,new BookDatailFragment())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_recyle_list,new CommentRecyclerFragmemt())
                .commit();

    }
}
