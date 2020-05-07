package com.example.christina_v30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {

    private List<Map<String, String>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public MyAdapter(Context context, List<Map<String, String>> data){

        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    //定义一个内部类，用来定义所需要的数据
    public class Info{
        public ImageView video_image;
        public TextView name_text;
        public TextView favourite_text;
        public TextView play_text;
        public TextView update_text;
        public TextView name_text_detail;
        public TextView favourite_text_detail;
        public TextView play_text_detail;
        public TextView update_text_detail;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Info info = new Info();
        convertView = layoutInflater.inflate(R.layout.card_layout, null);
        info.video_image = (ImageView) convertView.findViewById(R.id.video_image);
        info.name_text_detail = (TextView) convertView.findViewById(R.id.name_text_detail);
        info.favourite_text_detail = (TextView) convertView.findViewById(R.id.favourite_text_detail);
        info.play_text_detail = (TextView) convertView.findViewById(R.id.play_text_detail);
        info.update_text_detail = (TextView) convertView.findViewById(R.id.update_text_detail);
        info.name_text = (TextView) convertView.findViewById(R.id.name_text);
        info.favourite_text = (TextView) convertView.findViewById(R.id.favourite_text);
        info.play_text = (TextView) convertView.findViewById(R.id.play_text);
        info.update_text = (TextView) convertView.findViewById(R.id.update_text);

        Glide.with(context)
                .load((String) data.get(position).get("cover"))
                .into(info.video_image);
        info.name_text_detail.setText((String) data.get(position)
                .get("name_text"));
        info.favourite_text_detail.setText((String) data.get(position)
                .get("favourite_text"));
        System.out.println((String) data.get(position)
                .get("favourite_text"));
        info.play_text_detail.setText((String) data.get(position)
                .get("play_text"));
        info.update_text_detail.setText((String) data.get(position)
                .get("update_text"));


        return convertView;
    }
}
