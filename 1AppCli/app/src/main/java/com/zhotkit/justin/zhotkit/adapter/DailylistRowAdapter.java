package com.zhotkit.justin.zhotkit.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhotkit.justin.zhotkit.R;

import java.util.ArrayList;

public class DailylistRowAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    private ArrayList<String> mTitles = new ArrayList<String>();
    private ArrayList<String> mThumbnailUrl= new ArrayList<String>();

    ImageLoader imageLoader = ImageLoader.getInstance();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.ic_launcher).displayer(new RoundedBitmapDisplayer(100))
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .cacheInMemory(true)
            .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();


    public  DailylistRowAdapter(Activity activity, ArrayList<String> titles, ArrayList<String> urls) {
        mTitles = titles;
        mThumbnailUrl = urls;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return mTitles.size();
    }
    @Override
    public String getItem( int location ) {
        return  mTitles.get(location);
    }
    @Override
    public long getItemId(int position ) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( inflater == null ) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        ViewHolder viewHolder = null;
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.listrow_zhihu, null);
            viewHolder = new ViewHolder();
            viewHolder.thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail_daily);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_daily);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mTitles.get(position));
        imageLoader.displayImage(mThumbnailUrl.get(position), viewHolder.thumbNail, options);
        return convertView;
    }

    public class ViewHolder {
        ImageView thumbNail;
        TextView title ;
    }
}
