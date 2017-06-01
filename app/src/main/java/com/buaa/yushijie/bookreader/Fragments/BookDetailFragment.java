package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.AsyncTaskLoadImg;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;

import java.io.File;

import bean.BookBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookDetailFragment extends Fragment {
    private ImageView cover;
    private TextView title;
    private TextView author;
    private TextView readCount;
    private TextView collectionCount;
    private File cache;
    private BookBean book;

    public void setBook(BookBean book) {
        this.book = book;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = new File(getActivity().getCacheDir(), "cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_datail_fragment,container,false);
        cover =(ImageView) v.findViewById(R.id.book_datail_book_cover);
        title = (TextView)v.findViewById(R.id.book_datail_book_title);
        author = (TextView)v.findViewById(R.id.book_datail_book_author_name);
        readCount = (TextView)v.findViewById(R.id.book_datail_book_read_count);
        collectionCount = (TextView)v.findViewById(R.id.book_datail_book_collection_count);

        title.setText(book.title);
        author.setText("作者：" + book.author);
        readCount.setText("阅读数:" + book.readTimes);
        collectionCount.setText("收藏数:" + book.collectTimes);

        DownLoadBookInfoService service = new DownLoadBookInfoService();
        AsyncTaskLoadImg task = new AsyncTaskLoadImg(service,cover,cache);
        task.execute(book.imgSource);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDetailCoverDialogFragment dialog = new BookDetailCoverDialogFragment();
                dialog.setCurrentBook(book);
                dialog.setCache(cache);
                dialog.show(getFragmentManager(),"CompletePicture");
            }
        });
        return v;
    }
}
