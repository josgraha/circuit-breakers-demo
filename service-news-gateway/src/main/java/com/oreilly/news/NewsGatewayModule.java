package com.oreilly.news;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.oreilly.microservices.discovery.ClientFactory;
import com.oreilly.news.compositor.NewsCompositor;
import com.oreilly.news.http.LocalNewsApi;
import com.oreilly.news.http.WeatherApi;

public class NewsGatewayModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CityNewsResource.class).in(Scopes.SINGLETON);
        bind(NewsCompositor.class).in(Scopes.SINGLETON);
    }

    @Provides
    public LocalNewsApi localNewsApi(ClientFactory clientFactory) {
        return clientFactory.buildClient("local-news-service",
                LocalNewsApi.class);
    }

    @Provides
    public WeatherApi weatherApi(ClientFactory clientFactory) {
        return clientFactory.buildClient("weather-service",
                WeatherApi.class);
    }
}
