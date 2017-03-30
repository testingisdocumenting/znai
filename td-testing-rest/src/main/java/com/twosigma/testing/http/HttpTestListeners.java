package com.twosigma.testing.http;

import java.util.List;

import com.twosigma.utils.ServiceUtils;

/**
 * @author mykola
 */
public class HttpTestListeners {
    private static List<HttpTestListener> listeners = ServiceUtils.discover(HttpTestListener.class);

    public static void add(HttpTestListener listener) {
        listeners.add(listener);
    }

    public static void remove(HttpTestListener listener) {
        listeners.remove(listener);
    }

    public static void afterValidation(HttpValidationResult validationResult) {
        listeners.forEach(l -> l.afterValidation(validationResult));
    }
}
