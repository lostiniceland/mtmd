package com.mtmd.ui;

import com.mtmd.ui.infrastructure.client.ApiClient;
import com.mtmd.ui.infrastructure.client.gen.V1IceApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Apis {

    @Value("${endpoint.ice.host}")
    private String endpointHost;
    @Value("${endpoint.ice.port}")
    private String endpointPort;

    private final ConcurrentHashMap<String, ApiClient> apiClientCache = new
            ConcurrentHashMap<>();

    private volatile OkHttpClient okHttpClient;

    @PostConstruct
    void initialize() {
        okHttpClient = createOkHttpClient();
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.build();
    }



    OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    V1IceApi getIceApi() {
        return new V1IceApi(getApiClientForIce());
    }


    private ApiClient getApiClientForIce() {
        return apiClientCache.computeIfAbsent("ice", ignored -> createBasicApiClient());
    }


    private ApiClient createBasicApiClient() {
        return new
                ApiClient().setBasePath(String.format("http://%s:%s", endpointHost, endpointPort))
                .setHttpClient(okHttpClient)
                .setDebugging(false);
    }
}
