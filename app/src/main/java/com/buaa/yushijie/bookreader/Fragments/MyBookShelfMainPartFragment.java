package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.AsynTaskLoadImg;
import com.buaa.yushijie.bookreader.Services.CurrentUser;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;
import com.buaa.yushijie.bookreader.Services.DownLoadMyBookShelfService;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import bean.BookBean;
import bean.UserBean;
import bean.UserCategory;

/**
 * Created by yushijie on 17-5-4.
 */

public class MyBookShelfMainPartFragment extends Fragment {
    private ExpandableListView mBookShelfExpandableListView;

    private ArrayList<ArrayList<BookBean>> bookItemsLists = new ArrayList<>();
    private  ArrayList<UserCategory> categoryGroupNames = new ArrayList<>();
    private DownLoadMyBookShelfService service = new DownLoadMyBookShelfService();
    private File cache;

    private static final String TAG="MyBookShelfMainPart";


    public void setCategoryGroupNames(ArrayList<UserCategory> categoryGroupNames) {
        this.categoryGroupNames = categoryGroupNames;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bookItemsLists.add((ArrayList<BookBean>)msg.obj);
            mBookShelfExpandableListView.setAdapter(new ExpandableListViewAdapter(categoryGroupNames,bookItemsLists));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cache = new File(getActivity().getCacheDir(), "cache");
        if (!cache.exists()) {
            cache.mkdirs();
            Log.e(TAG, "onCreate: "+cache );
        }
    }

    //get every category's books
    private class DownLoadBookBeanOfEveryCategoryThread extends Thread{
        ArrayList<UserCategory> category;
        public DownLoadBookBeanOfEveryCategoryThread(ArrayList<UserCategory> uc){
            category = uc;
        }

        @Override
        public void run() {
            try{
                UserBean ub = ((CurrentUser) getActivity().getApplication()).getUser();
                for(int i=0;i<category.size();i++) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = service.getBookOfEveryCategory(category.get(i), ub);

                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //get every category book
    public void getData(){
        categoryGroupNames = ((CurrentUser)getActivity().getApplication()).getUserCategories();
           DownLoadBookBeanOfEveryCategoryThread t =
                   new DownLoadBookBeanOfEveryCategoryThread(categoryGroupNames);
            t.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_book_shelf_fragment,container,false);
        mBookShelfExpandableListView = (ExpandableListView)v.findViewById(R.id.my_book_shelf_list);
        mBookShelfExpandableListView.setGroupIndicator(null);

        //click category event
        mBookShelfExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(bookItemsLists.get(groupPosition).isEmpty())
                    return true;
                return false;
            }
        });

        //click book event
        mBookShelfExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent i = new Intent(getActivity(), BookDetailActivity.class);
                i.putExtra("BOOKITEM",bookItemsLists.get(groupPosition).get(childPosition));
                startActivity(i);
                return false;
            }
        });

        getData();
        return v;
    }



    //adapter
    private class ExpandableListViewAdapter extends BaseExpandableListAdapter{
        private ArrayList<UserCategory> categoryGroupName;
        private ArrayList<ArrayList<BookBean>> bookItemsList;

        public ExpandableListViewAdapter(ArrayList<UserCategory> ucList,ArrayList<ArrayList<BookBean>> bookList){
            categoryGroupName = ucList;
            bookItemsList = bookList;

        }
        @Override
        public int getGroupCount() {
            return categoryGroupName.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return  bookItemsList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return categoryGroupName.get(groupPosition);

        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return bookItemsList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            CategoryHolder categoryHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.my_book_shelf_fragment_group_list,parent,false);
                categoryHolder = new CategoryHolder();
                categoryHolder.categoryName =(TextView) convertView.findViewById(R.id.my_book_shelf_group_list);
                convertView.setTag(categoryHolder);
            }else{
                categoryHolder = (CategoryHolder)convertView.getTag();
            }
            categoryHolder.categoryName.setText(categoryGroupName.get(groupPosition).CategoryName);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            BookItemHolder bookItemHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.my_book_shelf_fragment_item_list,parent,false);
                bookItemHolder = new BookItemHolder();
                bookItemHolder.cover = (ImageView)convertView.findViewById(R.id.my_book_shelf_book_cover);
                bookItemHolder.bookTitle = (TextView)convertView.findViewById(R.id.my_book_shelf_book_title);
                bookItemHolder.bookAuthor = (TextView)convertView.findViewById(R.id.my_book_shelf_book_author_name);
                convertView.setTag(bookItemHolder);
            }else{
                bookItemHolder = (BookItemHolder)convertView.getTag();
            }
            bookItemHolder.bookAuthor.setText(bookItemsList.get(groupPosition).get(childPosition).author);
            bookItemHolder.bookTitle.setText(bookItemsList.get(groupPosition).get(childPosition).title);
           // Log.e(TAG, "getChildView: "+bookItemsList.get(groupPosition).get(childPosition).author);

            DownLoadBookInfoService service1 = new DownLoadBookInfoService();
            AsynTaskLoadImg task = new AsynTaskLoadImg(service1,bookItemHolder.cover,cache);
            task.execute(bookItemsList.get(groupPosition).get(childPosition).imgSource);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    //bookItems
    private class BookItemHolder{
        public ImageView cover;
        public TextView bookTitle;
        public TextView bookAuthor;
    }
    //Category
    private class CategoryHolder{
        public TextView categoryName;
    }

}
