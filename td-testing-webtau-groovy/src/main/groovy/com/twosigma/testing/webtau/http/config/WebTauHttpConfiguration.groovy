package com.twosigma.testing.webtau.http.config

import com.twosigma.testing.http.HttpRequestHeader
import com.twosigma.testing.http.HttpUrl
import com.twosigma.testing.http.config.HttpConfiguration
import com.twosigma.testing.webtau.cfg.WebTauConfig

/**
 * @author mykola
 */
class WebTauHttpConfiguration implements HttpConfiguration {
    private WebTauConfig cfg = WebTauConfig.INSTANCE

    @Override
    String fullUrl(String url) {
        if (HttpUrl.isFull(url)) {
            return url
        }

        return HttpUrl.concat(cfg.baseUrl, url)
    }

    @Override
    HttpRequestHeader fullHeader(HttpRequestHeader given) {
        return given
    }
}
