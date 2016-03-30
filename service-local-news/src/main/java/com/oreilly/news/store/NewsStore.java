package com.oreilly.news.store;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.oreilly.news.model.News;
import com.thedeanda.lorem.Lorem;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NewsStore {

    private static Logger log = LoggerFactory.getLogger(NewsStore.class);

    private Client client;

    @Inject
    public NewsStore(Provider<Client> client) {
        this.client = client.get();
    }

    public News getNews(String city) {
        Map<String, Object> data = null;
        try {
            data = client
                    .prepareGet("news", "local", city.toLowerCase())
                    .execute().get(3, TimeUnit.SECONDS).getSourceAsMap();
        } catch (Exception e) {
            log.error("error retrieving news", e);
            throw new NewsException();
        }

        if (data == null) {
            throw new WebApplicationException(404);
        }

        return new News(data.get("headline").toString(),
                data.get("news").toString());
    }

    private static List<String> CITIES = ImmutableList.of(
            "chicago", "milwaukee", "san francisco", "new york",
            "portland", "seattle", "boston");

    public void deleteIndex() {
        try {
            this.client.admin().indices().prepareDelete("news").execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNews() {
        try {
            /*
             * Delete news index in case it already exists.
             */
            this.client.admin().indices().prepareDelete("news").execute().get();
            this.client.admin().indices().prepareCreate("news").execute().get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
         * Inserting fake news data.
         * Side effect! For demo purposes only!
         */
        log.info("loading news");

        CITIES.forEach((city) -> this.client.prepareIndex("news", "local", city)
                .setSource(ImmutableMap.of("news", Lorem.getParagraphs(2, 4),
                        "headline", Lorem.getWords(3, 7)))
                .execute());
    }

    public static class NewsException extends RuntimeException {

    }
}
