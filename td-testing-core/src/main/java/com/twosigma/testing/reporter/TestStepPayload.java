package com.twosigma.testing.reporter;

import java.util.Map;

/**
 * @author mykola
 */
public interface TestStepPayload {
    Map<String, ?> toMap();
}
