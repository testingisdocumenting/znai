package com.twosigma.utils;

import java.util.*;

/**
 * @author mykola
 */
public class ServiceLoaderUtils {
    private ServiceLoaderUtils() {
    }

    public static <E> Set<E> load(Class<E> serviceClass) {
        ServiceLoader<E> loader = ServiceLoader.load(serviceClass);
        Set<E> result = new LinkedHashSet<>();
        loader.forEach(result::add);

        return result;
    }
}
