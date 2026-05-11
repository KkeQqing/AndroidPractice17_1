package com.example.androidpractice17_1.bean;

// 新闻实体类：封装标题、时间、内容、图片、详情
public class News {
    private String title;    // 标题
    private String time;     // 时间
    private String content;  // 简介/内容
    private String imgUrl;   // 图片链接
    private String detail;   // 详情

    // 构造方法
    public News(String title, String time, String content, String imgUrl, String detail) {
        this.title = title;
        this.time = time;
        this.content = content;
        this.imgUrl = imgUrl;
        this.detail = detail;
    }

    // Getter & Setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}