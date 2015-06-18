package com.zhilianxinke.schoolinhand.modules.news.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhilianxinke.schoolinhand.R;
import com.zhilianxinke.schoolinhand.domain.AppNews;
import com.zhilianxinke.schoolinhand.util.UrlBuilder;

import java.util.List;

/**
 * Created by hh on 2015-02-08.
 */
public class NewsAdapter extends ArrayAdapter<AppNews> {

    DisplayImageOptions options;
    private int textViewResourceId;

    public NewsAdapter(Context context, int textViewResourceId, List<AppNews> objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory()                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc()                          // 设置下载的图片是否缓存在SD卡中
                .build();                                   // 创建配置过得DisplayImageOption对象
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppNews item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(this.textViewResourceId, null);
        ImageView imgIsHot = (ImageView)view.findViewById(R.id.imgNewsIsHot);

        System.out.println("------------");
        System.out.print(item.getImage());
        if(item.getImage()!=null){
            ImageView nr_img = (ImageView)view.findViewById(R.id.nr_image);
            nr_img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(UrlBuilder.thumbnailImageUrl(item.getImage()), nr_img,options);
        }
        //title
        TextView tvNewsTitle = (TextView)view.findViewById(R.id.tvNewsTitle);
        tvNewsTitle.setText(item.getTitle());
        int picture = R.drawable.logo;
        switch(item.getKey()){
            case "活动":
                picture = R.drawable.rc_conversation_list_msg_send_failure;
                break;
            case "公告":
                picture = R.drawable.rc_ic_smiley_selected;
                break;
            case "相册":
                picture = R.drawable.u1f4a9;
                break;
            case "分享":
                picture = R.drawable.u1f4e2;
                break;
            default:
                break;
        }
        imgIsHot.setImageResource(picture);
        //prevContent
        TextView tvNewsPrevContent = (TextView)view.findViewById(R.id.tvNewsPrevContent);
        tvNewsPrevContent.setText(item.getContent());
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
