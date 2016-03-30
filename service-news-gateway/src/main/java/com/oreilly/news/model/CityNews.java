package com.oreilly.news.model;

public class CityNews {

    private String city;
    private Weather weather;
    private LocalNews localNews;

    public CityNews(String city, Weather weather, LocalNews localNews) {
        this.city = city;
        this.weather = weather;
        this.localNews = localNews;
    }

    public String getCity() {
        return city;
    }

    public Weather getWeather() {
        return weather;
    }

    public LocalNews getLocalNews() {
        return localNews;
    }
}
