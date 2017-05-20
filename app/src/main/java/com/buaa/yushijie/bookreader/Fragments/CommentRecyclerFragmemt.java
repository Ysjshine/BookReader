package com.buaa.yushijie.bookreader.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.JavaBean.Comment;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.DownLoadCommentService;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import bean.BookBean;
import bean.CommentBean;
import bean.UserBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class CommentRecyclerFragmemt extends Fragment{

    private RecyclerView mRecyclerView;
    private CommentAdapter mAdpater;
    private BookBean currentBook;
    private DownLoadCommentService service;

    private ArrayList<CommentBean> commentBeanArrayList = null;
    private ArrayList<UserBean> userBeanArrayList = null;

    List<Comment> commentList = new ArrayList<>();
    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new DownLoadCommentService(currentBook);


        //get user and comment info
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    commentBeanArrayList = service.getCommentInfo();
                    for(CommentBean cb:commentBeanArrayList){
                        userBeanArrayList.add(service.getUserInfoById(cb.UserID));
                    }
                }catch (Exception e){
                    if(e instanceof ConnectException){
                        //timeout
                    }
                    e.printStackTrace();
                }
            }
        }).start();
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
        for(int i=0;i<userBeanArrayList.size();i++){
            Comment c = new Comment();
            c.setCommentContent("     "+commentBeanArrayList.get(i).contents);
            c.setUsername(userBeanArrayList.get(i).nickname);
            c.setUserID(userBeanArrayList.get(i).UserID);
            c.setCommentID(commentBeanArrayList.get(i).CommentID);
            commentList.add(c);
        }
        mAdpater = new CommentAdapter(commentList);
        mRecyclerView.setAdapter(mAdpater);

    }

    private class CommentHolder extends RecyclerView.ViewHolder{
        private TextView mUserNameTextView;
        private TextView mCommentContentTextView;
        public CommentHolder(View itemView) {
            super(itemView);
            mUserNameTextView = (TextView)itemView.findViewById(R.id.comment_username);
            mCommentContentTextView = (TextView)itemView.findViewById(R.id.comment_content);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //pop delete dialog
                    return false;
                }
            });
        }

        public void bindCommentData(Comment c) {
            mUserNameTextView.setText(c.getUsername());
            mCommentContentTextView.setText(c.getCommentContent());
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
