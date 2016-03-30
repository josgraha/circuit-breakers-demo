package com.oreilly.news.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalNews {

    private String headline;
    private String news;

    public static final LocalNews NO_NEWS = new LocalNews("No local news found", "");

    public LocalNews() {
    }

    public LocalNews(String headline, String news) {
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
