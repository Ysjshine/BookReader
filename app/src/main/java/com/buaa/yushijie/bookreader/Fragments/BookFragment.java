package com.buaa.yushijie.bookreader.Fragments;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.JavaBean.Books;
import com.buaa.yushijie.bookreader.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;

    private void updateUI(){
        List<Books> b = new ArrayList<Books>();
        for(int i=0;i<100;i++) {
            Books bs = new Books();
            bs.setCover("cover.png");
            bs.setAuthor("Matin");
            bs.setCollectionCount(50);
            bs.setReadCount(100);
            bs.setTitle("Head first to Java");
            b.add(bs);
        }
        mBookAdapter = new BookAdapter(b);
        mRecyclerView.setAdapter(mBookAdapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_list_recycleview,container,false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.book_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private class BookHolder extends RecyclerView.ViewHolder{
        private ImageView mBookCoverImageView;
        private TextView mBookTitleTextView;
        private TextView mBookAuthorNameTextView;
        private TextView mBookReadCountTextView;
        private TextView mBookCollectionCountTextView;

        public BookHolder(View itemView) {
            super(itemView);
            mBookCoverImageView = (ImageView)itemView.findViewById(R.id.book_cover);
            mBookTitleTextView = (TextView)itemView.findViewById(R.id.book_title);
            mBookAuthorNameTextView = (TextView)itemView.findViewById(R.id.book_author_name);
            mBookReadCountTextView  =(TextView)itemView.findViewById(R.id.book_read_count);
            mBookCollectionCountTextView = (TextView)itemView.findViewById(R.id.book_collection_count);
        }

        public void bindBooksData(Books book){
            mBookTitleTextView.setText(book.getTitle());
            mBookAuthorNameTextView.setText("作者："+book.getAuthor());
            mBookReadCountTextView.setText("阅读数"+book.getReadCount());
            mBookCollectionCountTextView.setText("收藏数"+book.getCollectionCount());
            AssetManager assetManager = getActivity().getAssets();
            InputStream in = null;
            try{
                in = assetManager.open(book.getCover());
            }catch (Exception e){
                try{
                    in = assetManager.open("cover.png");
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
            Bitmap bp = BitmapFactory.decodeStream(in);
            mBookCoverImageView.setImageBitmap(bp);

        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Books> mBooksLib;

        public BookAdapter(List<Books> books) {
            mBooksLib = books;
        }

        @Override
        public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.book_fragment,parent,false);
            return new BookHolder(v);
        }

        @Override
        public void onBindViewHolder(BookHolder holder, int position) {
            Books book = mBooksLib.get(position);
            holder.bindBooksData(book);
        }

        @Override
        public int getItemCount() {
            return mBooksLib.size();
        }
    }
}
