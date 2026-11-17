package com.example.androidpractice17_1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListView lvNews;
    private ProgressBar progressBar;
    private TextView tvError;
    private NewsAdapter adapter;
    private List<News> newsList = new ArrayList<>();
    private ExecutorService executorService;
    private Handler mainHandler;

    // 免费测试新闻接口
    private static final String API_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        adapter = new NewsAdapter(newsList, getLayoutInflater());
        lvNews.setAdapter(adapter);

        // 加载新闻
        loadNewsData();
    }

    private void initViews() {
        lvNews = findViewById(R.id.lvNews);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
    }

    // 网络加载新闻数据
    private void loadNewsData() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        lvNews.setVisibility(View.GONE);

        executorService.execute(() -> {
            String result = "";
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                reader.close();
                conn.disconnect();

                // 解析JSON
                List<News> list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int id = obj.getInt("id");
                    int userId = obj.getInt("userId");
                    String title = obj.getString("title");
                    String body = obj.getString("body");

                    News news = new News(id, userId, title, body);
                    list.add(news);
                }

                // 主线程更新UI
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    lvNews.setVisibility(View.VISIBLE);
                    adapter.updateData(list);
                });

            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "加载失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}