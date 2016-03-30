package com.oreilly.news.model;

public class News {
    
    String headline;
    String news;

    public News(String headline, String news) {
        this.headline = headline;
        this.news = news;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
