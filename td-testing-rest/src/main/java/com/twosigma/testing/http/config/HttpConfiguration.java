package com.twosigma.testing.http.config;

import com.twosigma.testing.http.HttpRequestHeader;

/**
 * @author mykola
 */
public interface HttpConfiguration {
    String fullUrl(String url);
    HttpRequestHeader fullHeader(HttpRequestHeader given);
}
