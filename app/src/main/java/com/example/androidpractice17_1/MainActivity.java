package com.example.androidpractice17_1;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.androidpractice17_1.adapter.NewsAdapter;
import com.example.androidpractice17_1.bean.News;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerNews;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private TextView tvError;
    private NewsAdapter adapter;
    private List<News> newsList = new ArrayList<>();
    // 公共免费新闻接口（返回JSON数据）
    private final String NEWS_URL = "https://v.juhe.cn/toutiao/index?type=top&key=5f52d7249a3ab49d6a0869c2d54b0580";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        recyclerNews = findViewById(R.id.recycler_news);
        refreshLayout = findViewById(R.id.refresh_layout);
        progressBar = findViewById(R.id.progress_bar);
        tvError = findViewById(R.id.tv_error);

        // 列表布局
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));

        // 下拉刷新
        refreshLayout.setOnRefreshListener(this::getNewsData);

        // 首次加载
        getNewsData();
    }

    // OkHttp请求新闻数据（子线程自动处理，满足要求3）
    private void getNewsData() {
        showLoading();
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(NEWS_URL).build();
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null {
                    String json = response.body().string();
                    // 解析JSON
                    parseJson(json);
                } else {
                    runOnUiThread(() -> showError());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showError());
            }
        }).start();
    }

    // 解析JSON数据
    private void parseJson(String json) {
        try {
            newsList.clear();
            JSONObject root = new JSONObject(json);
            JSONObject result = root.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                String title = obj.getString("title");
                String time = obj.getString("date");
                String content = obj.getString("author_name");
                String imgUrl = obj.getString("thumbnail_pic_s");
                String detail = obj.getString("url");

                newsList.add(new News(title, time, content, imgUrl, detail));
            }

            // 主线程更新UI（满足要求5）
            runOnUiThread(this::showNewsList);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> showError());
        }
    }

    // 显示新闻列表
    private void showNewsList() {
        hideLoading();
        tvError.setVisibility(View.GONE);
        if (adapter == null) {
            adapter = new NewsAdapter(this, newsList, news ->
                    Toast.makeText(MainActivity.this, "新闻详情："+news.getDetail(), Toast.LENGTH_SHORT).show()
            );
            recyclerNews.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // 加载中
    private void showLoading() {
        if (!refreshLayout.isRefreshing()) progressBar.setVisibility(View.VISIBLE);
    }

    // 隐藏加载
    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        refreshLayout.setRefreshing(false);
    }

    // 加载失败
    private void showError() {
        hideLoading();
        tvError.setVisibility(View.VISIBLE);
    }
}