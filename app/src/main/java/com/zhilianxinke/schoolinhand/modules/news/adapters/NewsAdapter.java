package com.zhilianxinke.schoolinhand.modules.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.App_NewsInfoModel;

import java.util.List;

/**
 * Created by hh on 2015-02-08.
 */
public class NewsAdapter extends ArrayAdapter<App_NewsInfoModel> {

    private int textViewResourceId;

    public NewsAdapter(Context context, int textViewResourceId, List<App_NewsInfoModel> objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        App_NewsInfoModel item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.textViewResourceId,null);
        ImageView imgIsHot = (ImageView)view.findViewById(R.id.imgNewsIsHot);
        //title
        TextView tvNewsTitle = (TextView)view.findViewById(R.id.tvNewsTitle);
        tvNewsTitle.setText(item.getTitle());
        //prevContent
        TextView tvNewsPrevContent = (TextView)view.findViewById(R.id.tvNewsPrevContent);
        tvNewsPrevContent.setText("");
        //publisher
        TextView tvNewsPublisher = (TextView)view.findViewById(R.id.tvNewsPublisher);
        tvNewsPublisher.setText(item.getPublicUserName());
        //publicTime
        TextView tvNewsPublishTime = (TextView)view.findViewById(R.id.tvNewsPublishTime);
        tvNewsPublishTime.setText(item.getStrPublicTime());
        return view;
    }
}