package com.buaa.yushijie.bookreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;
import com.buaa.yushijie.bookreader.Activities.BookListActivity;
import com.buaa.yushijie.bookreader.Fragments.BookCategoryFragment;
import com.buaa.yushijie.bookreader.Fragments.BookFragment;
import com.buaa.yushijie.bookreader.Fragments.MyBookShelfMainPartFragment;
import com.buaa.yushijie.bookreader.Fragments.MyBookShelfNavigationFragment;
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment cityNavigationFragment = new NavigationFragment();
    private Fragment bookshelfNavigationFragment = new MyBookShelfNavigationFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navigation_container,cityNavigationFragment)
                            .commit();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_category_container,new BookCategoryFragment())
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navigation_container,bookshelfNavigationFragment)
                            .commit();
                    FragmentManager fm1 = getSupportFragmentManager();
                    fm1.beginTransaction().replace(R.id.book_category_container,new MyBookShelfMainPartFragment()).commit();

                    return true;
                case R.id.navigation_notifications:
                    Intent i = new Intent(MainActivity.this, BookListActivity.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.book_category_container,new BookCategoryFragment())
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.navigation_container,cityNavigationFragment)
                .commit();
    }

}
