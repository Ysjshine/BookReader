package com.buaa.yushijie.bookreader.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buaa.yushijie.bookreader.Activities.BookDetailActivity;
import com.buaa.yushijie.bookreader.Activities.BookListActivity;
import com.buaa.yushijie.bookreader.R;
import com.buaa.yushijie.bookreader.Services.AsynTaskLoadImg;
import com.buaa.yushijie.bookreader.Services.DownLoadBookInfoService;
import com.buaa.yushijie.bookreader.Services.DownLoadCommentService;
import com.buaa.yushijie.bookreader.Services.DownLoadIndexInfo;

import java.io.File;
import java.util.ArrayList;

import bean.BookBean;

/**
 * Created by yushijie on 17-4-30.
 */

public class BookCategoryFragment extends Fragment {
    private ArrayList<BookBean> bookBeanArrayList;
    private ArrayList<String> urlArrayList;
    private ArrayList<ImageView> imageViewArrayList = new ArrayList<>();

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

    private ImageView categoryView1;
    private ImageView categoryView2;
    private ImageView categoryView3;
    private ImageView categoryView4;
    private ImageView categoryView5;
    private ImageView categoryView6;
    private ImageView categoryView7;
    private ImageView categoryView8;

    private File cache;
    private static final String TAG="Index";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            DownLoadBookInfoService service = new DownLoadBookInfoService();
            urlArrayList = (ArrayList<String>)msg.obj;
            for (int i = 0; i < 3; i++) {
                imageViewArrayList.get(i).setOnClickListener(new ImageClickListener());
                AsynTaskLoadImg task = new AsynTaskLoadImg(service, imageViewArrayList.get(i), cache);
                task.execute(urlArrayList.get(i));
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = new File(getActivity().getCacheDir(), "cache");
        if (!cache.exists()) {
            cache.mkdirs();
            Log.e(TAG, "onCreate: " + cache);
        }
        getData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_book_city, container, false);
        imageView1 = (ImageView) v.findViewById(R.id.imageViewR1_1);
        imageView2 = (ImageView) v.findViewById(R.id.imageViewR2_1);
        imageView3 = (ImageView) v.findViewById(R.id.imageViewR3_1);

        categoryView1 = (ImageView) v.findViewById(R.id.imageView1);
        categoryView2 = (ImageView) v.findViewById(R.id.imageView2);
        categoryView3 = (ImageView) v.findViewById(R.id.imageView3);
        categoryView4 = (ImageView) v.findViewById(R.id.imageView4);
        categoryView5 = (ImageView) v.findViewById(R.id.imageView5);
        categoryView6 = (ImageView) v.findViewById(R.id.imageView6);
        categoryView7 = (ImageView) v.findViewById(R.id.imageView7);
        categoryView8 = (ImageView) v.findViewById(R.id.imageView8);

        categoryView1.setOnClickListener(new CategoryClickListener());
        categoryView2.setOnClickListener(new CategoryClickListener());
        categoryView3.setOnClickListener(new CategoryClickListener());
        categoryView4.setOnClickListener(new CategoryClickListener());
        categoryView5.setOnClickListener(new CategoryClickListener());
        categoryView6.setOnClickListener(new CategoryClickListener());
        categoryView7.setOnClickListener(new CategoryClickListener());
        categoryView8.setOnClickListener(new CategoryClickListener());

        imageViewArrayList.add(imageView1);
        imageViewArrayList.add(imageView2);
        imageViewArrayList.add(imageView3);


        return v;

    }

    private class ImageClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            try {
                BookBean b = null;
                switch (v.getId()) {
                    case R.id.imageViewR1_1:
                        b = bookBeanArrayList.get(0);
                        break;
                    case R.id.imageViewR2_1:
                        b = bookBeanArrayList.get(1);
                        break;
                    case R.id.imageViewR3_1:
                        b = bookBeanArrayList.get(2);
                        break;
                    default:
                        b = bookBeanArrayList.get(0);
                        break;
                }
                Intent i = new Intent(getActivity(), BookDetailActivity.class);
                i.putExtra("BOOKITEM", b);
                startActivity(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class CategoryClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            int categoryId;
            switch (v.getId()){
                case R.id.imageView1:
                    categoryId = 1;
                    break;
                case R.id.imageView2:
                    categoryId = 2;
                    break;
                case R.id.imageView3:
                    categoryId = 3;
                    break;
                case R.id.imageView4:
                    categoryId = 4;
                    break;
                case R.id.imageView5:
                    categoryId = 5;
                    break;
                case R.id.imageView6:
                    categoryId = 6;
                    break;
                case R.id.imageView7:
                    categoryId = 7;
                    break;
                case R.id.imageView8:
                    categoryId = 8;
                    break;
                default:
                    categoryId =1;
                    break;
            }
            Intent i = new Intent(getActivity(),BookListActivity.class);
            i.putExtra("CategoryID",categoryId);
            startActivity(i);
        }
    }

    public void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    DownLoadIndexInfo service = new DownLoadIndexInfo();
                    urlArrayList = service.getURL();
                    bookBeanArrayList = service.getTop3Book();
                    Message msg = new Message();
                    msg.obj = urlArrayList;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
