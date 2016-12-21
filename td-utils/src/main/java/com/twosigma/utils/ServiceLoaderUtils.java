package com.twosigma.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author mykola
 */
public class ServiceLoaderUtils {
    private ServiceLoaderUtils() {
    }

    public static <E> List<E> load(Class<E> serviceClass) {
        ServiceLoader<E> loader = ServiceLoader.load(serviceClass);
        List<E> result = new ArrayList<>();
        loader.forEach(result::add);

        return result;
    }
}
