package com.twosigma.documentation.server.urlhandlers;

import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;

public interface UrlContentHandler {
    String url();
    String buildContent(ReactJsNashornEngine nashornEngine);
}
