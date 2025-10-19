package org.testingisdocumenting.znai.cli;

public class ZnaiTitleWithMaybeOverride {
    static final String title = init();

    private static String init() {
        var maybeOverride = System.getenv("ZNAI_APP_TITLE");
        return maybeOverride == null ? "znai" : maybeOverride;
    }
}
