package com.oreilly.news;

import com.google.inject.Inject;
import com.oreilly.news.model.News;
import com.oreilly.news.store.NewsStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class NewsResource {

    @Inject
    private NewsStore newsStore;

    @GET
    @Path("/news/{city}")
    @Produces("application/json")
    public News getNews(@PathParam("city") String city) {
        return newsStore.getNews(city);
    }

    @GET
    @Path("/load")
    public Response loadNews() {
        newsStore.loadNews();

        return Response.ok().entity("OK").build();
    }

    @GET
    @Path("/delete")
    public Response deleteNewsIndex() {
        newsStore.deleteIndex();

        return Response.ok().entity("OK").build();
    }
}
