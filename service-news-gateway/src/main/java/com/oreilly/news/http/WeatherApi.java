package com.oreilly.news.http;

import com.oreilly.news.model.Weather;
import retrofit.http.GET;
import retrofit.http.Path;

public interface WeatherApi {

    @GET("/weather/{city}")
    Weather getWeatherForCity(@Path("city") String city);
}
