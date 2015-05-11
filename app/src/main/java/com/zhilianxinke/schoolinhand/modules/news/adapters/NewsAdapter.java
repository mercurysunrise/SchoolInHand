package com.zhilianxinke.schoolinhand.modules.news.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.AppNews;

import java.util.List;

/**
 * Created by hh on 2015-02-08.
 */
public class NewsAdapter extends ArrayAdapter<AppNews> {

    private int textViewResourceId;

    public NewsAdapter(Context context, int textViewResourceId, List<AppNews> objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppNews item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.textViewResourceId, null);
        ImageView imgIsHot = (ImageView)view.findViewById(R.id.imgNewsIsHot);
        //title
        TextView tvNewsTitle = (TextView)view.findViewById(R.id.tvNewsTitle);
        tvNewsTitle.setText(item.getTitle());
        //prevContent
        TextView tvNewsPrevContent = (TextView)view.findViewById(R.id.tvNewsPrevContent);
        tvNewsPrevContent.setText("");
        //publisher
        TextView tvNewsPublisher = (TextView)view.findViewById(R.id.tvNewsPublisher);
        tvNewsPublisher.setText(item.getAuthorName());
        //publicTime
        TextView tvNewsPublishTime = (TextView)view.findViewById(R.id.tvNewsPublishTime);
        if (item.getCover() != null && item.getCover().toString().length() > 10){
            tvNewsPublishTime.setText(item.getCover().toString().substring(0,10));
        }

        if (item.isReaded()){
            setReadState(view);
//            view.setAlpha(180);
//            view.setBackgroundColor(Color.GRAY);
        }

        return view;
    }

    public static void setReadState(View view){
        int readColor = Color.GRAY;
        TextView tvNewsTitle = (TextView)view.findViewById(R.id.tvNewsTitle);
        tvNewsTitle.setTextColor(readColor);
        TextView tvNewsPublisher = (TextView)view.findViewById(R.id.tvNewsPublisher);
        tvNewsPublisher.setTextColor(readColor);
        TextView tvNewsPublishTime = (TextView)view.findViewById(R.id.tvNewsPublishTime);
        tvNewsPublishTime.setTextColor(readColor);
    }
}
