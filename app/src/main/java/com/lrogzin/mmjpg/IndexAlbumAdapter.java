package com.lrogzin.mmjpg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.List;

/**
 * Created by 1U02UN on 2017/11/11.
 */

public class IndexAlbumAdapter extends RecyclerView.Adapter<IndexAlbumAdapter.ViewHolder>
        implements View.OnClickListener{

    private Context mContext;
    private List<PicBean> mData;
    private int position;
    private int screenWidth;
    private OnRecyclerViewItemClickListener mOnItemClickListener ;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public IndexAlbumAdapter(Context context, List<PicBean> data){
        mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        mData = data;
    }

    public void SetData(List<PicBean> data){mData = data;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String album_url = mData.get(position).getPic();
        holder.itemView.setTag(position);
        //Log.i("pic",album_url);
        GlideUrl glideUrl = new GlideUrl(album_url, new LazyHeaders.Builder()
                .addHeader("Referer", "http://www.mmjpg.com/")
                .build());
        Glide.with(mContext)
                .load(glideUrl)
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .override(screenWidth/2,screenWidth*3/4)
                .fitCenter()
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv);
        }
    }
}
