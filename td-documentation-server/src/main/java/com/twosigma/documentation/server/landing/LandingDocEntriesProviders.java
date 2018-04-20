package com.twosigma.documentation.server.landing;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.stream.Stream;

public class LandingDocEntriesProviders {
    private static Set<LandingDocEntriesProvider> providers =
            ServiceLoaderUtils.load(LandingDocEntriesProvider.class);

    public static void add(LandingDocEntriesProvider provider) {
        providers.add(provider);
    }

    public static Stream<LandingDocEntry> provide() {
        return providers.stream().flatMap(LandingDocEntriesProvider::provide);
    }
}
