package com.oreilly.news.compositor;

import com.google.inject.Inject;
import com.oreilly.news.command.LocalNewsHystrixCommand;
import com.oreilly.news.command.WeatherHystrixCommand;
import com.oreilly.news.http.LocalNewsApi;
import com.oreilly.news.http.WeatherApi;
import com.oreilly.news.model.CityNews;
import com.oreilly.news.model.LocalNews;
import com.oreilly.news.model.Weather;

import java.util.concurrent.Future;

public class NewsCompositor {

    @Inject
    private LocalNewsApi localNewsApi;

    @Inject
    private WeatherApi weatherApi;

    public CityNews getNewsForCity(String city) throws Exception {
        Future<LocalNews> localNewsFuture =
                new LocalNewsHystrixCommand(localNewsApi, city).queue();
        Future<Weather> weatherFuture =
                new WeatherHystrixCommand(weatherApi, city).queue();

        LocalNews localNews = localNewsFuture.get();
        Weather weather = weatherFuture.get();

        return new CityNews(city, weather, localNews);
    }
}
