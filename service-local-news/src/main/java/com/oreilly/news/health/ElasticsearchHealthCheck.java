package com.oreilly.news.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.Client;

public class ElasticsearchHealthCheck extends HealthCheck {

    private Client client;

    @Inject
    public ElasticsearchHealthCheck(Provider<Client> client) {
        this.client = client.get();
    }

    @Override
    protected Result check() throws Exception {
        Result result = Result.healthy();

        try {
            ClusterHealthStatus status = client.admin()
                    .cluster()
                    .health(new ClusterHealthRequest("news"))
                    .actionGet()
                    .getStatus();

            if (status == ClusterHealthStatus.RED) {
                result = Result.unhealthy("Elasticsearch cluster in unhealthy state");
            }
        } catch(Exception ex) {
            result = Result.unhealthy(ex);
        }

        return result;
    }
}
