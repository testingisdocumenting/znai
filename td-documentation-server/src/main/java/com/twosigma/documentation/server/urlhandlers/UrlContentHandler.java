package com.twosigma.documentation.server.urlhandlers;

import com.twosigma.documentation.html.reactjs.ReactJsBundle;

public interface UrlContentHandler {
    String url();
    String buildContent(ReactJsBundle reactJsBundle);
}
