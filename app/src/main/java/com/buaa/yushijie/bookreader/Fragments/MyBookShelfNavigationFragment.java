package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-5-1.
 */

public class MyBookShelfNavigationFragment extends Fragment {

    private Button addCategoryButton;
    private static final String SET_CATEGORY = "TAG";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_book_shelf_head_navigation_fragment_layout,container,false);
        addCategoryButton = (Button)v.findViewById(R.id.my_book_shelf_add_category);
        addCategoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CategorySetDialogFragment dialog = new CategorySetDialogFragment();
                dialog.show(getFragmentManager(),SET_CATEGORY);

            }
        });
        return v;
    }
}
