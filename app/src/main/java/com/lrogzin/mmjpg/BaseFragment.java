package com.lrogzin.mmjpg;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;


/**
 * @author 1U02UN
 */
public class BaseFragment extends Fragment {

    private String tittle;
    private String category_url;
    private XRecyclerView rv;
    private NetData netData;
    private View view;
    private static String status="";
    private static int load_page;
    private List<PicBean> albumList_data;
    private IndexAlbumAdapter indexAlbumAdapter;

    public BaseFragment() {

    }



    public static BaseFragment newInstance(String tittle, String category_url) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString("tittle", tittle);
        args.putString("category_url", category_url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tittle = getArguments().getString("tittle");
            category_url = getArguments().getString("category_url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_base, container, false);
        view =  inflater.inflate(R.layout.fragment_base, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        load_page=1;
        netData = new NetData();
        albumList_data = new ArrayList<>();
        indexAlbumAdapter = new IndexAlbumAdapter(getContext(),albumList_data);
        IndexAlbumTask indexAlbumTask = new IndexAlbumTask();
        indexAlbumTask.execute(1);
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);

        //RecycleView刷新和加载更多
        rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                load_page=1;
                albumList_data.clear();
                IndexAlbumTask indexAlbumTask = new IndexAlbumTask();
                indexAlbumTask.execute(load_page);
                status="onRefresh";

            }

            @Override
            public void onLoadMore() {
                load_page++;
                IndexAlbumTask indexAlbumTask = new IndexAlbumTask();
                indexAlbumTask.execute(load_page);
                rv.loadMoreComplete();
            }
        });

        indexAlbumAdapter.setOnItemClickListener(new IndexAlbumAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicListTask picListTask = new PicListTask();
                picListTask.execute(albumList_data.get(position).getLink());
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpacesItemDecoration decoration=new SpacesItemDecoration(1);
        rv.setAdapter(indexAlbumAdapter);
        rv.addItemDecoration(decoration);
        rv.setLayoutManager(layoutManager);
    }


    /**
     * 异步获取当前分类的主页PicBean
     */
    public class IndexAlbumTask extends AsyncTask<Integer,Void,List<PicBean>> {


        @Override
        protected List<PicBean> doInBackground(Integer... integers) {
            List<PicBean> albumList = new ArrayList<>();
            int page = integers[0];
            try {
                albumList = netData.getIndexAlbum(category_url,page);
            } catch (IOException e) {
                e.printStackTrace();
            }
            albumList_data.addAll(albumList);
            return albumList_data;
        }

        @Override
        protected void onPostExecute(List<PicBean> albumList_data){
            super.onPostExecute(albumList_data);
            if(status=="onRefresh"){
                rv.refreshComplete();
                status="refreshComplete";
            }
            indexAlbumAdapter.SetData(albumList_data);
            indexAlbumAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 异步获取被选中图集的整套图片信息
     */
    public class PicListTask extends AsyncTask<String,Void,List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {
            List picList = new ArrayList<>();
            try {
                picList = netData.getAlbumCount(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return picList;
        }

        @Override
        protected void onPostExecute(List<String> picList){
            super.onPostExecute(picList);
            PictureConfig config = new PictureConfig.Builder()
                    .setListData((ArrayList<String>) picList)
                    .setPosition(0)
                    .setDownloadPath("mmjpg")
                    .setIsShowNumber(true)
                    .needDownload(true)
                    .setPlacrHolder(R.mipmap.icon)
                    .build();
            ImagePagerActivity.startActivity(getActivity(), config);
        }

        }
    }
