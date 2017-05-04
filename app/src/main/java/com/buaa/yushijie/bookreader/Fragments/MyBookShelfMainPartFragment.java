package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.ExpandedMenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;
import com.buaa.yushijie.bookreader.JavaBean.Books;
import com.buaa.yushijie.bookreader.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by yushijie on 17-5-4.
 */

public class MyBookShelfMainPartFragment extends Fragment {
    private ExpandableListView mBookShelfExpandableListView;
    private ArrayList<Books> bookItems;
    private ArrayList<ArrayList<Books>> bookItemsList = new ArrayList<>();
    private ArrayList<String> categoryGroupName = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void getData(){
        categoryGroupName.add("我喜欢的书");
        categoryGroupName.add("文学风");
       for(int i=0;i<2;i++){
           ArrayList<Books> book = new ArrayList<>();
           for(int j=0;j<3;j++){
               Books bs = new Books();
               bs.setCover("cover.png");
               bs.setAuthor("Matin");
               bs.setCollectionCount(50);
               bs.setReadCount(100);
               bs.setTitle("Head first to Java");
               book.add(bs);
           }
           bookItemsList.add(book);
       }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_book_shelf_fragment,container,false);
        mBookShelfExpandableListView = (ExpandableListView)v.findViewById(R.id.my_book_shelf_list);
        mBookShelfExpandableListView.setGroupIndicator(null);
        mBookShelfExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(bookItemsList.get(groupPosition).isEmpty())
                    return true;
                return false;
            }
        });

        mBookShelfExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                startActivity(new Intent(getActivity(), BookDetailActivity.class));
                return false;
            }
        });
        getData();
        mBookShelfExpandableListView.setAdapter(new ExpandableListViewAdapter());
        return v;
    }

    private class ExpandableListViewAdapter extends BaseExpandableListAdapter{
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
            categoryHolder.categoryName.setText(categoryGroupName.get(groupPosition));
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
            bookItemHolder.bookAuthor.setText(bookItemsList.get(groupPosition).get(childPosition).getAuthor());
            bookItemHolder.bookTitle.setText(bookItemsList.get(groupPosition).get(childPosition).getTitle());

            //获取图像资源
            AssetManager assetManager = getActivity().getAssets();
            InputStream in = null;
            try{
                in = assetManager.open(bookItemsList.get(groupPosition).get(childPosition).getCover());
            }catch (Exception e){
                try{
                    in = assetManager.open("cover.png");
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
            Bitmap bp = BitmapFactory.decodeStream(in);
            bookItemHolder.cover.setImageBitmap(bp);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private class BookItemHolder{
        public ImageView cover;
        public TextView bookTitle;
        public TextView bookAuthor;
    }

    private class CategoryHolder{
        public TextView categoryName;
    }

}
