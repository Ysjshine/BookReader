package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;

import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bean.BookBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private File cache;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                List<BookBean> booklist = null;
                booklist = (List<BookBean>) msg.obj;
                mBookAdapter = new BookAdapter(booklist);
                mRecyclerView.setAdapter(mBookAdapter);
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = new File(Environment.getExternalStorageDirectory(), "cache");
        if (!cache.exists()) {
            cache.mkdirs();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_recycleview, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.book_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownLoadBookInfoService service = new DownLoadBookInfoService();
                List<BookBean> bookList = null;
                try {
                    bookList = service.getBookInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bookList;
                handler.sendMessage(msg);
            }
        }).start();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        File[] files = cache.listFiles();
        for (File file : files) {
            file.delete();
        }
        cache.delete();
    }

    private class BookHolder extends RecyclerView.ViewHolder {
        private ImageView mBookCoverImageView;
        private TextView mBookTitleTextView;
        private TextView mBookAuthorNameTextView;
        private TextView mBookReadCountTextView;
        private TextView mBookCollectionCountTextView;

        public BookHolder(View itemView) {
            super(itemView);
            mBookCoverImageView = (ImageView) itemView.findViewById(R.id.book_cover);
            mBookTitleTextView = (TextView) itemView.findViewById(R.id.book_title);
            mBookAuthorNameTextView = (TextView) itemView.findViewById(R.id.book_author_name);
            mBookReadCountTextView = (TextView) itemView.findViewById(R.id.book_read_count);
            mBookCollectionCountTextView = (TextView) itemView.findViewById(R.id.book_collection_count);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), BookDetailActivity.class));
                }
            });
        }

        public void bindBooksData(BookBean book) {
            mBookTitleTextView.setText(book.title);
            mBookAuthorNameTextView.setText("作者：" + book.author);
            mBookReadCountTextView.setText("阅读数:" + book.readTimes);
            mBookCollectionCountTextView.setText("收藏数:" + book.collectTimes);
            DownLoadBookInfoService service = new DownLoadBookInfoService();
            AsynTaskLoadImg task = new AsynTaskLoadImg(service,mBookCoverImageView);
            task.execute(book.imgSource);
        }
    }

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
            holder.bindBooksData(book);
        }

        @Override
        public int getItemCount() {
            return mBooksLib.size();
        }

    }

    private class AsynTaskLoadImg extends AsyncTask<String,Integer,Bitmap>{
        private DownLoadBookInfoService service = new DownLoadBookInfoService();
        private ImageView coverImage;

        public AsynTaskLoadImg(DownLoadBookInfoService service,ImageView coverImage) {
            super();
            this.service = service;
            this.coverImage = coverImage;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                return service.getImageURI(params[0],cache);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap uri) {
            super.onPostExecute(uri);
            coverImage.setImageBitmap(uri);
        }
    }
}
