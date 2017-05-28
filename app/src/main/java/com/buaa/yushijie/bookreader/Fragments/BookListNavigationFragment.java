package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.R;

/**
 * Created by yushijie on 17-5-28.
 */

public class BookListNavigationFragment extends Fragment {
    private Button backButton;
    private TextView titleTextView;
    private String text = "";

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_navigation_fragment,container,false);
        backButton = (Button)v.findViewById(R.id.book_list_head_navigation_back);
        titleTextView = (TextView)v.findViewById(R.id.book_list_title_text_view);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        titleTextView.setText(text);
        return v;
    }
}
