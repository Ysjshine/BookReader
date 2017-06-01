package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;

import java.io.File;
import java.util.List;

import com.buaa.yushijie.bookreader.Services.AsyncTaskLoadImg;
import bean.BookBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookFragment extends Fragment {
    private static final String TAG = "BookFragment";
    private RecyclerView mRecyclerView;
    private RadioButton readCountRadioButton;
    private RadioButton collectCountRadioButton;

    private BookAdapter mBookAdapter;
    private File cache;
    private List<BookBean> booklist = null;

    private static final int SORT_BY_READ = 0;
    private static final int SORT_BY_COLLECT = 1;

    //query string or id
    private String query;
    private int categoryID;

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                booklist = (List<BookBean>) msg.obj;
                mBookAdapter = new BookAdapter(sort(SORT_BY_READ,booklist));
                mRecyclerView.setAdapter(mBookAdapter);
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = new File(getActivity().getCacheDir(), "cache");
        Log.e(TAG, "onCreate: "+cache );
        if (!cache.exists()) {
            cache.mkdirs();
            Log.e(TAG, "onCreate: "+cache );
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_recycleview, container, false);
        readCountRadioButton = (RadioButton)v.findViewById(R.id.book_list_recyclerview_read_cunt_button);
        collectCountRadioButton = (RadioButton)v.findViewById(R.id.book_list_recyclerview_collection_cunt_button);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.book_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownLoadBookInfoService service = new DownLoadBookInfoService();
                List<BookBean> bookLists = null;
                try {
                    if(query !=null) {
                        bookLists = service.getBookInfo(query);
                    }else if(categoryID != -1){
                        bookLists = service.getBookInfoByCategroyID(categoryID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bookLists;
                handler.sendMessage(msg);
            }
        }).start();

        readCountRadioButton.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"checked");
                if(isChecked == true){
                    mBookAdapter = new BookAdapter(sort(SORT_BY_READ,booklist));
                    mRecyclerView.setAdapter(mBookAdapter);
                }
            }
        });

        collectCountRadioButton.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"checked");
                if(isChecked == true){
                    mBookAdapter = new BookAdapter(sort(SORT_BY_COLLECT,booklist));
                    mRecyclerView.setAdapter(mBookAdapter);
                }
            }
        });

        return v;
    }
    //sort

    private List<BookBean> sort(int flag,List<BookBean> books){
        int size = books.size();
        if(flag == SORT_BY_READ){
            for(int i=0;i<size;i++){
                for(int j=i+1;j<size;j++){
                    if(books.get(i).readTimes<books.get(j).readTimes){
                        BookBean tmp = books.get(i);
                        books.set(i,books.get(j));
                        books.set(j,tmp);
                    }
                }
            }
        }else if(flag == SORT_BY_COLLECT){
            for(int i=0;i<size;i++){
                for(int j=i+1;j<size;j++){
                    if(books.get(i).collectTimes<books.get(j).collectTimes){
                        BookBean tmp = books.get(i);
                        books.set(i,books.get(j));
                        books.set(j,tmp);
                    }
                }
            }
        }
        return books;
    }

    //holder
    private class BookHolder extends RecyclerView.ViewHolder {
        private ImageView mBookCoverImageView;
        private TextView mBookTitleTextView;
        private TextView mBookAuthorNameTextView;
        private TextView mBookReadCountTextView;
        private TextView mBookCollectionCountTextView;
        private BookBean book;

        public BookHolder(View itemView) {
            super(itemView);
            mBookCoverImageView = (ImageView) itemView.findViewById(R.id.book_cover);
            mBookTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
            mBookAuthorNameTextView = (TextView) itemView.findViewById(R.id.book_author_name);
            mBookReadCountTextView = (TextView) itemView.findViewById(R.id.book_read_count);
            mBookCollectionCountTextView = (TextView) itemView.findViewById(R.id.book_collection_count);

            //click and jump to the book detail activity
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), BookDetailActivity.class);
                    i.putExtra("BOOKITEM",book);
                    startActivity(i);
                }
            });
        }

        public BookBean getBook() {
            return book;
        }

        public void setBook(BookBean book) {
            this.book = book;
        }

        public void bindBooksData() {
            mBookTitleTextView.setText(book.title);
            mBookAuthorNameTextView.setText("作者：" + book.author);
            mBookReadCountTextView.setText("阅读数:" + book.readTimes);
            mBookCollectionCountTextView.setText("收藏数:" + book.collectTimes);

            //download book cover
            DownLoadBookInfoService service = new DownLoadBookInfoService();
            AsyncTaskLoadImg task = new AsyncTaskLoadImg(service,mBookCoverImageView,cache);
            task.execute(book.imgSource);
        }
    }

    //adapter
    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<BookBean> mBooksLib;

        public BookAdapter(List<BookBean> books) {
            mBooksLib = books;
        }

        @Override
        public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.book_fragment, parent, false);
            return new BookHolder(v);
        }

        @Override
        public void onBindViewHolder(BookHolder holder, int position) {
            BookBean book = mBooksLib.get(position);
            holder.setBook(book);
            holder.bindBooksData();
        }

        @Override
        public int getItemCount() {
            return mBooksLib.size();
        }
    }
}
