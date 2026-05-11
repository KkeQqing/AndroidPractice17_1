package com.example.androidpractice17_1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.androidpractice17_1.R;
import com.example.androidpractice17_1.bean.News;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;
    private OnItemClickListener listener;

    // 点击事件接口
    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public NewsAdapter(Context context, List<News> newsList, OnItemClickListener listener) {
        this.context = context;
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvTime.setText(news.getTime());
        holder.tvContent.setText(news.getContent());

        // Glide加载图片
        Glide.with(context).load(news.getImgUrl()).into(holder.ivNewsImg);

        // 条目点击事件（查看详情）
        holder.itemView.setOnClickListener(v -> listener.onItemClick(news));
    }

    @Override
    public int getItemCount() { return newsList.size(); }

    // ViewHolder
    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNewsImg;
        TextView tvTitle, tvTime, tvContent;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsImg = itemView.findViewById(R.id.iv_news_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}