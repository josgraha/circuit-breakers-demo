package com.oreilly.news;

import com.google.inject.Inject;
import com.oreilly.news.compositor.NewsCompositor;
import com.oreilly.news.model.CityNews;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/city")
public class CityNewsResource {

    @Inject
    private NewsCompositor newsCompositor;

    @GET
    @Path("/{city}/news")
    @Produces("application/json")
    public CityNews getCityNews(@PathParam("city") String city) throws Exception {
        return newsCompositor.getNewsForCity(city);
    }
}
