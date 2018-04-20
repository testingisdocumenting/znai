package com.twosigma.documentation.server.landing;

import java.util.stream.Stream;

public interface LandingDocEntriesProvider {
    Stream<LandingDocEntry> provide();
}
