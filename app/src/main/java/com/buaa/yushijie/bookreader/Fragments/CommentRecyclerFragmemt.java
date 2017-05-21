package com.buaa.yushijie.bookreader.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.buaa.yushijie.bookreader.Services.CurrentUser;
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
    private Activity currentActivity;
    private ArrayList<CommentBean> commentBeanArrayList = null;

    public void setCurrentBook(BookBean currentBook) {
        this.currentBook = currentBook;
    }

    public void setCommentBeanArrayList(ArrayList<CommentBean> commentBeanArrayList) {
        this.commentBeanArrayList = commentBeanArrayList;
    }

    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what  == 1) {
                commentBeanArrayList = (ArrayList<CommentBean>) msg.obj;
                mAdpater = new CommentAdapter(commentBeanArrayList);
                mRecyclerView.setAdapter(mAdpater);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.comment_recycle_list_layout,container,false);
        currentActivity = getActivity();
        mRecyclerView = (RecyclerView)v.findViewById(R.id.comment_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return v;
    }

    private void updateUI(){
            //get comment info
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    DownLoadCommentService service = new DownLoadCommentService(currentBook);
                    Message msg = new Message();
                    msg.obj = service.getCommentInfo();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    if(e instanceof ConnectException){
                        //timeout
                    }
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class CommentHolder extends RecyclerView.ViewHolder{
        private TextView mUserNameTextView;
        private TextView mCommentContentTextView;
        private CommentBean commentBean;

        public void setCommentBean(CommentBean commentBean) {
            this.commentBean = commentBean;
        }

        public CommentHolder(View itemView) {
            super(itemView);
            mUserNameTextView = (TextView)itemView.findViewById(R.id.comment_username);
            mCommentContentTextView = (TextView)itemView.findViewById(R.id.comment_content);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //pop delete dialog
                    int x = ((CurrentUser)currentActivity.getApplication()).getUser().UserID;
                    if(commentBean.UserID == x ){
                        DeleteACommentDialogFragment dialogFragment = new DeleteACommentDialogFragment();
                        dialogFragment.setCommentBean(commentBean);
                        dialogFragment.show(getFragmentManager(),"Delete");
                    }
                    return false;
                }
            });
        }

        public void bindCommentData() {
            mUserNameTextView.setText(commentBean.nickname);
            mCommentContentTextView.setText(commentBean.contents);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder>{
        List<CommentBean> commentList;

        public CommentAdapter(List<CommentBean> Co) {
            commentList = Co;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.comment_fragemt,parent,false);
            return new CommentHolder(v);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            CommentBean tp = commentList.get(position);
            holder.setCommentBean(tp);
            holder.bindCommentData();
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }
}
