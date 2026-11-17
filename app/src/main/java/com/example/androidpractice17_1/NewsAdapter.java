package com.example.androidpractice17_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private List<News> newsList;
    private LayoutInflater inflater;

    public NewsAdapter(List<News> newsList, LayoutInflater inflater) {
        this.newsList = newsList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_news, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvNewsTitle);
            holder.tvBody = convertView.findViewById(R.id.tvNewsBody);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());

        // 正文截取前100个字符显示
        String body = news.getBody();
        if (body.length() > 100) {
            body = body.substring(0, 100) + "...";
        }
        holder.tvBody.setText(body);

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvBody;
    }

    // 更新数据并刷新列表
    public void updateData(List<News> newList) {
        this.newsList = newList;
        notifyDataSetChanged();
    }
}