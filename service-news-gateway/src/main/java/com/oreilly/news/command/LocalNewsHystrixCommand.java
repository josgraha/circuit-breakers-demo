package com.oreilly.news.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.oreilly.news.http.LocalNewsApi;
import com.oreilly.news.model.LocalNews;

public class LocalNewsHystrixCommand extends HystrixCommand<LocalNews> {

    private LocalNewsApi localNewsApi;
    private String city;

    public LocalNewsHystrixCommand(LocalNewsApi localNewsApi, String city) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("LocalNews"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(5000)));
        this.localNewsApi = localNewsApi;
        this.city = city;
    }

    @Override
    protected LocalNews run() throws Exception {
        return localNewsApi.getLocalNewsForCity(city);
    }

    @Override
    protected LocalNews getFallback() {
        return LocalNews.NO_NEWS;
    }
}
