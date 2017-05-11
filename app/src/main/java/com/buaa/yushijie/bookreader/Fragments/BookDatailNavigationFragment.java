package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
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

public class BookDatailNavigationFragment extends Fragment {

    private Button backButton;
    private Button shareButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_datail_head_navigation_layout,container,false);
        backButton = (Button)v.findViewById(R.id.book_datail_navigation_back);
        shareButton = (Button)v.findViewById(R.id.book_datail_navigation_share);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                //not solve yet
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.putExtra("sss","sss");
                startActivity(i);
            }
        });
        return v;
    }
}
