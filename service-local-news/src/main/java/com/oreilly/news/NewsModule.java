package com.oreilly.news;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.oreilly.news.health.ElasticsearchHealthCheck;
import com.oreilly.news.store.NewsStore;
import com.typesafe.config.Config;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;

public class NewsModule extends RequestScopeModule {

    @Override
    public void configure() {
        bind(NewsStore.class).in(Scopes.SINGLETON);
        bind(NewsResource.class).in(Scopes.SINGLETON);
        bind(ElasticsearchHealthCheck.class).in(Scopes.SINGLETON);
        bind(Client.class).toProvider(ClientProvider.class);
    }

    public static class ClientProvider implements Provider<Client> {

        @Inject
        private Config config;

        private Client client;

        @Override
        public Client get() {
            return client == null ? new TransportClient()
                    .addTransportAddress(new InetSocketTransportAddress(
                            config.getString("elasticsearch.host"), 9300))
                    : client;
        }
    }
}
