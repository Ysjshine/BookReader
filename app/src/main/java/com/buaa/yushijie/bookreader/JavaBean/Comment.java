package com.buaa.yushijie.bookreader.JavaBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class Comment {
    private String username;
    private String commentContent;
    private int userID;
    private int commentID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent){
        this.commentContent = commentContent;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }
}
