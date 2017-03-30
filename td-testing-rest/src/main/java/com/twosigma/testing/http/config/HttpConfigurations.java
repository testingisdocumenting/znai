package com.twosigma.testing.http.config;

import java.util.List;

import com.twosigma.testing.http.testserver.TestServer;
import com.twosigma.utils.ServiceUtils;

/**
 * @author mykola
 */
public class HttpConfigurations {
    private static List<HttpConfiguration> configurations = ServiceUtils.discover(HttpConfiguration.class);

    public static void add(HttpConfiguration configuration) {
        configurations.add(configuration);
    }

    public static void remove(final HttpConfiguration configuration) {
        configurations.remove(configuration);
    }

    public static String fullUrl(String url) {
        String finalUrl = url;
        for (HttpConfiguration configuration : configurations) {
            finalUrl = configuration.fullUrl(finalUrl);
        }

        return finalUrl;
    }
}
