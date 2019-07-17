package com.twosigma.znai.server.urlhandlers;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.stream.Stream;

public class UrlContentHandlers {
    private static Set<UrlContentHandler> handlers =
            ServiceLoaderUtils.load(UrlContentHandler.class);

    public static void add(UrlContentHandler provider) {
        handlers.add(provider);
    }

    public static Stream<UrlContentHandler> urlContentHandlers() {
        return handlers.stream();
    }
}
