package com.twosigma.znai.server.landing;

import java.util.stream.Stream;

public interface LandingDocEntriesProvider {
    Stream<LandingDocEntry> provide();
}
