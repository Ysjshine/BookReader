package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookCategoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_book_city,container,false);
        return v;
    }

}
