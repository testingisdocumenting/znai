package com.twosigma.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author mykola
 */
public class ServiceUtils {
    public static <E> List<E> discover(Class<E> serviceClass) {
        ServiceLoader<E> loader = ServiceLoader.load(serviceClass);
        List<E> services = new ArrayList<E>();
        loader.forEach(services::add);

        return services;
    }
}
