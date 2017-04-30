package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.JavaBean.Comment;
import com.buaa.yushijie.bookreader.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushijie on 17-4-30.
 */

public class CommentRecyclerFragmemt extends Fragment{

    private RecyclerView mRecyclerView;
    private CommentAdapter mAdpater;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.comment_recycle_list_layout,container,false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.comment_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI(){
        List<Comment> commentList = new ArrayList<Comment>();
        for(int i=0;i<5;i++){
            Comment c = new Comment();
            c.setCommentContent("     我觉得这本书写得还可以，值得一看，获益颇多，受益匪浅");
            c.setUsername("忽如远行客");
            commentList.add(c);
        }
        mAdpater = new CommentAdapter(commentList);
        mRecyclerView.setAdapter(mAdpater);

    }

    private class CommentHolder extends RecyclerView.ViewHolder{
        private TextView mUserNameTextView;
        private TextView mCommentContenTextView;
        public CommentHolder(View itemView) {
            super(itemView);
            mUserNameTextView = (TextView)itemView.findViewById(R.id.comment_username);
            mCommentContenTextView = (TextView)itemView.findViewById(R.id.comment_content);
        }

        public void bindCommentData(Comment c) {
            mUserNameTextView.setText(c.getUsername());
            mCommentContenTextView.setText(c.getCommentContent());
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder>{
        List<Comment> commentList;

        public CommentAdapter(List<Comment> Co) {
            commentList = Co;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.comment_fragemt,parent,false);
            return new CommentHolder(v);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            Comment tp = commentList.get(position);
            holder.bindCommentData(tp);
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }
}
