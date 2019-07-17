package com.twosigma.znai.server.urlhandlers;

import com.twosigma.znai.html.reactjs.ReactJsBundle;

public interface UrlContentHandler {
    String url();
    String buildContent(ReactJsBundle reactJsBundle);
}
