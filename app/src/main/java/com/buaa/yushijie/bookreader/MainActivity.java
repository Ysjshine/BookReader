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
import com.buaa.yushijie.bookreader.Fragments.NavigationFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment navigationFragement = new NavigationFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    getSupportFragmentManager().beginTransaction()
                            .show(navigationFragement).commit();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.book_category_container,new BookCategoryFragment())
                            .commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction()
                            .hide(navigationFragement).commit();
                    FragmentManager fm1 = getSupportFragmentManager();
                    fm1.beginTransaction().replace(R.id.book_category_container,new BookFragment()).commit();

                   // mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    Intent i = new Intent(MainActivity.this, BookListActivity.class);
                    startActivity(i);
                    //mTextMessage.setText(R.string.title_notifications);
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
                .add(R.id.navigation_container,navigationFragement)
                .commit();
    }

}
